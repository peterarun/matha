package com.matha.domain;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "State")
public class State implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -7719487288146043933L;

	@Id
	@Column(name = "name")
	private String id;

	@OneToMany(mappedBy = "state", fetch = FetchType.EAGER)
	private Set<District> districts;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Set<District> getDistricts()
	{
		return districts;
	}

	public void setDistricts(Set<District> districts)
	{
		this.districts = districts;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("State [id=");
		builder.append(id);
		builder.append("]");
		return builder.toString();
	}

}
