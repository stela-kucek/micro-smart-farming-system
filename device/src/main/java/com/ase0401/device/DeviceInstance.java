package com.ase0401.device;

import com.ase0401.device.actuator.Actuator;
import com.ase0401.device.sensor.Sensor;

public class DeviceInstance {
	private int id;
	private Actuator actuator;
	private Sensor sensor;
	
	public Sensor getSensor() {
		return sensor;
	}
	public void setSensor(Sensor sensor) {
		this.sensor = sensor;
	}
	public DeviceInstance(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Actuator getActuator() {
		return actuator;
	}
	public void setActuator(Actuator actuator) {
		this.actuator = actuator;
	}
	
	public void performAction() {
		System.out.println("Device " + id + " performing an action...");
		actuator.start();
	}
	
	public void performMeasurement() {
		System.out.println("Device " + id + " performing a measurement...");
		sensor.start();
	}
}
