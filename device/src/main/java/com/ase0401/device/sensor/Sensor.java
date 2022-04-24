/**
 * 
 */
package com.ase0401.device.sensor;

import msfs_0401.Position;

/**
 * @author stela
 *
 */
public abstract class Sensor extends Thread {
	Position position;
	
	public Sensor(Position position) {
		this.position = position;
	}
	
}
