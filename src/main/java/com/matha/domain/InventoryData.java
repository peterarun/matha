package com.matha.domain;

public interface InventoryData
{
	Integer getQuantity();

	Book getBook();

	boolean isUnFilled();
}
