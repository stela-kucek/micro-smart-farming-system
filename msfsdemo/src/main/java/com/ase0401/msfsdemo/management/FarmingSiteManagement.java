/**
 * 
 */
package com.ase0401.msfsdemo.management;

import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ase0401.msfsdemo.constants.FarmbotCapabilities;
import com.ase0401.msfsdemo.factory.ModelFactory;
import com.ase0401.msfsdemo.repository.FarmingDeviceRepository;
import com.ase0401.msfsdemo.repository.FarmingSiteRepository;
import com.ase0401.msfsdemo.repository.UserRepository;

import msfs_0401.Action;
import msfs_0401.Configuration;
import msfs_0401.FarmingDevice;
import msfs_0401.FarmingSite;
import msfs_0401.PlantLayout;
import msfs_0401.PlantToPosMapping;
import msfs_0401.Position;

/**
 * @author stela
 *
 */
@Component
public class FarmingSiteManagement {

	@Autowired
	private ModelFactory factory;

	@Autowired
	private FarmingSiteRepository siteRepo;

	@Autowired
	private FarmingDeviceRepository deviceRepo;

	private String farmerUsername;

	private DeviceStateUpdater updater;


	public void setUpFarmingSiteManagement(String username) {
		farmerUsername = username;

	}

	public ArrayList<FarmingSite> getUserSites() {
		ArrayList<FarmingSite> resultList = new ArrayList<FarmingSite>();
		ArrayList<FarmingSite> siteList = siteRepo.getFarmingSites();
		for (Iterator<FarmingSite> iterator = siteList.iterator(); iterator.hasNext();) {
			FarmingSite farmingSite = (FarmingSite) iterator.next();
			if (farmingSite.getOwner().equals(farmerUsername)) {
				resultList.add(farmingSite);
			}

		}
		return resultList;
	}

	public ArrayList<FarmingDevice> getDevicesFromUser() {

		ArrayList<FarmingDevice> deviceList = deviceRepo.getFarmingDevices();
		ArrayList<FarmingDevice> resultList = new ArrayList<FarmingDevice>();

		for (Iterator<FarmingDevice> iterator = deviceList.iterator(); iterator.hasNext();) {
			FarmingDevice farmingDevice = (FarmingDevice) iterator.next();
			if (farmingDevice.getOwner().equals(farmerUsername)) {
				resultList.add(farmingDevice);
			}
		}

		return resultList;
	}
	
	public FarmingDevice getDeviceByName(String name) {
		ArrayList<FarmingDevice> deviceList = getDevicesFromUser();
		for (Iterator<FarmingDevice> iterator = deviceList.iterator(); iterator.hasNext();) {
			FarmingDevice farmingDevice = (FarmingDevice) iterator.next();
			if(farmingDevice.getName().equals(name)) {
				return farmingDevice;
			}
		}
		return null;
	}
	
	public boolean isDeviceEmpty(int id) {
		FarmingDevice d = deviceRepo.getFarmingDeviceById(id);
		if(d.getMaintenanceData().getBatteryLevel() <= 0.1) {
			return true;
		}
		return false;
	}
	
	public FarmingSite getSiteById(int id) {
		ArrayList<FarmingSite> list = getUserSites();
		for (Iterator<FarmingSite> iterator = list.iterator(); iterator.hasNext();) {
			FarmingSite site = (FarmingSite) iterator.next();
			if(site.getId() == id) return site;
		}
		
		return null;
	}
	
	public PlantLayout getPlantLayoutBySiteId(int id) {
		ArrayList<FarmingSite> list = getUserSites();

		for (Iterator<FarmingSite> iterator = list.iterator(); iterator.hasNext();) {
			FarmingSite site = (FarmingSite) iterator.next();
			if(site.getId() == id) return site.getCurrentLayout();
		}
		
		return null;
	}
	
	public PlantLayout getNewPlantLayout() {
		return factory.createPlantLayout();
	}
	
	public PlantToPosMapping getNewP2Pmapping() {
		return factory.createP2Pmapping();
	}
	
	public void updatePlantLayout(int siteId, PlantLayout newlayout) {
		FarmingSite site = getSiteById(siteId);
		site.setCurrentLayout(newlayout);
		siteRepo.updateFarmingSite(site);
	}
	
	
	public void chargeDevice(int id) {
		FarmingDevice toCharge = deviceRepo.getFarmingDeviceById(id);
		toCharge.getMaintenanceData().setBatteryLevel(100.0);
		deviceRepo.updateFarmingDevice(toCharge);
	}

	public void removeDevice(int id) {
		FarmingDevice deviceToDelete = deviceRepo.getFarmingDeviceById(id);
		String oldSiteName = deviceToDelete.getAssignedSite();
		if (oldSiteName != null) {
			FarmingSite oldSite = findSiteByName(oldSiteName);
			oldSite.setAssignedDevice(null);
			siteRepo.updateFarmingSite(oldSite);
		}
		deviceRepo.deleteFarmingDevice(deviceToDelete);
	}
	
	public boolean deviceNameExists(String name) {
		ArrayList<FarmingDevice> deviceList = getDevicesFromUser();
		for (Iterator<FarmingDevice> iterator = deviceList.iterator(); iterator.hasNext();) {
			FarmingDevice farmingDevice = (FarmingDevice) iterator.next();
			if(farmingDevice.getName().equalsIgnoreCase(name)) return true;
		}
		return false;
	}

