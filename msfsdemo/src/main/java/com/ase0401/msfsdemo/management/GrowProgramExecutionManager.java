package com.ase0401.msfsdemo.management;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ase0401.msfsdemo.service.GrowProgramExecutor;

import msfs_0401.GrowProgram;

@Component
public class GrowProgramExecutionManager {

	Map<String, ArrayList<GrowProgram>> gpMap = new HashMap<String, ArrayList<GrowProgram>>();
	String username;
	ArrayList<GrowProgram> activePrograms = new ArrayList<GrowProgram>();
	@Autowired
	GrowProgramExecutor executor;

	public void setUpForUser(String username) {
		this.username = username;
	}

	public ArrayList<GrowProgram> getActiveGPs() {
		return gpMap.get(username);
	}

	public void activateGP(GrowProgram gp) {
		if (!gpMap.containsKey(username)) {
			gpMap.put(username, new ArrayList<GrowProgram>());
		}
		gpMap.get(username).add(gp);
		System.out.println("Grow program '" + gp.getName() + "' activated!");
		activePrograms = gpMap.get(username);
	}

	public void deactivateGP(GrowProgram gp) {
		gpMap.get(username).remove(gp);
		activePrograms = gpMap.get(username);
		System.out.println("Grow program '" + gp.getName() + "' deactivated!");
	}

	public boolean isActive(GrowProgram gp) {
		if (gpMap.containsKey(username)) {
			if (gpMap.get(username).contains(gp))
				return true;
		}
		return false;
	}

	// @Scheduled(cron = "0 0 6 * * ?") // fires every day at 6 AM

	@Scheduled(fixedDelay = 60000,initialDelay = 5000)
	public void delegateGPs() {
		if (!activePrograms.isEmpty()) {
		//	for (Map.Entry<String, ArrayList<GrowProgram>> entry : gpMap.entrySet()) {
			//	ArrayList<GrowProgram> activeGPs = entry.getValue();
				for (Iterator<GrowProgram> iterator = activePrograms.iterator(); iterator.hasNext();) {
					GrowProgram growProgram = (GrowProgram) iterator.next();
					executor.setUp(growProgram);
					executor.run();
				}
			}

		//}
	}

}
