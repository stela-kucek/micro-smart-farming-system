/**
 * 
 */
package com.ase0401.device.rest.model;

import java.util.Date;

/**
 * @author stela
 *
 */
public class LogModel {
	int id;
	Date timestamp;
	String aspect;
	
	Condition condition;
	
	PlantGrowthCopy plantGrowth;
	
	Action action;
	
	public Action getAction() {
		return action;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	Position position;

	public LogModel () {
		
	}
	
	public PlantGrowthCopy getPlantGrowth() {
		return plantGrowth;
	}

	public void setPlantGrowth(PlantGrowthCopy plantGrowth) {
		this.plantGrowth = plantGrowth;
	}

	
	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public String getAspect() {
		return aspect;
	}

	public void setAspect(String aspect) {
		this.aspect = aspect;
	}

	public Condition getLoggedAspect() {
		return condition;
	}

	public void setLoggedAspect(Condition loggedAspect) {
		this.condition = loggedAspect;
	}
}
