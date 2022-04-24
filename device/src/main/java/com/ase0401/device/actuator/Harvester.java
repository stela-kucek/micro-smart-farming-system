/**
 * 
 */
package com.ase0401.device.actuator;

import msfs_0401.Harvesting;
import msfs_0401.Position;

/**
 * @author stela
 *
 */
public class Harvester extends Actuator {

	/**
	 * 
	 */
	public Harvester(Position position, Harvesting details) {
		super(position, details);
	}

	@Override
	public void run() {
		System.out.println("Harvesting at position " + position.getNumber());
		try {
			Thread.sleep(2000);
			System.out.println("Still harvesting...");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done!");
	}

}
