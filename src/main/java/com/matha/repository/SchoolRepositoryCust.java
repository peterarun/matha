package com.matha.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.matha.domain.District;
import com.matha.domain.School;
import com.matha.domain.State;

@Repository
public interface SchoolRepositoryCust {

	public List<School> fetchSchools(State state, District district, String name, String city, String pin);

	public List<School> fetchSchools(int startId);
}
