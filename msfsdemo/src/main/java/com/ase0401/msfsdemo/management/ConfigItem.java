package com.ase0401.msfsdemo.management;

public class ConfigItem {
	int deviceId;
	String type;
	int pos;
	int measurementId; // 0 humidity, 1 acidity, 2 plantgrowth
	int actionId; // 0 seeding, 1 replanting, 2 fertilizing, 3 irrigating, 4 harvesting
	double value;
	String unit;

	public ConfigItem() {

	}

	public ConfigItem(int deviceId, int pos, int measurementId) {
		this.deviceId = deviceId;
		this.measurementId = measurementId;
		this.pos = pos;
		type = "m";
	}

	public ConfigItem(int deviceId, int pos, int actionId, double value, String unit) {
		this.deviceId = deviceId;
		type = "a";
		this.pos = pos;
		this.actionId = actionId;
		this.value = value;
		this.unit = unit;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public int getActionId() {
		return actionId;
	}

	public void setActionId(int actionId) {
		this.actionId = actionId;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getMeasurementId() {
		return measurementId;
	}

	public void setMeasurementId(int measurementId) {
		this.measurementId = measurementId;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}
}
