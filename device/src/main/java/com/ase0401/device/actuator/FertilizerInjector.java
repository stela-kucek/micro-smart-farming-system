package com.ase0401.device.actuator;

import msfs_0401.Fertilizing;
import msfs_0401.Position;

/**
 * @author stela
 *
 */
public class FertilizerInjector extends Actuator {
	private String fertilizer;
	private double amount;
	private String unit;

	/**
	 * 
	 */
	public FertilizerInjector(Position position, Fertilizing details) {
		super(position, details);
		amount = details.getSpec().getValue();
		unit = details.getSpec().getUnit();
		fertilizer = details.getFertilizer();
	}

	@Override
	public void run() {
		System.out.println("Applying  fertilizer '" + fertilizer + "' at position " + position.getNumber()
				+ ", amount: " + amount + " " + unit);
		try {
			Thread.sleep(2000);
			System.out.println("Still fertilizing...");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Done!");

	}

}
