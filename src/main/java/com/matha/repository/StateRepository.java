package com.matha.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matha.domain.State;

@Repository
public interface StateRepository extends JpaRepository<State, String> {

}
