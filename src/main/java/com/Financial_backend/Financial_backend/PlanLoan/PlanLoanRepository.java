package com.Financial_backend.Financial_backend.PlanLoan;

import com.Financial_backend.Financial_backend.Entity.SponsorEntity;
import com.Financial_backend.Financial_backend.Entity.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlanLoanRepository extends JpaRepository<PlanLoanEntity, Long> {


    /// custom method for finding the loan status of the loggedIn user amd the user type should be employee
    @Query("SELECT l FROM PlanLoanEntity l WHERE l.user =:users")
    List<PlanLoanEntity> findByUserAndUserType(
            @Param("users")UsersEntity users);



    /// custom method to check if the user exist or not by email
//  Optional <UsersEntity> findByEmail(String email);


  /// custom method to fetch every loan request of their corresponding employee
  @Query("SELECT p FROM PlanLoanEntity p WHERE p.sponsor = :sponsor")
  List<PlanLoanEntity> findLoanRequestOfCorrespondingEmp(@Param("sponsor") SponsorEntity sponsor);



}
