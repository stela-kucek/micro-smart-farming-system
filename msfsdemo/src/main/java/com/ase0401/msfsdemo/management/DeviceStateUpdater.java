/**
 * 
 */
package com.ase0401.msfsdemo.management;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

import msfs_0401.DeviceState;
import msfs_0401.FarmingDevice;

/**
 * @author stela
 *
 */
public class DeviceStateUpdater {

	Date timeNow = null;

	public DeviceStateUpdater() {
		timeNow = new Date();
	}

	public FarmingDevice updateDeviceState(FarmingDevice device) {

		if (device.isActive()) {
			System.out.println("Updating state of device " + device.getId() + "...");
			double batteryLevel = device.getMaintenanceData().getBatteryLevel();
			if (batteryLevel > 0.1) {
				long hrsNow = device.getMaintenanceData().getOperationHours();
				device.getMaintenanceData().setOperationHours(hrsNow + 1);
				double newBatteryLevel = round(batteryLevel - 0.1, 2);
				device.getMaintenanceData().setBatteryLevel(newBatteryLevel);
			}
			else {
				device.getMaintenanceData().setBatteryLevel(0.0);
				device.getMaintenanceData().setState(DeviceState.BATTERY_EMPTY);
				device.setActive(false);
			}
		}

		return device;

	}

	private static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
