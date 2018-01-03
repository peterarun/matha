package com.matha.domain;

import org.springframework.data.annotation.Id;

public class Publisher {

	@Id
	private String id;
	private String name;
	private String address1;
	private String address2;
	private String phone1;
	private String phone2;

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

	public String getAddress1() {
		return address1;
	}

	public void setAddress1(String address1) {
		this.address1 = address1;
	}

	public String getAddress2() {
		return address2;
	}

	public void setAddress2(String address2) {
		this.address2 = address2;
	}

	public String getPhone1() {
		return phone1;
	}

	public void setPhone1(String phone1) {
		this.phone1 = phone1;
	}

	public String getPhone2() {
		return phone2;
	}

	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Publisher [name=");
		builder.append(name);
		builder.append(", address1=");
		builder.append(address1);
		builder.append("]");
		return builder.toString();
	}

}
