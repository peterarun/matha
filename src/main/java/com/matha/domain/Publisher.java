package com.matha.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Publisher")
public class Publisher {

	@Id
	@Column(name = "Serialid")
	private String id;

	@Column(name = "Pname")
	private String name;

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

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Publisher [name=");
		builder.append(name);
		builder.append("]");
		return builder.toString();
	}

}
