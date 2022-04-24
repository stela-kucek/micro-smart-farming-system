/**
 * 
 */
package com.ase0401.msfsdemo.service;

/**
 * @author stela
 *
 */
public interface DeviceMaintenanceService {
	
	public void updateDeviceFirmware();
	
	public String getDeviceState(int deviceId);
	
	public void initiateReplacementShipping();
}
