/**
 * 
 */
package com.ase0401.device.factory;

import java.util.Date;

import org.springframework.stereotype.Component;

import msfs_0401.Action;
import msfs_0401.Amount;
import msfs_0401.Condition;
import msfs_0401.Configuration;
import msfs_0401.Log;
import msfs_0401.LoggedAspect;
import msfs_0401.Msfs_0401Factory;
import msfs_0401.PlantGrowth;
import msfs_0401.Position;
import msfs_0401.SoilAcidity;
import msfs_0401.SoilHumidity;

/**
 * @author stela
 *
 */
@Component
public class LogFactory {

	private static Msfs_0401Factory factory = Msfs_0401Factory.eINSTANCE;

	public Log createLog(int id, String name, LoggedAspect aspect, Position position) {
		Log log = factory.createLog();

		// add all to log
		log.setId(id);
		log.setAspect(name);
		log.setLoggedAspect(aspect);
		log.setTimestamp(new Date());
		log.setPosition(position);

		return log;
	}

	public SoilHumidity createSoilHumidity() {
		return factory.createSoilHumidity();
	}

	public SoilAcidity createSoilAcidity() {
		return factory.createSoilAcidity();
	}
	
	public Position createPosition() {
		return factory.createPosition();
	}
	
	public Condition createCondition() {
		return factory.createCondition();
	}
	
	public PlantGrowth createPlantGrowth() {
		return factory.createPlantGrowth();
	}
	
	public Position createPosition(int nr) {
		Position p = factory.createPosition();
		p.setNumber(nr);
		return p;
	}
	
	public Amount createAmount() {
		return factory.createAmount();
	}
	
	public Configuration createConfiguration() {
		return factory.createConfiguration();
	}
	
	public Action createAction(int type) {
		Action a = factory.createAction();
		
		switch(type) {
		case 0:
			a = factory.createSeeding();
			break;
		case 1:
			a = factory.createReplanting();
			break;
		case 2:
			a = factory.createFertilizing();
			break;
		case 3:
			a = factory.createIrrigating();
			break;
		case 4:
			a = factory.createHarvesting();
		}
		
		return a;
		
	}
}
