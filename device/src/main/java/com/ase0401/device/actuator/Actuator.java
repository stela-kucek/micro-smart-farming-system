/**
 * 
 */
package com.ase0401.device.actuator;

import msfs_0401.Action;
import msfs_0401.Position;

/**
 * @author stela
 *
 */
public abstract class Actuator extends Thread {
	Action action;
	Position position;

	public Actuator(Position position, Action action) {
		this.position = position;
		this.action = action;
	}

	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
}
