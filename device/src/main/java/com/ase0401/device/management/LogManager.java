/**
 * 
 */
package com.ase0401.device.management;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ase0401.device.DeviceInstance;
import com.ase0401.device.actuator.Actuator;
import com.ase0401.device.actuator.FertilizerInjector;
import com.ase0401.device.actuator.Harvester;
import com.ase0401.device.actuator.Irrigator;
import com.ase0401.device.actuator.Replanter;
import com.ase0401.device.actuator.SeedInjector;
import com.ase0401.device.factory.LogFactory;
import com.ase0401.device.repository.LogRepository;
import com.ase0401.device.sensor.PlantGrowthSensor;
import com.ase0401.device.sensor.Sensor;
import com.ase0401.device.sensor.SoilAciditySensor;
import com.ase0401.device.sensor.SoilHumiditySensor;

import msfs_0401.Action;
import msfs_0401.Amount;
import msfs_0401.Condition;
import msfs_0401.Configuration;
import msfs_0401.Fertilizing;
import msfs_0401.Harvesting;
import msfs_0401.Irrigating;
import msfs_0401.LifeCycleStage;
import msfs_0401.Log;
import msfs_0401.LoggedAspect;
import msfs_0401.PlantGrowth;
import msfs_0401.Position;
import msfs_0401.Replanting;
import msfs_0401.Seeding;
import msfs_0401.SoilAcidity;
import msfs_0401.SoilHumidity;

/**
 * @author stela
 *
 */
@Component
public class LogManager {

	@Autowired
	private LogFactory factory;

	@Autowired
	private LogRepository repository;
	
	private Actuator actuator;
	
	private Sensor sensor;

	private ArrayList<Integer> activeDevices;
	
	private Map<Integer, List<Configuration>> deviceTodos = new HashMap<Integer, List<Configuration>>();

	private  ArrayList<DeviceInstance> devices;
	
	private AtomicBoolean enabled = new AtomicBoolean();

	private int id;

