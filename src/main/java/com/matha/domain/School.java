package com.matha.domain;

import static com.matha.util.UtilConstants.EMPTY_STR;
import static com.matha.util.UtilConstants.NEW_LINE;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "Customer")
public class School implements Serializable {

	private static final long serialVersionUID = -5417690910985870568L;

	@Id
	@Column(name = "CustId")
	@GenericGenerator(name = "kaugen", strategy = "increment")
	@GeneratedValue(generator = "kaugen")
	private Integer id;

	@Column(name = "CName")
	private String name;

	@Column(name = "Address1")
	private String address1;

	@Column(name = "Address2")
	private String address2;

	@Column(name = "Address3")
	private String address3;

	@Column(name = "Add4")
	private String city;
	
	@Column(name = "Add5")
	private String pin;

	@OneToOne
	@JoinColumn(name = "state")
	private State state;

	@OneToOne
	@JoinColumn(name = "district")
	private District district;

	@Column(name = "PhNo1")
	private String phone1;

	@Column(name = "PhNo2")
	private String phone2;
	
	@Column(name = "OutStanding")
	private Double outstanding;	

	private String principal;
	private String email;

	public String getDistrictStr()
	{
		if(this.district == null)
		{
			return EMPTY_STR;
		}
		else
		{
			return this.district.getId();
		}
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public District getDistrict() {
		return district;
	}

	public void setDistrict(District district) {
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
	
	public Double getOutstanding()
	{
		return outstanding;
	}

	public void setOutstanding(Double outstanding)
	{
		this.outstanding = outstanding;
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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("School [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", address1=");
		builder.append(address1);
		builder.append(", address2=");
		builder.append(address2);
		builder.append(", address3=");
		builder.append(address3);
		builder.append(", city=");
		builder.append(city);
		builder.append(", pin=");
		builder.append(pin);
		builder.append(", state=");
		builder.append(state);
		builder.append(", district=");
		builder.append(district);
		builder.append(", phone1=");
		builder.append(phone1);
		builder.append(", phone2=");
		builder.append(phone2);
		builder.append(", principal=");
		builder.append(principal);
		builder.append(", email=");
		builder.append(email);
		builder.append("]");
		return builder.toString();
	}

	public String addressText() {
		StringBuilder builder = new StringBuilder();
		if (isNotBlank(address1)) {
			builder.append(address1);
			builder.append(NEW_LINE);
		}
		if (isNotBlank(address2)) {
			builder.append(address2);
			builder.append(NEW_LINE);
		}
		if (isNotBlank(city)) {
			builder.append(city);
			builder.append(NEW_LINE);
		}
		if (isNotBlank(pin)) {
			builder.append(pin);
			builder.append(NEW_LINE);
		}
		if (district != null && isNotBlank(district.getId())) {
			builder.append(district.getId());
			builder.append(NEW_LINE);
		}
		if (state != null && isNotBlank(state.getId())) {
			builder.append(state.getId());
		}

		return builder.toString();
	}

	public String fetchDetails()
	{
		StringBuilder builder = new StringBuilder(name);
		builder.append(NEW_LINE);
		builder.append(addressText());
		return builder.toString();
	}

	public String getStmtAddress()
	{
		StringBuilder builder = new StringBuilder();		
		if (address2 != null) {
			builder.append(address2);
			builder.append(NEW_LINE);
		}
		if (city != null) {
			builder.append(city);
			builder.append(NEW_LINE);
		}
		if (pin != null) {
			builder.append(pin);
			builder.append(NEW_LINE);
		}
		if (state != null) {
			builder.append(state.getId());
			builder.append(NEW_LINE);
		}
		if (district != null) {
			builder.append(district.getId());
		}
		return builder.toString();
	}

	public String toBasicString() {
		StringBuilder builder = new StringBuilder();
		builder.append("School [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}

}
