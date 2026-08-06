package com.Financial_backend.Financial_backend.Repository;

import com.Financial_backend.Financial_backend.Entity.ContributionLineItemEntity;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContributionLineItemRepository extends JpaRepository<ContributionLineItemEntity, Long> {

//method for finding the employee

    @Query("SELECT e FROM  ContributionLineItemEntity e WHERE e.employee =:employee")
    List<ContributionLineItemEntity> findByEmployee(
            @Param("employee")UsersEntity employee
    );


    //method for calculating YTDContribution


    @Query(value = "SELECT SUM(c.employee_amount) FROM contribution_line_item c " +
            "WHERE c.employee_id = CAST(:employeeId AS bigint) " +
            "AND TO_DATE(c.pay_date, 'DD/MM/YYYY') BETWEEN CAST(:startDate AS date) AND CAST(:endDate AS date)",
            nativeQuery = true)
    Double findByDate(@Param("employeeId") Long employeeId,
                      @Param("startDate") String startDate,
                      @Param("endDate") String endDate);

}
