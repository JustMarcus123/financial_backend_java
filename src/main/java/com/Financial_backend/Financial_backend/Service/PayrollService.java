package com.Financial_backend.Financial_backend.Service;

import com.Financial_backend.Financial_backend.Dto.Request.PayrollUploadRequestDto;
import com.Financial_backend.Financial_backend.Dto.Response.EmployeeResponseDto;
import com.Financial_backend.Financial_backend.Dto.Response.PayrollResponseDto;
import com.Financial_backend.Financial_backend.Entity.ContributionBatchEntity;
import com.Financial_backend.Financial_backend.Entity.ContributionLineItemEntity;
import com.Financial_backend.Financial_backend.Entity.SponsorEntity;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import com.Financial_backend.Financial_backend.Enum.BatchStatus;
import com.Financial_backend.Financial_backend.Enum.PayrollType;
import com.Financial_backend.Financial_backend.Respository.ContributionBatchRepository;
import com.Financial_backend.Financial_backend.Respository.ContributionLineItemRepository;
import com.Financial_backend.Financial_backend.Respository.UsersRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class PayrollService {

    private final ContributionBatchRepository contributionBatchRepository;
    private final UsersRepository usersRepository;
    private final ContributionLineItemRepository contributionLineItemRepository;


    //IRS LIMIT 2026
     private static final double IRS_LIMIT_2026 = 23000.0;

    //----------------------------
    //UPLOAD PAYROLL CSV
    //----------------------------

    @Transactional

    public PayrollResponseDto payrollUpload(
            MultipartFile file,
            PayrollUploadRequestDto requestDto,
            UsersEntity uploadedBy //store the loggedIn person data once the method is triggered.
    ) {

        //get the sponsor to know who uploads the payroll
        SponsorEntity sponsor = uploadedBy.getSponsor();

        //generate batch code
        String batchCode = generateBatchCode(sponsor.getCompany_name());

        //Create the batch
        ContributionBatchEntity batch = ContributionBatchEntity.builder()
                .sponsor(sponsor)
                .batchCode(batchCode)
                .payPeriod(requestDto.getPayPeriod())
                .payDate(requestDto.getPayDate())
                .payrollType(PayrollType.valueOf(requestDto.getPayrollType()))
                .batchStatus(BatchStatus.PENDING)
                .uploadedBy(uploadedBy.getEmail())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        contributionBatchRepository.save(batch);

        // parse CSV and Create line items
       List<ContributionLineItemEntity> lineItems = parseCSV(
               file,batch,sponsor);

       //calculate totals
        double totalEmployee = lineItems.stream()
                .mapToDouble(lineItemEntity ->lineItemEntity.getEmployeeAmount() !=null ? lineItemEntity.getEmployeeAmount() :0)
                .sum();

        double totalMatch = lineItems.stream()
                .mapToDouble(lineItemEntity-> lineItemEntity.getEmployerMatch() !=null ?
                        lineItemEntity.getEmployerMatch():0)
                .sum();

        // update batch with totals
        batch.setTotalParticipant(lineItems.size());
        batch.setTotalEmployeeAmount(totalEmployee);
        batch.setTotalEmployerMatch(totalMatch);
        batch.setTotalAmount(totalEmployee + totalMatch);
        batch.setBatchStatus(BatchStatus.PROCESSING);
        batch.setUpdatedAt(LocalDateTime.now());
        contributionBatchRepository.save(batch);

        //process each line and update the balance
        processLineItems(lineItems, batch, sponsor);

        return mapToResponse(batch);

    }

    //-----------------------------------
    //parse csv
    //-----------------------------------
    private List <ContributionLineItemEntity> parseCSV(
            MultipartFile file,
            ContributionBatchEntity batch,
            SponsorEntity sponsor
    ){
        List <ContributionLineItemEntity> lineItems = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(file.getInputStream()))){
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) !=null){

                //skip header row
                if(isHeader){
                    isHeader = false;
                    continue;
                }

                //skip empty lines
                if(line.trim().isEmpty()) continue;

                String[] cols = line.split(",");

                if(cols.length < 3) continue;

                String employeeId = cols[0].trim(); //EMP-00012
                double grossSalary = Double.parseDouble(cols[1].trim());
                double deferralRate = Double.parseDouble(cols[2].trim());
                String payDate = cols[3].trim();

                //find the employee by their code and sponsor
                Optional<UsersEntity> employeeOpt =
                     usersRepository.findByEmpCodeAndSponsor(
                             employeeId, sponsor
                     );

                if(employeeOpt.isEmpty()){
                    //employee not found --- skip with a note
                    ContributionLineItemEntity FailedLine =
                            ContributionLineItemEntity.builder()
                                    .batch(batch)
                                    .employeeCode(employeeId)
                                    .grossSalary(grossSalary)
                                    .deferralRate(deferralRate)
                                    .payDate(payDate)
                                    .posted(false)
                                    .failureReason("Employee not found" + employeeId)
                                    .build();
                    contributionLineItemRepository.save(FailedLine);
                    continue;
                }

                //if employee is found
                UsersEntity employee = employeeOpt.get();

                //calculate amounts
                double employeeAmount = grossSalary * deferralRate;
                double employerMatch = calculateMatch(
                        grossSalary, employeeAmount, sponsor
                );

                //check IRS limit
                double ytdDeferred = getYTDDeferred(employee); //we send the employee as object for checking the IRS limit
                boolean irsLimitReached = false;

                if(ytdDeferred + employeeAmount > IRS_LIMIT_2026){

                    //cap at limit
                    employeeAmount = Math.max(0, IRS_LIMIT_2026 - ytdDeferred );
                    irsLimitReached = true;
                }
                ContributionLineItemEntity lineItem = ContributionLineItemEntity.builder()
                        .batch(batch)
                        .employee(employee)
                        .employeeCode(employeeId)
                        .grossSalary(grossSalary)
                        .deferralRate(deferralRate)
                        .payDate(payDate)
                        .employeeAmount(employeeAmount)
                        .employerMatch(employerMatch)
                        .totalAmount(employeeAmount + employerMatch)
                        .irsLimitReached(irsLimitReached)
                        .posted(false)
                        .build();

                contributionLineItemRepository.save(lineItem);  // ✅ save to DB
                lineItems.add(lineItem);                         // ✅ add to list
            }
        } catch (Exception e){
            throw new RuntimeException(
                    "Failed to parse CSV:" + e.getMessage()
            );
        }
        return lineItems;
    }

    //--------------------------------------
    //PROCESS LINE ITEMS - update account balance
    //--------------------------------------

    @Transactional
    private void processLineItems(

            List <ContributionLineItemEntity> lineItemEntities,
            ContributionBatchEntity contributionBatchEntity,
            SponsorEntity sponsor
    ){
        int successCount = 0;
        int failCount = 0;

        for (ContributionLineItemEntity lineItem : lineItemEntities){

            try {

                UsersEntity employee = lineItem.getEmployee();
                if(employee ==null){
                    failCount ++;
                    continue;
                }

                //Update employee 401k balance
                double currentBalance = employee.getBalance() !=null ? employee.getBalance() : 0.00;

                employee.setBalance(
                        currentBalance + lineItem.getTotalAmount()
                );

                employee.setUpdated_at(LocalDateTime.now());

                usersRepository.save(employee);

                //Mark line as spotted
                lineItem.setPosted(true);
                contributionLineItemRepository.save(lineItem);

                successCount ++;

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }


    //---------------------------------------
    //HELPER
    //---------------------------------------

    //calculate employer match based on plan rules
    //eg: 50% uo to 8% of salary

    private double calculateMatch(
            double grossSalary,
            double employeeAmount,
            SponsorEntity sponsor
    ){
        double matchPct = sponsor.getMatchPercentage(); //eg: 0.05 --50%
        double matchCap = sponsor.getMatchCap();  //eg: 0.08 --8%

        double maxMatchable = grossSalary * matchCap;
        double matchable = maxMatchable * matchPct;

        return matchable;
    }

    private String generateBatchCode(String companyName){
        String prefix = companyName
                .replace("[^a-zA-Z0-9]", "")
                .toUpperCase()
                .substring(0,Math.min(4,companyName.length()));

        String timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("MMddHHmm"));
        return "BATCH-"+ prefix + "-" +timestamp;

        //eg: BATCH-ACME-03151430
    }

    private double getYTDDeferred(UsersEntity employee){

        //let's sum all the post item amount for this employee this

        return contributionLineItemRepository.findByEmployee(employee).stream()
                .filter(lineItem -> Boolean.TRUE.equals(lineItem.getPosted()))
                .mapToDouble(lineItem->lineItem.getEmployeeAmount() != null ?
                        lineItem.getEmployeeAmount() : 0)
                .sum();
    }

    //Map entity to response dto

    private PayrollResponseDto mapToResponse(ContributionBatchEntity batchEntity){

        return PayrollResponseDto.builder()
                .id(batchEntity.getId())
                .batchCode(batchEntity.getBatchCode())
                .payPeriod(batchEntity.getPayPeriod())
                .payDate(batchEntity.getPayDate())
                .payrollType(batchEntity.getPayrollType().name())
                .batchStatus(batchEntity.getBatchStatus().name())
                .totalParticipant(batchEntity.getTotalParticipant())
                .totalEmployeeAmount(batchEntity.getTotalEmployeeAmount())
                .totalEmployerMatch(batchEntity.getTotalEmployerMatch())
                .totalAmount(batchEntity.getTotalAmount())
                .uploadedBy(batchEntity.getUploadedBy())
                .createdAt(batchEntity.getCreatedAt() !=null ? batchEntity.getCreatedAt().toString(): null)
                .failureReason(batchEntity.getFailureReason())
                .build();

    }


    //---------------------------------------
    //fetching 401(k) balance-- for now it is only the contribution and employer match and latter we will update the earnings too
    //---------------------------------------

    public EmployeeResponseDto find401kBalance (UsersEntity loggedInUser ){

        //check if users exist or not

        UsersEntity user = usersRepository.findByEmail(loggedInUser.getEmail())
                .orElseThrow(()-> new RuntimeException("user not found"));

        return EmployeeResponseDto.builder()
                .employeeId(user.getEmployeeId())
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .balance(user.getBalance() !=null ? user.getBalance() : 0.00)
                .build();  // Only when .build() is called does it take all those stored values and construct


    }



}
