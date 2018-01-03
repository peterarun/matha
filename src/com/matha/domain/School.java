package com.matha.domain;

import java.util.Calendar;
import java.util.Date;

import org.springframework.data.annotation.Id;


public class School {

	public static final String NEW_LINE = System.getProperty("line.separator");

	@Id
	private String id;
	private String name;
	private String address1;
	private String address2;
	private String address3;
	private String city;
	private String pin;
	private String state;
	private String district;
	private String phone1;
	private String phone2;
	private String principal;
	private String email;
	private Date insertTime;
	private Date updateTime;

	public School(String id, String name, String address1, String address2, String address3, String city, String pin,
			String state, String district, String phone1, String phone2, String principal, String email) {
		super();
		this.id = id;
		this.name = name;
		this.address1 = address1;
		this.address2 = address2;
		this.address3 = address3;
		this.city = city;
		this.pin = pin;
		this.state = state;
		this.district = district;
		this.phone1 = phone1;
		this.phone2 = phone2;
		this.principal = principal;
		this.email = email;
		Date date = Calendar.getInstance().getTime();
		this.insertTime = date;
		this.updateTime = date;
	}

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

	public String getAddress3() {
		return address3;
	}

	public void setAddress3(String address3) {
		this.address3 = address3;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
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

	public String getPrincipal() {
		return principal;
	}

	public void setPrincipal(String principal) {
		this.principal = principal;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getInsertTime() {
		return insertTime;
	}

	public void setInsertTime(Date insertTime) {
		this.insertTime = insertTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
    
	public String addressText() {
		StringBuilder builder = new StringBuilder();
		builder.append(address1);
		builder.append(NEW_LINE);
		builder.append(address2);
		builder.append(NEW_LINE);
		builder.append(address3);
		builder.append(NEW_LINE);
		builder.append(city);
		builder.append(", PIN: ");
		builder.append(pin);
		builder.append(NEW_LINE);
		builder.append(district);
		builder.append(NEW_LINE);
		builder.append(state);
		return builder.toString();
	}

	public String fullAddress() {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append(NEW_LINE);
		builder.append(address1);
		builder.append(NEW_LINE);
		builder.append(address2);
		builder.append(NEW_LINE);
		builder.append(address3);
		builder.append(NEW_LINE);
		builder.append(city);
		builder.append(NEW_LINE);
		builder.append(pin);
		builder.append(NEW_LINE);
		builder.append(state);
		builder.append(NEW_LINE);
		builder.append(district);
		builder.append(NEW_LINE);
		builder.append(phone1);
		return builder.toString();
	}

	public String shortAddress() {
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append(NEW_LINE);
		builder.append(address1);
		return builder.toString();
	}

}
