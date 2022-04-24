package com.ase0401.device.rest;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ase0401.device.DeviceInstance;
import com.ase0401.device.DeviceScheduler;
import com.ase0401.device.management.LogManager;
import com.ase0401.device.rest.model.CopyHelper;
import com.ase0401.device.rest.model.LogModel;

import msfs_0401.Configuration;
import msfs_0401.Log;

@RestController
@RequestMapping("/api")
public class DeviceRestController {

	ArrayList<Integer> activeDevices = new ArrayList<Integer>();
	ArrayList<DeviceInstance> devices = new ArrayList<DeviceInstance>();
	
	@Autowired
	private DeviceScheduler scheduler;
	
	@Autowired
	private LogManager manager;

	@Autowired
	private EventProducer eventProducer;

	@GetMapping("/hello")
	public String sayHello() {
		return "Hello World, this is a device";
	}

	@GetMapping("/logs/{deviceId}")
	public List<LogModel> getLogs(@PathVariable int deviceId) {
		manager.setUpManager(deviceId, activeDevices, scheduler.getTodos(), devices);
		eventProducer.setDeviceList(activeDevices);
		List<LogModel> logData = new ArrayList<>();
		List<Log> logs = manager.getLogsFromDevice();
		logData = CopyHelper.copyEClassToPOJO(logs);
		return logData;
	}

	@GetMapping("/sensordata/{deviceId}")
	public List<LogModel> getSensorData(@PathVariable int deviceId) {
		manager.setUpManager(deviceId, activeDevices, scheduler.getTodos(), devices);
		eventProducer.setDeviceList(activeDevices);
		List<LogModel> logData = new ArrayList<>();
		List<Log> logs = manager.getSensorData();
		logData = CopyHelper.copyEClassToPOJO(logs);
		
		return logData;
	}
	
	@GetMapping("/actuatordata/{deviceId}")
	public List<LogModel> getActuatorData(@PathVariable int deviceId) {
		manager.setUpManager(deviceId, activeDevices, scheduler.getTodos(), devices);
		eventProducer.setDeviceList(activeDevices);
		List<LogModel> logData = new ArrayList<>();
		List<Log> logs = manager.getActuatorData();
		logData = CopyHelper.copyEClassToPOJO(logs);
		
		return logData;
	}

	@PostMapping("/state/{deviceId}/{state}")
	public void activateDevice(@PathVariable int deviceId, @PathVariable String state) {

		if (state.equalsIgnoreCase("on")) {
			if (!isDeviceActive(deviceId)) {
				activeDevices.add(deviceId);
				manager.setUpManager(deviceId, activeDevices, scheduler.getTodos(), devices);
				eventProducer.setDeviceList(activeDevices);
				System.out.println("Starting device " + deviceId + "...");
			}
			
		}
		if (state.equalsIgnoreCase("off")) {
			if (isDeviceActive(deviceId)) {
				activeDevices.remove((Integer) deviceId);
				manager.setUpManager(deviceId, activeDevices, scheduler.getTodos(), devices);
				eventProducer.setDeviceList(activeDevices);
				System.out.println("Stopping device " + deviceId + "...");
			}
		}
	}
	
	@PostMapping("/measure/{deviceId}/{pos}/{what}")
	public void measure(@PathVariable int deviceId, @PathVariable int pos, @PathVariable int what) {
		Configuration config = manager.createNewMConfiguration(what, pos);
		
		scheduler.addTodoToDevice(deviceId, config);
	}
	
	@PostMapping("/clearschedule/{deviceId}") 
	public void clear(@PathVariable int deviceId) {
		if(scheduler.getTodosOfDevice(deviceId)!= null) scheduler.getTodosOfDevice(deviceId).clear();
	}

	@PostMapping("/config/{deviceId}/{pos}/{actionId}/{value}/{unit}")
	public void setAction(@PathVariable int deviceId, @PathVariable int pos, @PathVariable int actionId,  
			@PathVariable double value,
			 @PathVariable String unit ) {
		Configuration config = manager.createNewConfiguration(actionId, pos, value, unit);
		
		scheduler.addTodoToDevice(deviceId, config);
	}
	
	@PostMapping("/register/{deviceId}")
	public void registerDevice(@PathVariable int deviceId) {
		if(!deviceExists(deviceId)) devices.add(new DeviceInstance(deviceId));
	}
	
	private boolean isDeviceActive(int id) {
		for (Iterator<Integer> iterator = activeDevices.iterator(); iterator.hasNext();) {
			Integer integer = (Integer) iterator.next();
			if (integer == id)
				return true;
		}
		return false;
	}
	
	private boolean deviceExists(int id) {
		for (Iterator<DeviceInstance> iterator = devices.iterator(); iterator.hasNext();) {
			DeviceInstance i = (DeviceInstance) iterator.next();
			if(i.getId() == id) return true;
		}
		
		return false;
	}


}