	public void setUpManager(int id, ArrayList<Integer> activeDevices,  Map<Integer, List<Configuration>> todos, ArrayList<DeviceInstance> devices) {
		this.id = id;
		repository.setUpRepository(id);
		this.activeDevices = new ArrayList<>();
		for (Iterator<Integer> iterator = activeDevices.iterator(); iterator.hasNext();) {
			Integer integer = (Integer) iterator.next();
			this.activeDevices.add(integer);
		}
		deviceTodos = todos;
		this.devices = devices;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void logData(String name, LoggedAspect aspect, Position position) {
		int newId = repository.getNextId();

		while (idExists(newId)) {
			newId += 1;
		}

		repository.addLog(factory.createLog(newId, name, aspect, position));
	}

	public boolean idExists(int id) {
		ArrayList<Log> logs = repository.getLogs();
		for (Iterator<Log> iterator = logs.iterator(); iterator.hasNext();) {
			Log log = (Log) iterator.next();
			if (log.getId() == id)
				return true;
		}
		return false;
	}

	public ArrayList<Log> getLogsFromDevice() {
		return repository.getLogs();
	}
	

	public ArrayList<Log> getSensorData() {
		ArrayList<Log> all = repository.getLogs();
		ArrayList<Log> sensorData = new ArrayList<Log>();
		for (Iterator<Log> iterator = all.iterator(); iterator.hasNext();) {
			Log log = (Log) iterator.next();
			if (log.getLoggedAspect() instanceof Condition || log.getLoggedAspect() instanceof PlantGrowth) {
				sensorData.add(log);
			}
		}

		return sensorData;
	}

	public ArrayList<Log> getActuatorData() {
		ArrayList<Log> all = repository.getLogs();
		ArrayList<Log> sensorData = new ArrayList<Log>();
		for (Iterator<Log> iterator = all.iterator(); iterator.hasNext();) {
			Log log = (Log) iterator.next();
			if (log.getLoggedAspect() instanceof Action) {
				sensorData.add(log);
			}
		}

		return sensorData;
	}
	
	public Configuration createNewConfiguration(int actionId, int nr, double value, String unit) {
		Configuration config = factory.createConfiguration();
		Action action = factory.createAction(actionId);
		Amount amt = factory.createAmount();
		amt.setValue(value);
		amt.setUnit(unit);
		action.setSpec(amt);
		config.setCommand(action);
		Position pos = factory.createPosition(nr);
		config.setPosition(pos);
		return config;
	}
	
	public Configuration createNewMConfiguration(int what, int pos) {
		Configuration config = factory.createConfiguration();
		if(what == 0) {
			Amount spec = factory.createAmount();
			double value = randomDouble(1.0, 99.0);
			spec.setValue(value);
			spec.setUnit("%");
			Condition cond = factory.createSoilHumidity();
			cond.setSpec(spec);
			config.setCommand(cond);
		}
		if(what == 1) {
			double value = randomDouble(3.5, 9.5);
			Amount spec = factory.createAmount();
			spec.setValue(value);
			spec.setUnit("pH");
			Condition cond = factory.createSoilAcidity();
			cond.setSpec(spec);
			config.setCommand(cond);
		}
		
		if(what == 2) {
			PlantGrowth pg = factory.createPlantGrowth();
			pg.setStage(randomStage());
			config.setCommand(pg);
		}

		Position position = factory.createPosition(pos);
		config.setPosition(position);
		return config;
	}

	@Scheduled(fixedRate = 5000)
	public void logSensorData() {
		if (activeDevices != null && !activeDevices.isEmpty()) {
			//if (enabled.get()) {
				for (Iterator<Integer> iterator = activeDevices.iterator(); iterator.hasNext();) {
					Integer integer = (Integer) iterator.next();
					if(deviceTodos.get(integer) != null && !deviceTodos.get(integer).isEmpty() ) {
						repository.setUpRepository(integer);
						Configuration config = deviceTodos.get(integer).get(0);
						DeviceInstance device = getDeviceById(integer);
						if(config.getCommand() instanceof Action) {
							actuator = selectSuitableActuator(config);
							device.setActuator(actuator);
							device.performAction();
							logData(getNameOfAction(actuator), config.getCommand(), config.getPosition());
						}
						else {
							sensor = selectSuitableSensor(integer, config);
							device.setSensor(sensor);
							device.performMeasurement();
							logData(getNameOfMeasurement(sensor), config.getCommand(), config.getPosition());
						}
						
						deviceTodos.get(integer).remove(0);
					}
				}
		//	}
		}
	}
	
	private Actuator selectSuitableActuator(Configuration config) {
		Action a = (Action) config.getCommand();
		Position p = config.getPosition();

		if (a instanceof Seeding)
			return new SeedInjector(p, (Seeding) a);
		if (a instanceof Fertilizing)
			return new FertilizerInjector(p, (Fertilizing) a);
		if (a instanceof Irrigating)
			return new Irrigator(p, (Irrigating) a);
		if (a instanceof Harvesting)
			return new Harvester(p, (Harvesting) a);
		if (a instanceof Replanting)
			return new Replanter(p, (Replanting) a);

		return null;
	}
	
	private Sensor selectSuitableSensor(int id, Configuration config) {
		LoggedAspect a = config.getCommand();
		Position p = config.getPosition();

		if (a instanceof SoilHumidity)
			return new SoilHumiditySensor(p, (SoilHumidity) a);
		if (a instanceof SoilAcidity)
			return new SoilAciditySensor(p, (SoilAcidity) a);
		if (a instanceof PlantGrowth)
			return new PlantGrowthSensor(id, p, (PlantGrowth) a);

		return null;
	}
	
	private String getNameOfAction(Actuator a) {
		if (a instanceof SeedInjector)
			return "Seeding";
		if (a instanceof FertilizerInjector)
			return "Fertilizing";
		if (a instanceof Irrigator)
			return "Irrigating";
		if (a instanceof Harvester)
			return "Harvesting";
		if (a instanceof Replanter)
			return "Replanting";
		return null;
	}
	
	private String getNameOfMeasurement(Sensor a) {
		if (a instanceof SoilHumiditySensor)
			return "Soil Humidity";
		if (a instanceof SoilAciditySensor)
			return "Soil Acidity";
		if (a instanceof PlantGrowthSensor)
			return "Plant Growth";
		return null;
	}

	public void stopSensors() {
		enabled.set(false);
	}

	public void startSensors() {
		enabled.set(true);
	}

	private static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(Double.toString(value));
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

	private static int randomInt(int from, int to) {
		return from + (int) (new Random().nextFloat() * (to - from));
	}

	private static double randomDouble(double from, double to) {
		return round(from + new Random().nextDouble() * (to - from), 2);
	}
	
	private static LifeCycleStage randomStage() {
		List<LifeCycleStage> stages = Arrays.asList(LifeCycleStage.SEED, LifeCycleStage.SEEDLING, LifeCycleStage.YOUNG, LifeCycleStage.ADULT, LifeCycleStage.FRUIT);
		int idx = randomInt(0, 5);
		return stages.get(idx);
	}
	
	private DeviceInstance getDeviceById(int id) {
		for (Iterator<DeviceInstance> iterator = devices.iterator(); iterator.hasNext();) {
			DeviceInstance d = (DeviceInstance) iterator.next();
			if(d.getId() == id) return d;
		}
		return null;
	}
}
