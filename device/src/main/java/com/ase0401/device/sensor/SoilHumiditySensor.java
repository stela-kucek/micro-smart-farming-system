/**
 * 
 */
package com.ase0401.device.sensor;

import msfs_0401.Position;
import msfs_0401.SoilHumidity;

/**
 * @author stela
 *
 */
public class SoilHumiditySensor extends ConditionSensor {
	
	private double value;
	private String unit;

	public SoilHumiditySensor(Position position, SoilHumidity condition) {
		super(position, condition);
		value = condition.getSpec().getValue();
		unit = condition.getSpec().getUnit();
	}

	@Override
	public void run() {
		System.out.println("Measuring humidity at position " + position.getNumber());
		try {	
			Thread.sleep(2000);
			System.out.println("Still measuring...");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			System.out.println("Done! Measured humidity: " + value + " " + unit);
	}
	
	

}
