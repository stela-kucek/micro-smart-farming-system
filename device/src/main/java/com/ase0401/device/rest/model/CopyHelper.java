/**
 * 
 */
package com.ase0401.device.rest.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import msfs_0401.Log;
import msfs_0401.PlantGrowth;
import msfs_0401.SoilHumidity;

/**
 * @author stela
 *
 */
public class CopyHelper {
	
	public static List<LogModel> copyEClassToPOJO (List<Log> logs) {
		List<LogModel> list = new ArrayList<>();
		
		for (Iterator<Log> iterator = logs.iterator(); iterator.hasNext();) {
			Log log = (Log) iterator.next();
			LogModel copy = new LogModel();
			copy.setId(log.getId());
			copy.setAspect(log.getAspect());
			copy.setPosition(new Position());
			copy.getPosition().setNumber(log.getPosition().getNumber());
			copy.setTimestamp(log.getTimestamp());
			
			if(log.getLoggedAspect() instanceof msfs_0401.Condition) {
				msfs_0401.Condition shObj = (msfs_0401.Condition) log.getLoggedAspect();
				Condition cond = new Condition();
				if(shObj instanceof SoilHumidity) {
					cond = new SoilHumidityCopy();
				}
				else
					cond = new SoilAcidityCopy();
				
				cond.setSpec(new Amount());
				cond.getSpec().setValue(shObj.getSpec().getValue());
				cond.getSpec().setUnit(shObj.getSpec().getUnit());
				copy.setLoggedAspect(cond);
			}
			else if(log.getLoggedAspect() instanceof msfs_0401.Action) {
				msfs_0401.Action action = (msfs_0401.Action) log.getLoggedAspect();
				
				if(action instanceof msfs_0401.Irrigating) {
					Irrigating act = new Irrigating();
					act.setSpec(new Amount());
					act.getSpec().setValue(action.getSpec().getValue());
					act.getSpec().setUnit(action.getSpec().getUnit());
					copy.setAction(act);
				}
				else if(action instanceof msfs_0401.Fertilizing) {
					Fertilizing act = new Fertilizing();
					act.setSpec(new Amount());
					act.getSpec().setValue(action.getSpec().getValue());
					act.getSpec().setUnit(action.getSpec().getUnit());
					act.setFertilizer(((msfs_0401.Fertilizing) action).getFertilizer());
					copy.setAction(act);
				}
				else if(action instanceof msfs_0401.Seeding) {
					Seeding act = new Seeding();
					act.setSpec(new Amount());
					act.getSpec().setValue(action.getSpec().getValue());
					act.getSpec().setUnit(action.getSpec().getUnit());
					copy.setAction(act);
				}
				else if(action instanceof msfs_0401.Replanting) {
					Replanting act = new Replanting();
					act.setSpec(new Amount());
					act.getSpec().setValue(action.getSpec().getValue());
					act.getSpec().setUnit(action.getSpec().getUnit());
					copy.setAction(act);
				}
				
				else if(action instanceof msfs_0401.Harvesting) {
					Harvesting act = new Harvesting();
					switch(((msfs_0401.Harvesting) action).getMode().getName()) {
					case "onClick":
						act.setMode(com.ase0401.device.rest.model.HarvestMode.onClick);
						break;
					case "manual":
						act.setMode(com.ase0401.device.rest.model.HarvestMode.manual);
						break;
					case "auto":
						act.setMode(com.ase0401.device.rest.model.HarvestMode.auto);
						break;
					}
					act.setSpec(new Amount());
					act.getSpec().setValue(action.getSpec().getValue());
					act.getSpec().setUnit(action.getSpec().getUnit());
					copy.setAction(act);
				}

			}
			else if (log.getLoggedAspect() instanceof PlantGrowth) {
				PlantGrowth obj = (PlantGrowth) log.getLoggedAspect();
				PlantGrowthCopy pg = new PlantGrowthCopy();
				pg.setStage(obj.getStage());
				copy.setPlantGrowth(pg);
			}
			
			list.add(copy);
		}
		
		return list;
	}
}
