/**
 * 
 */
package com.ase0401.msfsdemo.factory;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;

import org.springframework.stereotype.Component;

import msfs_0401.Action;
import msfs_0401.Address;
import msfs_0401.AdultTreatment;
import msfs_0401.Amount;
import msfs_0401.Capability;
import msfs_0401.Condition;
import msfs_0401.Configuration;
import msfs_0401.DeviceState;
import msfs_0401.FarmingDevice;
import msfs_0401.FarmingSite;
import msfs_0401.FinancialEntry;
import msfs_0401.FruitTreatment;
import msfs_0401.GrowProgram;
import msfs_0401.IncomeStatement;
import msfs_0401.LifeCycleStage;
import msfs_0401.MaintenanceData;
import msfs_0401.Msfs_0401Factory;
import msfs_0401.Order;
import msfs_0401.OrderItem;
import msfs_0401.OrderStatus;
import msfs_0401.Plant;
import msfs_0401.PlantLayout;
import msfs_0401.PlantSpecies;
import msfs_0401.PlantToPosMapping;
import msfs_0401.Position;
import msfs_0401.Role;
import msfs_0401.SeedTreatment;
import msfs_0401.SeedlingTreatment;
import msfs_0401.Type;
import msfs_0401.User;
import msfs_0401.YoungTreatment;

/**
 * This class encapsulates the creation logic for central model types defined in
 * the EMF project
 * 
 * @author stela
 *
 */
@Component
public class ModelFactory {

	private static Msfs_0401Factory factory = Msfs_0401Factory.eINSTANCE;

	public User createUser(int id, String type, String fname, String lname, String username, String password,
			String company, String contact) {
		User newUser = factory.createUser();
		Type newType = factory.createType();

		switch (type) {

		case "Micro Farmer":
			newUser = factory.createMicroFarmer();
			newType.getRoles().add(Role.ROLE_FARMER);
			newType.getRoles().add(Role.ROLE_CONSUMER);
			newType.getRoles().add(Role.ROLE_SEEDSUPPLIER);
			break;

		case "Device Supplier":
			newUser = factory.createDeviceSupplier();
			newType.getRoles().add(Role.ROLE_DEVICESUPPLIER);
			break;

		case "Seed Supplier":
			newUser = factory.createSeedSupplier();
			newType.getRoles().add(Role.ROLE_SEEDSUPPLIER);
			break;

		case "Consumer":
			newUser = factory.createConsumer();
			newType.getRoles().add(Role.ROLE_CONSUMER);
			break;

		default:
			newUser = factory.createConsumer();
			newType.getRoles().add(Role.ROLE_CONSUMER);
			break;
		}

		newUser.setId(id);
		newUser.setFirstname(fname);
		newUser.setLastname(lname);
		newUser.setUsername(username);
		newUser.setPassword(password);
		newUser.setCompany(company);
		newUser.setContact(contact);
		newUser.setType(newType);

		return newUser;
	}

	public IncomeStatement createIncomeStatement() {
		return factory.createIncomeStatement();
	}

	public FinancialEntry createFinancialEntry(String type, Double amount, String description, String currency,
			Date date) {
		FinancialEntry entry = factory.createFinancialEntry();

		switch (type) {
		case "Revenue":
			entry = factory.createRevenue();
			break;

		case "Expense":
			entry = factory.createExpense();
			break;

		case "Operational Cost":
			entry = factory.createOperationalCost();
			break;
		}

		entry.setAmount(amount);
		entry.setDescription(description);
		entry.setCurrency(currency);
		entry.setDate(date);

		return entry;
	}

	public Plant createPlant(int id, String atStage, String name, String scientificName, Double price,
			boolean forSale, String owner, int quantity) {
		Plant plant = factory.createPlant();

		plant.setId(id);

		switch (atStage) {
		case "seed":
			plant.setCurrentStage(LifeCycleStage.SEED);
			break;

		case "seedling":
			plant.setCurrentStage(LifeCycleStage.SEEDLING);
			break;

		case "young":
			plant.setCurrentStage(LifeCycleStage.YOUNG);
			break;

		case "adult":
			plant.setCurrentStage(LifeCycleStage.ADULT);
			break;

		case "fruit":
			plant.setCurrentStage(LifeCycleStage.FRUIT);
			break;
		}

		PlantSpecies species = factory.createPlantSpecies();
		species.setName(name);
		species.setScientificName(scientificName);
		plant.setSpecies(species);

		plant.setPrice(price);
		plant.setForSale(forSale);
		
		plant.setOwner(owner);
		
		plant.setQuantity(quantity);

		return plant;
	}

