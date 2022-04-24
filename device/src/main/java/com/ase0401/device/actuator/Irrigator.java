/**
 * 
 */
package com.ase0401.device.actuator;

import msfs_0401.Irrigating;
import msfs_0401.Position;

/**
 * @author stela
 *
 */
public class Irrigator extends Actuator {
	private double amount;
	private String unit;

	/**
	 * 
	 */
	public Irrigator(Position position, Irrigating details) {
		super(position, details);
		amount = details.getSpec().getValue();
		unit = details.getSpec().getUnit();
	}

	@Override
	public void run() {
		System.out.println("Irrigating at position " + position.getNumber()
				+ ", amount: " + amount + " " + unit);
		try {
			Thread.sleep(2000);
			System.out.println("Still irrigating...");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done!");

	}

}
