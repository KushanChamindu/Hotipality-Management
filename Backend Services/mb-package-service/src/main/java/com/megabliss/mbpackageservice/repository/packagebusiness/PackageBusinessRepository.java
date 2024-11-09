package com.megabliss.mbpackageservice.repository.packagebusiness;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.megabliss.mbpackageservice.model.packagebusiness.PackageBusiness;

@Repository
public interface PackageBusinessRepository extends JpaRepository<PackageBusiness, Long> {
    Optional<PackageBusiness> findByServiceProviderId(String serviceProviderId);

    void deleteByServiceProviderId(String serviceProviderId);
}