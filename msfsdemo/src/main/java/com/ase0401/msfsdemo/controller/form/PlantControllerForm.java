/**
 * 
 */
package com.ase0401.msfsdemo.controller.form;

/**
 * @author azar
 *
 */

public class PlantControllerForm {
	String id;
	double price;
	boolean forSale;
	int quantity;
	
	public double getPrice() {
		return price;
	}
	public void setPrice(double price) {
		this.price = price;
	}
	public boolean isForSale() {
		return forSale;
	}
	public void setForSale(boolean forSale) {
		this.forSale = forSale;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
}
