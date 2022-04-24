/**
 * 
 */
package com.ase0401.device.actuator;

import msfs_0401.Position;
import msfs_0401.Seeding;

/**
 * @author stela
 *
 */
public class SeedInjector extends Actuator {

	private double depth;
	
	private String unit;
	

	/**
	 * 
	 */
	public SeedInjector(Position position, Seeding details) {
		super(position, details);
		this.depth = details.getSpec().getValue();
		this.unit = details.getSpec().getUnit();
	}

	@Override
	public void run() {
		System.out.println("Seeding at position " + position.getNumber() + " at a depth of "
				+ depth + " " + unit);
		try {	
			Thread.sleep(2000);
			System.out.println("Still seeding...");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			System.out.println("Done!");
	}

}
