package com.Financial_backend.Financial_backend.PlanLoan.Employee;

import com.Financial_backend.Financial_backend.PlanLoan.Employee.Exception.EmployeeNotExistException;
import com.Financial_backend.Financial_backend.PlanLoan.Employee.Exception.SponsorNotExistException;
import com.Financial_backend.Financial_backend.PlanLoan.Employee.RequestDto.PlanLoanRequestDto;
import com.Financial_backend.Financial_backend.PlanLoan.Employee.ResponseDto.PlanLoanResponseDto;
import com.Financial_backend.Financial_backend.Entity.SponsorEntity;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import com.Financial_backend.Financial_backend.PlanLoan.PlanLoanEntity;
import com.Financial_backend.Financial_backend.PlanLoan.PlanLoanRepository;
import com.Financial_backend.Financial_backend.Respository.SponsorRepository;
import com.Financial_backend.Financial_backend.Respository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service

@RequiredArgsConstructor
public class PlanLoanService {

    private final PlanLoanRepository planLoanRepository;
    private final SponsorRepository sponsorRepository;
    private final UsersRepository usersRepository;

    // create a new loan request
    @Transactional
    public PlanLoanResponseDto requestNewLoan(
            PlanLoanRequestDto loanData, SponsorEntity sponsor, UsersEntity loggedInUser
    ){
        //fetch the user
        UsersEntity usersEntity = usersRepository.findById(loggedInUser.getId())
                .orElseThrow(()->new EmployeeNotExistException("user does not exist"));

        //fetch the sponsor
        SponsorEntity sponsorEntity =sponsorRepository.findById(sponsor.getId())
                .orElseThrow(()->new  SponsorNotExistException("sponsor does not exist "));

        //proceed with the request
        PlanLoanEntity planLoan = PlanLoanEntity.builder()
                .loanPurpose(loanData.getLoanPurpose())
                .loanAmount(loanData.getLoanAmount())
                .repaymentTerm(loanData.getRepaymentTerm())
                .requestedTime(LocalDateTime.now())
                .sponsor(sponsorEntity)
                .user(usersEntity)
                .status(LoanStatus.PENDING)
                .build();
        PlanLoanEntity planLoanEntity = planLoanRepository.save(planLoan);

        planLoanEntity.setLoanId(String.format("LOAN-%05d",planLoanEntity.getId()));

        //return the thing
        PlanLoanResponseDto planLoanResponseDto = PlanLoanResponseDto.builder()
                .loanPurpose(planLoan.getLoanPurpose())
                .loanAmount(planLoan.getLoanAmount())
                .requestedTime(planLoan.getRepaymentTerm())
                .loanId(planLoan.getLoanId())
                .id(String.valueOf(planLoan.getId()))
                .requestedTime(String.valueOf(planLoan.getRequestedTime()))
                .build();

        return planLoanResponseDto;
    }



    /// fetch the loan status
    public List<PlanLoanResponseDto>  fetchLoanStatus(
            SponsorEntity sponsor,
            UsersEntity users
    ){


        //check if the user exist or not
        UsersEntity usersEntity = usersRepository.findById(users.getId())
                .orElseThrow(()-> new EmployeeNotExistException("employee does not exist"));

        //check if the sponsor exist
        SponsorEntity sponsorEntity = sponsorRepository.findById(sponsor.getId())
                .orElseThrow(()->new SponsorNotExistException("sponsor does not exist"));



        /// now fetch the loan status for the logged on user and the role should be employee

        return planLoanRepository.findByUserAndUserType(
              users
        )
                .stream()
                .map(planLoanEntity -> {
                    PlanLoanResponseDto planLoanResponseDto = PlanLoanResponseDto.builder()
                            .loanId(planLoanEntity.getLoanId())
                            .loanAmount(planLoanEntity.getLoanAmount())
                            .loanPurpose(planLoanEntity.getLoanPurpose())
                            .requestedTime(planLoanEntity.getRepaymentTerm())
                            .status(planLoanEntity.getStatus())
                            .requestedTime(String.valueOf(planLoanEntity.getRequestedTime()))
                            .build();
                    return planLoanResponseDto;
                }
                )
                .collect(Collectors.toList());

    }

}
