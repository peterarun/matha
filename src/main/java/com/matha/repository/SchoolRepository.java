package com.matha.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.matha.domain.School;

@Repository
public interface SchoolRepository extends JpaRepository<School, Integer>, SchoolRepositoryCust {

	List<School> findByNameLike(String schoolNamePart);

	List<School> findAllByNameLike(String schoolNamePart);
	
	List<School> findTop10ByNameLike(String schoolNamePart);

}