	public String getDeviceName(int id) {
		return deviceRepo.getFarmingDeviceById(id).getName();
	}

	public String getSiteName(int id) {
		return siteRepo.getFarmingSiteById(id).getName();
	}
	
	public Position getNewPosition(int nr) {
		return factory.createPosition(nr);
	}

	public void reassignDevice(int deviceId, String site) {
		FarmingDevice deviceToUpdate = deviceRepo.getFarmingDeviceById(deviceId);
		String oldSiteName = deviceToUpdate.getAssignedSite();

		if (oldSiteName != null) {
			FarmingSite oldSite = findSiteByName(oldSiteName);
			oldSite.setAssignedDevice(null);
			siteRepo.updateFarmingSite(oldSite);
		}

		if (site != null) {
			int siteId = Integer.valueOf(site);
			FarmingSite siteToUpdate = siteRepo.getFarmingSiteById(siteId);
			siteToUpdate.setAssignedDevice(deviceToUpdate.getName());
			deviceToUpdate.setAssignedSite(siteToUpdate.getName());
			siteRepo.updateFarmingSite(siteToUpdate);
		} else
			deviceToUpdate.setAssignedSite(site);

		deviceRepo.updateFarmingDevice(deviceToUpdate);
	}

	public void registerNewDevice(String name, String supplier, String site) {

		int newId = deviceRepo.getNextId();

		while (idExists(newId)) {
			newId += 1;
		}

		if (site != null) {
			int siteId = Integer.valueOf(site);
			FarmingSite toUpdate = siteRepo.getFarmingSiteById(siteId);
			toUpdate.setAssignedDevice(name);
			siteRepo.updateFarmingSite(toUpdate);
			deviceRepo.addFarmingDevice(factory.createFarmingDevice(newId, name, false, supplier,
					FarmbotCapabilities.getCapabilities(), farmerUsername, toUpdate.getName()));
		}

		else
			deviceRepo.addFarmingDevice(factory.createFarmingDevice(newId, name, false, supplier,
					FarmbotCapabilities.getCapabilities(), farmerUsername, null));
	}

	public boolean idExists(int id) {
		ArrayList<FarmingDevice> deviceList = deviceRepo.getFarmingDevices();
		for (Iterator<FarmingDevice> iterator = deviceList.iterator(); iterator.hasNext();) {
			FarmingDevice farmingDevice = (FarmingDevice) iterator.next();
			if (farmingDevice.getId() == id)
				return true;
		}
		return false;
	}

	public ArrayList<FarmingSite> getFreeSites() {
		ArrayList<FarmingSite> resultList = new ArrayList<FarmingSite>();
		ArrayList<FarmingSite> siteList = getUserSites();
		for (Iterator<FarmingSite> iterator = siteList.iterator(); iterator.hasNext();) {
			FarmingSite farmingSite = (FarmingSite) iterator.next();
			if (farmingSite.getAssignedDevice() == null) {
				resultList.add(farmingSite);
			}
		}
		return resultList;
	}

	public FarmingSite findSiteByName(String name) {
		ArrayList<FarmingSite> list = getUserSites();
		for (Iterator<FarmingSite> iterator = list.iterator(); iterator.hasNext();) {
			FarmingSite farmingSite = (FarmingSite) iterator.next();
			if (farmingSite.getName().equals(name))
				return farmingSite;
		}
		return null;
	}

	public String getAssignedSite(int deviceId) {
		return deviceRepo.getFarmingDeviceById(deviceId).getAssignedSite();
	}

	public void toggleDeviceState(int deviceId) {
		FarmingDevice deviceToUpdate = deviceRepo.getFarmingDeviceById(deviceId);
		boolean currentlyActive = deviceToUpdate.isActive();
		if (currentlyActive) {
			deviceToUpdate.setActive(false);
		} else {
			deviceToUpdate.setActive(true);
		}

		deviceRepo.updateFarmingDevice(deviceToUpdate);
	}

	public boolean getDeviceActiveState(int deviceId) {
		FarmingDevice device = deviceRepo.getFarmingDeviceById(deviceId);
		return device.isActive();
	}

	public Configuration createNewConfiguration(String type, int nr) {
		Configuration config = factory.createConfiguration();
		Action action = factory.createAction(type);
		config.setCommand(action);
		Position pos = factory.createPosition(nr);
		config.setPosition(pos);
		return config;
	}

	public int getActionId(String action) {
		switch (action) {
		case "seeding":
			return 0;
		case "replanting":
			return 1;
		case "fertilizing":
			return 2;
		case "irrigating":
			return 3;
		case "harvesting":
			return 4;
		default:
			return 0;
		}

	}
	
	public int getMeasurementId(String action) {
		switch (action) {
		case "humidity":
			return 0;
		case "acidity":
			return 1;
		case "growth":
			return 2;
		default:
			return 0;
		}

	}

	@Scheduled(fixedRate = 1000)
	public void updateDevices() {

		ArrayList<FarmingDevice> deviceList = deviceRepo.getFarmingDevices();

		if (deviceList != null && !deviceList.isEmpty()) {
			updater = new DeviceStateUpdater();

			for (Iterator<FarmingDevice> iterator = deviceList.iterator(); iterator.hasNext();) {
				FarmingDevice farmingDevice = (FarmingDevice) iterator.next();

				deviceRepo.updateFarmingDevice(updater.updateDeviceState(farmingDevice));
			}
		}

	}

}
