package com.Financial_backend.Financial_backend.Payroll;


import com.Financial_backend.Financial_backend.Dto.Request.PayrollUploadRequestDto;
import com.Financial_backend.Financial_backend.Entity.SponsorEntity;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import com.Financial_backend.Financial_backend.Respository.ContributionBatchRepository;
import com.Financial_backend.Financial_backend.Respository.ContributionLineItemRepository;
import com.Financial_backend.Financial_backend.Respository.UsersRepository;
import com.Financial_backend.Financial_backend.Service.PayrollService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class PayrollServiceTest {

    //1. create fake repositories
@Mock private ContributionBatchRepository contributionBatchRepository;
@Mock private ContributionLineItemRepository contributionLineItemRepository;
@Mock private UsersRepository usersRepository;

   //2. inject the service we are about to test
    @InjectMocks
    private PayrollService payrollService;

    //3. Reusable test data

    private SponsorEntity sponsor;
    private UsersEntity employee;
    private UsersEntity employer;
    private PayrollUploadRequestDto requestDto;


    //4. build fake data
    @BeforeEach
    void setUp(){

        //a. build a fake sponsor
        sponsor = SponsorEntity.builder()
                .build();

    }


}
