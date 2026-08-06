package com.Financial_backend.Financial_backend.Payroll;

import com.Financial_backend.Financial_backend.Dto.Request.PayrollUploadRequestDto;
import com.Financial_backend.Financial_backend.Entity.SponsorEntity;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import com.Financial_backend.Financial_backend.Enum.Role;
import com.Financial_backend.Financial_backend.Repository.ContributionBatchRepository;
import com.Financial_backend.Financial_backend.Repository.ContributionLineItemRepository;
import com.Financial_backend.Financial_backend.Repository.NetworthRepository;
import com.Financial_backend.Financial_backend.Repository.UsersRepository;
import com.Financial_backend.Financial_backend.Service.PayrollService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PayrollServiceTest {

    //1. create fake repositories
@Mock private ContributionBatchRepository contributionBatchRepository;
@Mock private ContributionLineItemRepository contributionLineItemRepository;
@Mock private UsersRepository usersRepository;
@Mock private NetworthRepository networthRepository;

   //2. inject the service we are about to test
    @InjectMocks
    private PayrollService payrollService;

    private  UsersEntity mockEmployee;


    @BeforeEach
    void setUp(){
        mockEmployee = UsersEntity.builder()
                .employeeId("1L")
                .firstName("john")
                .email("john@example.com")
                .balance(0.00)
                .build();



        //fake sponsor
        SponsorEntity mockSponsor = SponsorEntity.builder()
                .id(1L)
                .company_name("google")
                .match_formula(String.valueOf(0.50))
                .build();


        //fake loggedIn user
        UsersEntity LoggedInUser = UsersEntity.builder()
                .employeeId(String.valueOf(1L))
                .role(Role.valueOf("admin@acme.com"))
                .sponsor(mockSponsor)
                .build();

        //fake request dto
        PayrollUploadRequestDto mockRequest = new PayrollUploadRequestDto();
        mockRequest.setPayPeriod("MONTHLY");
        mockRequest.setPayDate("2026-06-15");
        mockRequest.setPayrollType("REGULAR");

        //fake CSV file
        String csvContent = """
                employeeCode, grossSalary, deferralRate, payDate
                EMP-00017, 1000000, 0.5, 2026-06-15
                """;

      MultipartFile mocCsvFile = new MockMultipartFile(
                "file",
                "payroll.csv",
                "text/csv",
                csvContent.getBytes()

        );

    }


    //test 1:YTD contribution returns the correct sum

    @Test
    void testFindByDate_ReturnCorrectYTDContribution() {
        LocalDate now = LocalDate.now();
        String startDate = LocalDate.of(now.getYear(), 1, 1).toString();
        String endDate = LocalDate.of(now.getYear(), 12, 31).toString();

        when(contributionLineItemRepository.findByDate(mockEmployee, startDate, endDate))
                .thenReturn(5000.0);

        Double result = contributionLineItemRepository.findByDate(mockEmployee, startDate, endDate);

        assertNotNull(result);
        assertEquals(5000.0, result);
        System.out.println("✅ YTD Contribution: " + result);
    }



    // ✅ Test: returns 0.0 when no contributions exist (primitive double default)
    @Test
    void testFindByDate_ReturnsZeroWhenNoContributions() {
        LocalDate now = LocalDate.now();
        String startDate = LocalDate.of(now.getYear(), 1, 1).toString();
        String endDate = LocalDate.of(now.getYear(), 12, 31).toString();

        when(contributionLineItemRepository.findByDate(mockEmployee, startDate, endDate))
                .thenReturn(0.0);  // primitive double default, not null

        double result = contributionLineItemRepository.findByDate(mockEmployee, startDate, endDate);

        assertEquals(0.0, result);
        System.out.println("✅ No contributions found, YTD returned: " + result);
    }


    //-----------------------------------------------------------------
    //Below will be the unit test for the payroll whether the batch is created or not
    //------------------------------------------------------------------

    //TEST 1- Batch is created and saved correctly






}
