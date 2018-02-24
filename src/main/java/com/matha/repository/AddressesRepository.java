package com.matha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matha.domain.Address;

@Repository
public interface AddressesRepository extends JpaRepository<Address, String> {

}