/**
 * 
 */
package com.ase0401.msfsdemo.controller.form;

/**
 * @author stela
 *
 */
public class DeviceActionForm {
	
	private String chosenDevice;
	
	private String chosenAction;
	
	private String position;
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getFertilizer() {
		return fertilizer;
	}

	public void setFertilizer(String fertilizer) {
		this.fertilizer = fertilizer;
	}

	private String value;
	
	private String unit;
	
	private String fertilizer;

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getChosenDevice() {
		return chosenDevice;
	}

	public void setChosenDevice(String chosenDevice) {
		this.chosenDevice = chosenDevice;
	}

	public String getChosenAction() {
		return chosenAction;
	}

	public void setChosenAction(String chosenAction) {
		this.chosenAction = chosenAction;
	}

}
