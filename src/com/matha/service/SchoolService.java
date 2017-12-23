package com.matha.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matha.domain.School;
import com.matha.domain.State;
import com.matha.repository.SchoolRepository;
import com.matha.repository.StateRepository;

@Service
public class SchoolService {

	@Autowired
	SchoolRepository schoolRepoitory;

	@Autowired
	StateRepository stateRepoitory;

	public List<School> fetchSchoolsLike(String schoolPart) {
		return schoolRepoitory.findByNameLike(schoolPart);
	}

	public School insertSchool(School school) {
		return schoolRepoitory.save(school);
	}

	public List<State> fetchAllStates() {
		return stateRepoitory.findAll();
	}
}
