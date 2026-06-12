package com.Financial_backend.Financial_backend.Respository;

import com.Financial_backend.Financial_backend.Entity.ContributionLineItemEntity;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContributionLineItemRepository extends JpaRepository<ContributionLineItemEntity, Long> {

//method for finding the employee

    @Query("SELECT e FROM  ContributionLineItemEntity e WHERE e.employee =:employee")
    List<ContributionLineItemEntity> findByEmployee(
            @Param("employee")UsersEntity employee
    );

}
