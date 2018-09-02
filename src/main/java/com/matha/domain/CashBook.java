package com.matha.domain;

import com.matha.util.Converters;
import javafx.util.StringConverter;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "CashBook")
public class CashBook {

	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name="TDate")
	private LocalDate txnDate;
	
	private String description;
	
	private Double amount;

	@ManyToOne
	@JoinColumn(name = "type")
	private CashHead type;

	private String mode;

	private String representative;

	public String getTypeVal()
	{
		if(this.type != null)
		{
			return type.getCashHeadName();
		}
		else
		{
			return "None";
		}
	}

	public String getTxnDateStr()
	{
		StringConverter<LocalDate> conv = Converters.getLocalDateConverter();
		return conv.toString(txnDate);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getTxnDate() {
		return txnDate;
	}

	public void setTxnDate(LocalDate tDate) {
		this.txnDate = tDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public CashHead getType() {
		return type;
	}

	public void setType(CashHead type) {
		this.type = type;
	}

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}
}
