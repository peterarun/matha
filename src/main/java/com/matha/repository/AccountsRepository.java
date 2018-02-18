package com.matha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matha.domain.Account;

@Repository
public interface AccountsRepository extends JpaRepository<Account, String> {

}
