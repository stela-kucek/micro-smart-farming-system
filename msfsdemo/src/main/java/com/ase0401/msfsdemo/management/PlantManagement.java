/**
 * 
 */
package com.ase0401.msfsdemo.management;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ase0401.msfsdemo.repository.PlantRepository;

import msfs_0401.Plant;
import msfs_0401.PlantSpecies;

/**
 * @author stela & azar
 *
 */

@Component
public class PlantManagement {
	
	@Autowired
	PlantRepository plantsRepo;
	
	
	public ArrayList<String> getPlantSpecies(){
		ArrayList<String> list = new ArrayList<String>();
		ArrayList<Plant> plants = plantsRepo.getPlants();
		Set<PlantSpecies> set = new HashSet<PlantSpecies>();
		for (Iterator<Plant> iterator = plants.iterator(); iterator.hasNext();) {
			Plant p = (Plant) iterator.next();
			set.add(p.getSpecies());
			
		}
		
		for (Iterator<PlantSpecies> iterator = set.iterator(); iterator.hasNext();) {
			PlantSpecies plantSpecies = (PlantSpecies) iterator.next();
			list.add(plantSpecies.getName());
		}
		return list;
	}
	
	public ArrayList<Plant> getPlantsFromUser(String username){
	
		ArrayList<Plant> plants = plantsRepo.getPlants();
		for (Iterator<Plant> iterator = plants.iterator(); iterator.hasNext();) {
			Plant plant = (Plant) iterator.next();
			if (plant.getOwner() == username) {
				plants.add(plant);
				}
			}
			
		return plants;
	}
	
	public void updatePlant(int id, double price, int quantity, boolean forsale) {
		Plant plant = plantsRepo.getPlantById(id);
		plant.setForSale(forsale);
		plant.setPrice(price);
		plant.setQuantity(quantity);
		plantsRepo.saveData(plant);
	}

}