	public FarmingSite createFarmingSite(int id, String name, String owner) {
		FarmingSite site = factory.createFarmingSite();
		site.setId(id);
		site.setName(name);
		site.setOwner(owner);

		PlantLayout layout = factory.createPlantLayout();

		// initialize the positions and initialize the layout

		for (int i = 1; i <= 55; i++) {
			PlantToPosMapping p2p = factory.createPlantToPosMapping();
			Position pos = factory.createPosition();
			pos.setNumber(i);
			site.getPositions().add(pos);
			p2p.setPosition(i);
			p2p.setPlant("");
			layout.getMappings().add(p2p);
		}
		
		site.setCurrentLayout(layout);

		site.setAssignedDevice(null);

		return site;
	}

	public ArrayList<Capability> createCapabilityList(int count) {
		ArrayList<Capability> list = new ArrayList<>();

		for (int i = 0; i < count; i++) {
			list.add(factory.createCapability());
		}

		return list;
	}

	public FarmingDevice createFarmingDevice(int id, String name, boolean active, String supplier,
			ArrayList<Capability> capabilities, String owner, String assignedSite) {
		FarmingDevice device = factory.createFarmingDevice();

		device.setId(id);
		device.setName(name);
		device.setActive(active);
		device.setSupplier(supplier);

		for (Iterator<Capability> iterator = capabilities.iterator(); iterator.hasNext();) {
			Capability capability = (Capability) iterator.next();
			device.getCapabilities().add(capability);
		}

		MaintenanceData md = factory.createMaintenanceData();
		md.setBatteryLevel(100.0);
		md.setOperationHours(0);
		md.setFirmwareVersion("V1");
		md.setState(DeviceState.OK);

		device.setMaintenanceData(md);

		device.setAssignedSite(assignedSite);

		device.setOwner(owner);

		return device;
	}

	public Configuration createConfiguration() {
		return factory.createConfiguration();
	}

	public Action createAction(String type) {
		Action a = factory.createAction();
		switch (type) {
		case "seeding":
			a = factory.createSeeding();
			break;
		case "replanting":
			a = factory.createReplanting();
			break;
		case "fertilizing":
			a = factory.createFertilizing();
			break;
		case "irrigating":
			a = factory.createIrrigating();
			break;
		case "harvesting":
			a = factory.createHarvesting();
			break;
		}

		return a;

	}
	
	public Condition createCondition(String cond) {
		Condition c = factory.createCondition();
		switch(cond) {
		case "acidity":
			c = factory.createSoilAcidity();
			break;
		case "humidity":
			c = factory.createSoilHumidity();
			break;
		}
		return c;
	}
	
	public PlantToPosMapping createP2Pmapping() {
		return factory.createPlantToPosMapping();
	}

	public Position createPosition(int nr) {
		Position p = factory.createPosition();
		p.setNumber(nr);
		return p;
	}
	
	public PlantLayout createPlantLayout() {
		return factory.createPlantLayout();
	}
	
	public GrowProgram createGrowProgram() {
		return factory.createGrowProgram();
	}
	
	public SeedTreatment createSeedTreatment() {
		return factory.createSeedTreatment();
	}
	
	public SeedlingTreatment createSeedlingTreatment() {
		return factory.createSeedlingTreatment();
	}
	
	public YoungTreatment createYoungTreatment() {
		return factory.createYoungTreatment();
	}
	
	public AdultTreatment createAdultTreatment() {
		return factory.createAdultTreatment();
	}
	
	public FruitTreatment createFruitTreatment() {
		return factory.createFruitTreatment();
	}
	
	public Amount createAmount() {
		return factory.createAmount();
	}
	
	public OrderItem createOrderItem(int id, String item, double price, int quantity, String owner){
		
		OrderItem orderItem = factory.createOrderItem();
		orderItem.setId(id);	
		orderItem.setItem(item);
		orderItem.setPrice(price);
		orderItem.setQuantity(quantity);
		orderItem.setSeller(owner);
		
		return orderItem;
	}
	
public Order createNewOrder() {
	return factory.createOrder();
}
	
public Address createaddress(String street, String zipCode, String city, String country) {
	
		Address address = factory.createAddress();
		
		address.setStreet(street);
		address.setZipCode(zipCode);
		address.setCity(city);
		address.setCountry(country);
		
		
		return address;
	}

}
