/**
 * 
 */
package com.ase0401.msfsdemo.service;

import java.util.List;

import com.ase0401.msfsdemo.service.model.LogModel;

/**
 * @author stela
 *
 */
public interface DeviceControlService {
	
	public List<LogModel> getSensorData(int deviceId);
	
	public List<LogModel> getLogs(int deviceId);
	
	public List<LogModel> getActuatorData(int deviceId);
	
	public void toggleState(int deviceId, String state);
	
	public void setAction(int deviceId, int actionId, int pos, double value, String unit);
	
	public void setMeasurement(int deviceId, int measureId, int pos);
	
	public void registerDevice(int deviceId);
	
	public void clearSchedule(int deviceId);
	
	public String getMessage(int deviceId);

}
