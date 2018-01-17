package com.matha.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CashHead")
public class CashHead {

	@Id
	@Column(name = "Id")
	private String cashHeadName;

	public String getCashHeadName() {
		return cashHeadName;
	}

	public void setCashHeadName(String cashHeadName) {
		this.cashHeadName = cashHeadName;
	}

}
