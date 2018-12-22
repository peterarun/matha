package com.matha.domain;

import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Districts")
public class District implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6647801337701991704L;

	@Id
	@Column(name = "name")
	private String id;

	@ManyToOne
	@JoinColumn(name = "state")
	private State state;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the state
	 */
	public State getState() {
		return state;
	}

	/**
	 * @param state
	 *            the state to set
	 */
	public void setState(State state) {
		this.state = state;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
		{
			return true;
		}
		if (o == null || getClass() != o.getClass())
		{
			return false;
		}
		District district = (District) o;
		return StringUtils.equalsIgnoreCase(id, district.id) &&
				Objects.equals(state, district.state);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(id, state);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		builder.append("District [id=");
		builder.append(id);
		builder.append(", state=");
		builder.append(state);
		builder.append("]");
		return builder.toString();
	}
}
