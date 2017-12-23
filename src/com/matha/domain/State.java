package com.matha.domain;

import java.util.List;

import org.springframework.data.annotation.Id;

public class State {

	@Id
	private String id;
	private String name;
	private List<String> districts;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getDistricts() {
		return districts;
	}

	public void setDistricts(List<String> districts) {
		this.districts = districts;
	}

	@Override
	public String toString() {
		return name;
	}

}
