package com.Financial_backend.Financial_backend.Respository;


import com.Financial_backend.Financial_backend.Entity.ContributionBatchEntity;
import com.Financial_backend.Financial_backend.Entity.SponsorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContributionBatchRepository extends JpaRepository<ContributionBatchEntity, Long> {

    // find all the sponsor which is of this batch

    @Query("SELECT b FROM ContributionBatchEntity b " +
            "WHERE b.sponsor =:sponsor ORDER BY b.createdAt DESC")
    List<SponsorEntity> findBatchesBySponsor (@Param("sponsor") SponsorEntity sponsor);



}
