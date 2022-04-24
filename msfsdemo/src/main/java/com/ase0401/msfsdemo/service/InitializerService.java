/**
 * 
 */
package com.ase0401.msfsdemo.service;

import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ase0401.msfsdemo.repository.FarmingDeviceRepository;

import msfs_0401.FarmingDevice;

/**
 * @author stela
 *
 */

@Service
public class InitializerService {
	@Autowired
	DeviceControlService deviceService;
	
	@Autowired
	FarmingDeviceRepository deviceRepo;
	
	@Autowired
	MqttFarmerClient notifClient;
	
	public void initialize() {
		notifClient.setupClient();
		notifClient.subscribe();
		initializeExistingDevices();
	}
	
	private void initializeExistingDevices() {
		System.out.println("Registering existing devices...");
		ArrayList<FarmingDevice> devices = deviceRepo.getFarmingDevices();
		for (Iterator<FarmingDevice> iterator = devices.iterator(); iterator.hasNext();) {
			FarmingDevice farmingDevice = (FarmingDevice) iterator.next();
			deviceService.registerDevice(farmingDevice.getId());
		}
	}

}
