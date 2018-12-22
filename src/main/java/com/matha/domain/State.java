package com.matha.domain;

import org.apache.commons.lang3.StringUtils;

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
		{
			return true;
		}
		if (obj == null || getClass() != obj.getClass())
		{
			return false;
		}
		State other = (State) obj;
		return StringUtils.equalsIgnoreCase(other.id, this.id);
	}
}
