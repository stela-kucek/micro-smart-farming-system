/**
 * 
 */
package com.ase0401.msfsdemo.controller.form;

/**
 * @author stela
 *
 */
public class DeviceRegistrationForm {

	String deviceId;
	String name;
	String supplier;
	String site;
	
	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSupplier() {
		return supplier;
	}

	public void setSupplier(String supplier) {
		this.supplier = supplier;
	}

}
