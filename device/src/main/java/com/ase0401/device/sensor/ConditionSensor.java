/**
 * 
 */
package com.ase0401.device.sensor;

import msfs_0401.Condition;
import msfs_0401.Position;

/**
 * @author stela
 *
 */
public class ConditionSensor extends Sensor {
	
	private Condition condition;

	public ConditionSensor(Position position, Condition condition) {
		super(position);
		this.condition = condition;
	}
	
	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}


}
