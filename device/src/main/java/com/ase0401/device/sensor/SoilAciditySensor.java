/**
 * 
 */
package com.ase0401.device.sensor;

import msfs_0401.Position;
import msfs_0401.SoilAcidity;

/**
 * @author stela
 *
 */
public class SoilAciditySensor extends ConditionSensor {
	
	private double value;
	private String unit;

	public SoilAciditySensor(Position position, SoilAcidity condition) {
		super(position, condition);
		value = condition.getSpec().getValue();
		unit = condition.getSpec().getUnit();
	}

	@Override
	public void run() {
		System.out.println("Measuring acidity at position " + position.getNumber());
		try {	
			Thread.sleep(2000);
			System.out.println("Still measuring...");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			System.out.println("Done! Measured acidity: " + value + " " + unit);
	}
	
	

}
