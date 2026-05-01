package com.Financial_backend.Financial_backend.Service;

import com.Financial_backend.Financial_backend.Dto.Request.EmployeeRequestDto;
import com.Financial_backend.Financial_backend.Dto.Response.EmployeeResponseDto;
import com.Financial_backend.Financial_backend.Entity.SponsorEntity;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import com.Financial_backend.Financial_backend.Enum.EmployeeStatus;
import com.Financial_backend.Financial_backend.Enum.Role;
import com.Financial_backend.Financial_backend.Respository.SponsorRepository;
import com.Financial_backend.Financial_backend.Respository.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final UsersRepository usersRepository;
    private  final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final SponsorRepository sponsorRepository;

    public EmployeeService (UsersRepository usersRepository,PasswordEncoder passwordEncoder, EmailService emailService, SponsorRepository sponsorRepository){
        this.usersRepository = usersRepository;
        this.passwordEncoder= passwordEncoder;
        this.emailService = emailService;
        this.sponsorRepository= sponsorRepository;
    }

    //----------------------------------------
    //adding a single employee manually
    //----------------------------------------
    @Transactional
    public EmployeeResponseDto addEmployee(EmployeeRequestDto employeeRequestDto, SponsorEntity sponsor){


        //fetch the sponsor
        SponsorEntity sponsorEntity = sponsorRepository.findById(sponsor.getId())
                .orElseThrow(() -> new RuntimeException("Sponsor not found"));
        // check of the email is already used
        if(usersRepository.existsByEmail(employeeRequestDto.getEmail())){
            throw  new RuntimeException(
                    "email already registered"+employeeRequestDto.getEmail()
            );
        }

        //generate temp password
        String tempPassword = generateTempPassword();


        UsersEntity usersEntity = new UsersEntity();

        usersEntity. setFirstName(employeeRequestDto.getFirstName());
        usersEntity.setLastName(employeeRequestDto.getLastName());
        usersEntity.setPhone(employeeRequestDto.getPhone());
        usersEntity.setEmail(employeeRequestDto.getEmail());
        usersEntity.setDepartment(employeeRequestDto.getDepartment());
        usersEntity.setJobTitle(employeeRequestDto.getJobTitle());
        usersEntity.setAnnualSalary(employeeRequestDto.getAnnualSalary());
        usersEntity.setStartDate(employeeRequestDto.getStartDate());
        usersEntity.setDeferralRate(String.valueOf(employeeRequestDto.getDeferralRate() !=null ? employeeRequestDto.getDeferralRate() : 0.03)); // default percentage


        usersEntity.setPassword_hash(passwordEncoder.encode(tempPassword));
        usersEntity.setRole(Role.EMPLOYEE);
        usersEntity.setSponsor(sponsorEntity);
//        usersEntity.setEmployeeId(employeeId);  //EMP-00182
        usersEntity.setStatus(EmployeeStatus.PENDING);
        usersEntity.setCreated_at(LocalDateTime.now());
        usersEntity.setUpdated_at(LocalDateTime.now());
        usersEntity.setIs_active(true);
        usersEntity.setIsTempPassword(true);

        UsersEntity saved = usersRepository.save(usersEntity);

       saved.setEmployeeId(String.format("EMP-%05d",saved.getId()));

       usersRepository.save(saved);

        //send invite mail to employees
        emailService.sendEmployeeInviteEmail(
                saved.getEmail(),
                saved.getFirstName(),
                sponsorEntity.getCompany_name(),
                sponsorEntity.getEnrollmentCode(),
                tempPassword
        );

     // send it to the response dto
      EmployeeResponseDto response = new EmployeeResponseDto();

      response.setFirstName(saved.getFirstName());
      response.setLastName(saved.getLastName());
      response.setPhone(saved.getPhone());
      response.setEmail(saved.getEmail());
      response.setDepartment(saved.getDepartment());
      response.setJobTitle(saved.getJobTitle());
      response.setAnnualSalary(saved.getAnnualSalary());
      response.setEmployeeId(saved.getEmployeeId());
      response.setStatus(saved.getStatus());
      response.setStartDate(saved.getStartDate());
      response.setDeferralRate(Double.valueOf(String.valueOf(saved.getDeferralRate())));

      return response;
    }


    //-------------------------------------
    //Get all employees for this sponsor
    //-------------------------------------

    public List <UsersEntity> getEmployee(SponsorEntity sponsor){

        return usersRepository.findEmployeeBySponsor(
                sponsor,Role.EMPLOYEE
        );
    }

    //-------------------------------------
    //HELPERS
    //-------------------------------------
    private String generateTempPassword(){
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String digits ="1234567890";

        return "Emp@"
                + upper.charAt((int)(Math.random() * upper.length()))
                +lower.charAt((int)(Math.random() * lower.length()))
                +digits.charAt((int)(Math.random() * digits.length()))
                + upper.charAt((int)(Math.random() * upper.length()));

    }




}
