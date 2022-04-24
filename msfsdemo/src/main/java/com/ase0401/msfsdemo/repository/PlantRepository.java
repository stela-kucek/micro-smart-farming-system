/**
 * 
 */
package com.ase0401.msfsdemo.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceImpl;
import org.springframework.stereotype.Repository;

import msfs_0401.Msfs_0401Package;
import msfs_0401.Plant;

/**
 * @author stela
 * 
 *         This class handles reading and writing of Plants.
 * 
 *         The Plant data is persisted to the following XMI file:
 *         /repository/plants.xmi
 * 
 */
@Repository
public class PlantRepository {

	private ResourceSet resSet;
	private Resource resource;

	static final String fileName = "repository/plants.xmi";

	/**
	 * Constructor
	 * 
	 * Initializes the resource
	 * 
	 */
	public PlantRepository() {
		
		Msfs_0401Package.eINSTANCE.eClass();
		Resource.Factory.Registry reg = Resource.Factory.Registry.INSTANCE;
		Map<String, Object> m = reg.getExtensionToFactoryMap();
		m.put("xmi", new XMIResourceFactoryImpl());
		this.resSet = new ResourceSetImpl();
		this.resource = new XMIResourceImpl();
		// check if file exists
		File tmpDir = new File(fileName);
		boolean exists = tmpDir.exists();
		
		if(exists) {
			System.out.println("File exists ");
			ArrayList<EObject> existingObjects = loadData();
			if (!existingObjects.isEmpty()) {
				// file exists, get the existing resource
				this.resource = this.resSet.getResource(URI.createFileURI(fileName), true);
			} else {
				this.resource = this.resSet.createResource(URI.createFileURI(fileName));
			}
		}
		// file doesn't exist, create
		else this.resource = this.resSet.createResource(URI.createFileURI(fileName));

	}

	/**
	 * Persists an object to the file defined by the @filename variable
	 * 
	 * @param data EObject to save
	 * 
	 */
	public void saveData(EObject data) {
		this.resource.getContents().add(data);
		try {
			this.resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			System.out.println("Saving data to file " + fileName + " has failed.\n" + e.getLocalizedMessage());
		}
	}

	/**
	 * Removes an object from file defined by the @filename variable
	 * 
	 * @param data EObject to remove
	 * 
	 */
	public void removeData(EObject data) {
		this.resource.getContents().remove(data);
		try {
			this.resource.save(Collections.EMPTY_MAP);
		} catch (IOException e) {
			System.out.println("Saving data to file " + fileName + " has failed.\n" + e.getLocalizedMessage());
		}
	}

	/**
	 * Returns a list of all EObjects stored in the file defined by @filename. Note
	 * that this list will also contain the EObjects *contained* in other EObjects
	 * (e.g., @PlantSpecies in @Plant objects)
	 * 
	 * @return list of all EObjects in the file
	 * 
	 */
	public ArrayList<EObject> loadData() {
		ArrayList<EObject> arrayList = new ArrayList<>();
		File source = new File(fileName);
		try {
			this.resource.load(new FileInputStream(source), new HashMap<Object, Object>());
			TreeIterator<EObject> it = this.resource.getAllContents();
			while (it.hasNext()) {
				arrayList.add(it.next());
			}
		} catch (IOException e) {
			System.out.println("Loading data from file " + fileName + " has failed. The file doesn't exist yet. \nMessage: " + e.getLocalizedMessage());
		}

		return arrayList;
	}

	/**
	 * Returns a list of Plants from the file defined by @filename
	 * 
	 * @return list of all Plants
	 * 
	 */
	public ArrayList<Plant> getPlants() {
		ArrayList<EObject> arrayList = loadData();
		ArrayList<Plant> list = new ArrayList<>();
		for (Iterator<EObject> iterator = arrayList.iterator(); iterator.hasNext();) {
			EObject object = iterator.next();
			if (object instanceof Plant) {
				Plant plant = (Plant) object;
				list.add(plant);
			}
		}
		return list;
	}

	/**
	 * Adds Plants defined in @plantsToAdd to the file defined by @filename
	 * 
	 * @param plantsToAdd
	 * 
	 */
	public void addPlants(ArrayList<Plant> plantsToAdd) {
		for (Iterator<Plant> iterator = plantsToAdd.iterator(); iterator.hasNext();) {
			Plant plant = (Plant) iterator.next();
			saveData(plant);
		}
	}

	/**
	 * Adds a single Plant defined by @plant to the file defined by @filename
	 * 
	 * @param plant
	 * 
	 */
	public void addPlant(Plant plant) {
		saveData(plant);
	}

	/**
	 * Deletes a specific Plant from file defined by the @filename variable
	 * 
	 */
	public void deletePlant(Plant plant) {
		removeData(plant);
	}

	/**
	 * Returns a Plant with the specified ID if such exists, returns null
	 * otherwise
	 * 
	 * @param id
	 * @return Plant defined by the passed @id, or null if no such Plant
	 *         exists
	 *         
	 */
	public Plant getPlantById(int id) {
		ArrayList<Plant> list = getPlants();
		for (Iterator<Plant> iterator = list.iterator(); iterator.hasNext();) {
			Plant plant = (Plant) iterator.next();
			if (plant.getId() == id)
				return plant;
		}
		return null;
	}

	/**
	 * Returns a Plant with the specified plantname if such exists, returns null
	 * otherwise
	 * 
	 * @param plantname
	 * @return Plant with the passed @plantname, or null if no such Plant
	 *         exists
	 *         
	 */
	public Plant getPlantByPlantname(String plantname) {
		ArrayList<Plant> list = getPlants();
		for (Iterator<Plant> iterator = list.iterator(); iterator.hasNext();) {
			Plant plant = (Plant) iterator.next();
			if (plant.getSpecies().getName().equalsIgnoreCase(plantname))
				return plant;
		}
		return null;
	}

	/**
	 * Deletes a specific Plant by their ID from file defined by the @filename
	 * variable
	 * 
	 * @param id ID of Plant to delete
	 */
	public void deletePlantById(int id) {
		Plant toDelete = getPlantById(id);
		removeData(toDelete);
	}

	/**
	 * Returns the ID of the next element in the storage file
	 * 
	 * @return the ID of the next potential entry 
	 */
	public int getNextId() {
		ArrayList<Plant> list = getPlants();
		if(list == null) {
			System.out.println("There are no stored plants currently");
			return 0;
		}
		return list.size();
	}

}
