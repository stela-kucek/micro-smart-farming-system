/**
 * 
 */
package com.ase0401.device.sensor;

import com.ase0401.device.rest.EventProducer;

import msfs_0401.LifeCycleStage;
import msfs_0401.PlantGrowth;
import msfs_0401.Position;

/**
 * @author stela
 *
 */
public class PlantGrowthSensor extends Sensor {
	
	private LifeCycleStage stage;
	private int deviceId;

	public PlantGrowthSensor(int deviceId, Position position, PlantGrowth condition) {
		super(position);
		stage = condition.getStage();
		this.deviceId = deviceId;
	}

	@Override
	public void run() {
		System.out.println("Detecting plant stage at position " + position.getNumber());
		try {	
			Thread.sleep(2000);
			System.out.println("Still detecting...");
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
			System.out.println("Done! Plant's stage: " + stage.getName());
			if(stage.equals(LifeCycleStage.FRUIT)) {
				EventProducer.publishNotificationToHarvest(deviceId, position.getNumber());
			}
		
	}
	
	

}
