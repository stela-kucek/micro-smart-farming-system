package com.ase0401.msfsdemo.service;

public interface GrowProgramExecutionService extends Runnable {

	public void filterGrowProgramInstructions();
	
	public void executeOnDevices() throws InterruptedException;
}
