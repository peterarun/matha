package com.matha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matha.domain.District;

@Repository
public interface DistrictRepository extends JpaRepository<District, String> {

}
