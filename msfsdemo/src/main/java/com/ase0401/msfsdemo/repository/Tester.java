/**
 * 
 */
package com.ase0401.msfsdemo.repository;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Date;

import msfs_0401.FarmingSite;
import msfs_0401.FinancialEntry;
import msfs_0401.IncomeStatement;
import msfs_0401.MicroFarmer;
import msfs_0401.Msfs_0401Factory;
import msfs_0401.Role;
import msfs_0401.Type;
import msfs_0401.User;

/**
 * @author stela
 * 
 * This class can be used for trying out the repositories.
 * See examples below
 *
 */
public class Tester {

	/**
	 * @param args
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public static void main(String[] args) throws FileNotFoundException, IOException {
		UserRepository userRepo = new UserRepository();
		Msfs_0401Factory factory = Msfs_0401Factory.eINSTANCE; 
		
		// e.g., user will choose "micro farmer" as account type:
		String chosenByUser = "Micro Farmer";
		
		Type type = factory.createType();
		type.setName(chosenByUser);
		
		MicroFarmer newUser = factory.createMicroFarmer();
		
		// Assign adequate roles: 
		type.getRoles().add(Role.ROLE_FARMER);
		type.getRoles().add(Role.ROLE_SEEDSUPPLIER);
		type.getRoles().add(Role.ROLE_CONSUMER);	
		
		newUser.setId(0);
		newUser.setFirstname("Test");
		newUser.setLastname("User");
		newUser.setContact("test.user@gmail.com");
		newUser.setUsername("test");
		newUser.setPassword("test");
		
		newUser.setType(type);
		
		userRepo.addUser(newUser);
		
		System.out.println("All Users:\n" + userRepo.getUsers());
		
		System.out.println("Fetch by id:\n" + userRepo.getUserById(0));
		
		// since this is a micro farmer, we can create a farming site and
		// make this user his owner
		
		FarmingSite site = factory.createFarmingSite();
		site.setId(0);
		site.setName("Test Site");
		site.setOwner(newUser.getUsername());
		
		FarmingSiteRepository siteRepo = new FarmingSiteRepository();
		siteRepo.addFarmingSite(site);
		//siteRepo.deleteFarmingSiteById(0);
		System.out.println("All Sites:\n" + siteRepo.getFarmingSites());
		System.out.println("Farming Site 0 is owned by " + siteRepo.getFarmingSiteById(0).getOwner());
	
		User u = userRepo.getUserById(0);
		u.setFirstname("Tanya");
		userRepo.updateUser(u);
		
		
		IncomeStatement is = factory.createIncomeStatement();
		FinancialEntry r = factory.createRevenue();
		r.setAmount(200.0);
		r.setCurrency("€");
		r.setDate(new Date());
		r.setDescription("Sold Grow Program 1");
		is.getFinancialEntries().add(r);
		//FinancialManagement fm = new FinancialManagement(0);

	}

}
