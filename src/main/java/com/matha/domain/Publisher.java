package com.matha.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.GenericGenerator;

import static com.matha.util.UtilConstants.*;

@Entity
@Table(name = "Publisher")
public class Publisher
{

	@Id
	@Column(name = "Serialid")
	@GenericGenerator(name="pubId" , strategy="increment")
	@GeneratedValue(generator="pubId")	
	private Integer id;

	@Column(name = "Pname")
	private String name;

	@Column(name = "address1")
	private String address1;

	@Column(name = "address2")
	private String address2;

	@Column(name = "address3")
	private String address3;

	@Column(name = "phone1")
	private String phone1;

	@Column(name = "phone2")
	private String phone2;

	@Column(name = "PIN")
	private String pin;

	@Column(name = "Email")
	private String email;

	@Column(name = "GSTIN")
	private String gstIn;

	@Column(name = "logoName")
	private String logoFileName;
	
	public Integer getId()
	{
		return id;
	}

	public void setId(Integer id)
	{
		this.id = id;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getAddress1()
	{
		return address1;
	}

	public void setAddress1(String address1)
	{
		this.address1 = address1;
	}

	public String getAddress2()
	{
		return address2;
	}

	public void setAddress2(String address2)
	{
		this.address2 = address2;
	}

	public String getAddress3()
	{
		return address3;
	}

	public void setAddress3(String address3)
	{
		this.address3 = address3;
	}

	public String getPhone1()
	{
		return phone1;
	}

	public void setPhone1(String phone1)
	{
		this.phone1 = phone1;
	}

	public String getPhone2()
	{
		return phone2;
	}

	public void setPhone2(String phone2)
	{
		this.phone2 = phone2;
	}

	public String getPin()
	{
		return pin;
	}

	public void setPin(String pIN)
	{
		pin = pIN;
	}

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getGstIn()
	{
		return gstIn;
	}

	public void setGstIn(String gstIn)
	{
		this.gstIn = gstIn;
	}

	public String getLogoFileName()
	{
		return logoFileName;
	}

	public void setLogoFileName(String logoFileName)
	{
		this.logoFileName = logoFileName;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("Publisher [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", address1=");
		builder.append(address1);
		builder.append(", address2=");
		builder.append(address2);
		builder.append(", address3=");
		builder.append(address3);
		builder.append(", phone1=");
		builder.append(phone1);
		builder.append(", phone2=");
		builder.append(phone2);
		builder.append(", gstIn=");
		builder.append(gstIn);
		builder.append("]");
		return builder.toString();
	}

	public String getStmtAddress()
	{
		StringBuilder builder = new StringBuilder();		
		builder.append(StringUtils.isBlank(address1) ? "" : address1);
		builder.append(StringUtils.isBlank(address2) ? "" : ", " + address2);
		builder.append(StringUtils.isBlank(address3) ? "" : ", " + address3);
		return builder.toString();
	}

	public String getAddress()
	{
		StringBuilder builder = new StringBuilder();
		builder.append(name);
		builder.append(NEW_LINE);
		if(!StringUtils.isBlank(address1))
		{
			builder.append(address1);
			builder.append(NEW_LINE);
		}		
		if(!StringUtils.isBlank(address2))
		{
		builder.append(address2);
		builder.append(NEW_LINE);
		}
		builder.append("Phone Numbers: ");
		builder.append(StringUtils.isBlank(phone1) ? "" : phone1);
		builder.append(StringUtils.isBlank(phone2) ? "" : "; " + phone2);
		return builder.toString();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Publisher other = (Publisher) obj;
		if (id == null)
		{
			if (other.id != null)
				return false;
		}
		else if (!id.equals(other.id))
			return false;
		return true;
	}

}
