package com.gendev.streamcombinations.repository;

import com.gendev.streamcombinations.model.main.StreamingPackage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StreamingPackageRepository extends JpaRepository<StreamingPackage, Long> {
}