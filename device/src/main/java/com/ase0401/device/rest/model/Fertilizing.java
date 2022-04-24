package com.ase0401.device.rest.model;

public class Fertilizing extends Action {
	String fertilizer;
	Amount period;
	int reps;
	public String getFertilizer() {
		return fertilizer;
	}
	public void setFertilizer(String fertilizer) {
		this.fertilizer = fertilizer;
	}
	public Amount getPeriod() {
		return period;
	}
	public void setPeriod(Amount period) {
		this.period = period;
	}
	public int getReps() {
		return reps;
	}
	public void setReps(int reps) {
		this.reps = reps;
	}
	
}
