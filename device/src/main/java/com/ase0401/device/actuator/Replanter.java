/**
 * 
 */
package com.ase0401.device.actuator;

import msfs_0401.Position;
import msfs_0401.Replanting;

/**
 * @author stela
 *
 */
public class Replanter extends Actuator {
	
	private double depth = 20.0;
	
	private String unit = "mm";

	/**
	 * 
	 */
	public Replanter(Position position, Replanting details) {
		super(position, details);
		this.depth = details.getSpec().getValue();
		this.unit = details.getSpec().getUnit();
	}

	@Override
	public void run() {
		System.out.println("Replanting at position " + position.getNumber() + " at a depth of "
				+ depth + " " + unit);
		try {	
			Thread.sleep(2000);
			System.out.println("Still replanting...");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			System.out.println("Done!");
		
	}

}
