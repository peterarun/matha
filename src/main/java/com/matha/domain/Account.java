package com.matha.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BankAccounts")
public class Account
{

	@Id
	@Column(name = "Name")
	private String name;

	@Column(name = "AccNum")
	private String accountNum;

	@Column(name = "IFSC")
	private String ifsc;

	@Column(name = "address")
	private String address;

	@Column(name = "phone1")
	private String phone1;

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAccountNum()
	{
		return accountNum;
	}

	public void setAccountNum(String accountNum)
	{
		this.accountNum = accountNum;
	}

	public String getIfsc()
	{
		return ifsc;
	}

	public void setIfsc(String ifsc)
	{
		this.ifsc = ifsc;
	}

	public String getAddress()
	{
		return address;
	}

	public void setAddress(String address)
	{
		this.address = address;
	}

	public String getPhone1()
	{
		return phone1;
	}

	public void setPhone1(String phone1)
	{
		this.phone1 = phone1;
	}

}
