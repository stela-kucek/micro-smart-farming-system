/**
 * 
 */
package com.ase0401.msfsdemo.factory;

import java.util.ArrayList;
import java.util.Date;

import com.ase0401.msfsdemo.management.FinancialManagement;
//import com.ase0401.msfsdemo.management.GrowProgramManagement;
import com.ase0401.msfsdemo.repository.FarmingDeviceRepository;
import com.ase0401.msfsdemo.repository.FarmingSiteRepository;
import com.ase0401.msfsdemo.repository.GrowProgramRepository;
import com.ase0401.msfsdemo.repository.PlantRepository;
import com.ase0401.msfsdemo.repository.UserRepository;

import msfs_0401.Capability;
import msfs_0401.CapabilityType;

/**
 * @author stela
 *
 */
public class DummyDataGenerator {
	
	private static ModelFactory f = new ModelFactory();
	private static FinancialManagement fm = new FinancialManagement();
	//private static GrowProgramManagement gm = new
	
	public static void main(String[] args) {
		
		// Comment out things you don't want generated
		
		addUsers();
		
		addPlants();
		
		addDevices();
		
		addSites();
		
	//	addGrowProgram();
		
		//fm.setUpUserFinancesById(0, new UserRepository());
		//fm.addFinancialEntry("Operational Cost", 123.2, "water", "$", new Date());
		
	}
	
	private static void addUsers() {
		UserRepository ur = new UserRepository();
		
		// password: max123
		ur.addUser(f.createUser(ur.getNextId(), "Micro Farmer", "Max", "Mustermann", "max", "$2y$12$tiM5YdGKmlKspDADuMnasuAeiAGNufMJNdoGuYa09j11tFzjPaXsm", null, null));
		// password: joe123
		ur.addUser(f.createUser(ur.getNextId(), "Device Supplier", "Joe", "Smith", "joe", "$2y$12$bn2HcMKxyF0f7qOAVJz1q.3ZXQ04SeMrOTgZu1F2zLXumSdrub/4W", "Farmergenics", "joe.smith@gmail.com"));
	}
	
	private static void addPlants() {
		PlantRepository pr = new PlantRepository();
		pr.addPlant(f.createPlant(pr.getNextId(), "seed", "Carrot", "Daucus carota subsp. sativus", 2.0, true, "max", 4));
		pr.addPlant(f.createPlant(pr.getNextId(), "seed", "Tomato", "Solanum lycopersicum", 2.0, true, "max", 5));
		pr.addPlant(f.createPlant(pr.getNextId(), "seed", "Jalapeño", "Capsicum annuum 'Jalapeño'", 2.0, true, "max", 3));
	}
//	private static void addGrowProgram() {
//		GrowProgramRepository gr = new GrowProgramRepository();
//		gr.addGrowProgram(f.createGrowProgram());
//	}
//	
	private static void addDevices() {
		FarmingDeviceRepository fdr = new FarmingDeviceRepository();
		ArrayList<Capability> caps = f.createCapabilityList(5);
		caps.get(0).setName("Irrigating");
		caps.get(0).setType(CapabilityType.ACTION);
		caps.get(1).setName("Seeding");
		caps.get(1).setType(CapabilityType.ACTION);
		caps.get(2).setName("Soil Humidity");
		caps.get(2).setType(CapabilityType.MEASUREMENT);
		caps.get(3).setName("PlantGrowth");
		caps.get(3).setType(CapabilityType.MEASUREMENT);
		caps.get(4).setName("Fertilizing");
		caps.get(4).setType(CapabilityType.ACTION);
		fdr.addFarmingDevice(f.createFarmingDevice(fdr.getNextId(), "Bot 0", false, "Farmergenics", caps, "max", null));
		
		ArrayList<Capability> caps2 = f.createCapabilityList(5);
		caps2.get(0).setName("Irrigating");
		caps2.get(0).setType(CapabilityType.ACTION);
		caps2.get(1).setName("Seeding");
		caps2.get(1).setType(CapabilityType.ACTION);
		caps2.get(2).setName("Soil Humidity");
		caps2.get(2).setType(CapabilityType.MEASUREMENT);
		caps2.get(3).setName("PlantGrowth");
		caps2.get(3).setType(CapabilityType.MEASUREMENT);
		caps2.get(4).setName("Fertilizing");
		caps2.get(4).setType(CapabilityType.ACTION);
		fdr.addFarmingDevice(f.createFarmingDevice(fdr.getNextId(), "Bot 1", false, "Farmergenics", caps2, "max", null));
	}
	
	private static void addSites() {
		FarmingSiteRepository fsr = new FarmingSiteRepository();

		fsr.addFarmingSite(f.createFarmingSite(fsr.getNextId(), "Farming Site East", "max"));
		fsr.addFarmingSite(f.createFarmingSite(fsr.getNextId(), "Farming Site West", "max"));
	}
}
