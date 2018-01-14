package com.matha.domain;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import com.matha.util.UtilConstants;

@Entity
@Table(name = "BookPurchase")
public class Purchase implements Serializable {

	private static final long serialVersionUID = -5481240318711096172L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne()
	@JoinColumn(name = "publisher")
	private Publisher publisher;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "purchase")
	@Cascade({CascadeType.SAVE_UPDATE, CascadeType.PERSIST, CascadeType.MERGE})
	private Set<OrderItem> orderItem;

	private LocalDate purchaseDate;

	private LocalDate deliveryDate;

	private Double subTotal;

	private String discountType;

	private Double discount;

	private String grNo;

	private String invoiceNo;

	private Double paidAmount;

	private String notes;

	public Double getNetAmount() {

		Double value = null;
		if (subTotal != null) {
			if (this.discount != null) {
				value = UtilConstants.discTypes[0].equals(this.discountType) ? this.subTotal - this.discount
						: this.subTotal - this.subTotal * this.discount / 100;
			} else {
				value = this.subTotal;
			}
		}

		return value;
	}

	public Double getAmountToPay() {
		if (this.getNetAmount() == null) {
			return null;
		}
		return this.getNetAmount() - (this.paidAmount == null ? 0 : this.paidAmount);
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Publisher getPublisher() {
		return publisher;
	}

	public void setPublisher(Publisher publisher) {
		this.publisher = publisher;
	}

	public Set<OrderItem> getOrderItem() {
		return orderItem;
	}

	public void setOrderItem(Set<OrderItem> orderItem) {
		this.orderItem = orderItem;
	}

	public LocalDate getPurchaseDate() {
		return purchaseDate;
	}

	public void setPurchaseDate(LocalDate purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	public LocalDate getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(LocalDate deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Double getSubTotal() {
		return subTotal;
	}

	public void setSubTotal(Double subTotal) {
		this.subTotal = subTotal;
	}

	public String getDiscountType() {
		return discountType;
	}

	public void setDiscountType(String discountType) {
		this.discountType = discountType;
	}

	public Double getDiscount() {
		return discount;
	}

	public void setDiscount(Double discount) {
		this.discount = discount;
	}

	public String getGrNo() {
		return grNo;
	}

	public void setGrNo(String grNo) {
		this.grNo = grNo;
	}

	public String getInvoiceNo() {
		return invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	public Double getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(Double paidAmount) {
		this.paidAmount = paidAmount;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Purchase [id=");
		builder.append(id);
		builder.append(", publisher=");
		builder.append(publisher);
		builder.append(", purchaseDate=");
		builder.append(purchaseDate);
		builder.append(", deliveryDate=");
		builder.append(deliveryDate);
		builder.append(", subTotal=");
		builder.append(subTotal);
		builder.append(", discountType=");
		builder.append(discountType);
		builder.append(", discount=");
		builder.append(discount);
		builder.append(", grNo=");
		builder.append(grNo);
		builder.append(", invoiceNo=");
		builder.append(invoiceNo);
		builder.append(", paidAmount=");
		builder.append(paidAmount);
		builder.append(", notes=");
		builder.append(notes);
		builder.append("]");
		return builder.toString();
	}

}
