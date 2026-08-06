package com.Financial_backend.Financial_backend.Repository;


import com.Financial_backend.Financial_backend.Entity.NetworthEntity;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NetworthRepository extends JpaRepository<NetworthEntity, Long> {


    @Query("SELECT n FROM NetworthEntity n WHERE n.employee = :employee")
    Optional<NetworthEntity> findByEmployee(@Param("employee") UsersEntity employee);

}
