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

import msfs_0401.FarmingSite;
import msfs_0401.Msfs_0401Package;

/**
 * @author stela
 * 
 *         This class handles reading and writing of FarmingSites.
 * 
 *         The FarmingSite data is persisted to the following XMI file:
 *         /repository/sites.xmi
 * 
 */
@Repository
public class FarmingSiteRepository {

	private ResourceSet resSet;
	private Resource resource;

	static final String fileName = "repository/sites.xmi";

	/**
	 * Constructor
	 * 
	 * Initializes the resource
	 * 
	 */
	public FarmingSiteRepository() {
		
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
	 * (e.g., @Plants in @FarmingSite objects)
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
	 * Returns a list of FarmingSites from the file defined by @filename
	 * 
	 * @return list of all FarmingSites
	 * 
	 */
	public ArrayList<FarmingSite> getFarmingSites() {
		ArrayList<EObject> arrayList = loadData();
		ArrayList<FarmingSite> list = new ArrayList<>();
		for (Iterator<EObject> iterator = arrayList.iterator(); iterator.hasNext();) {
			EObject object = iterator.next();
			if (object instanceof FarmingSite) {
				FarmingSite site = (FarmingSite) object;
				list.add(site);
			}
		}
		return list;
	}

	/**
	 * Adds FarmingSites defined in @sitesToAdd to the file defined by @filename
	 * 
	 * @param sitesToAdd
	 * 
	 */
	public void addFarmingSites(ArrayList<FarmingSite> sitesToAdd) {
		for (Iterator<FarmingSite> iterator = sitesToAdd.iterator(); iterator.hasNext();) {
			FarmingSite site = (FarmingSite) iterator.next();
			saveData(site);
		}
	}

	/**
	 * Adds a single FarmingSite defined by @site to the file defined by @filename
	 * 
	 * @param site
	 * 
	 */
	public void addFarmingSite(FarmingSite site) {
		saveData(site);
	}

	/**
	 * Deletes a specific FarmingSite from file defined by the @filename variable
	 * 
	 * @param site FarmingSite to delete
	 * 
	 */
	public void deleteFarmingSite(FarmingSite site) {
		removeData(site);
	}

	/**
	 * Returns a FarmingSite with the specified ID if such exists, returns null
	 * otherwise
	 * 
	 * @param id
	 * @return FarmingSite defined by the passed @id, or null if no such FarmingSite 
	 *         exists
	 *         
	 */
	public FarmingSite getFarmingSiteById(int id) {
		ArrayList<FarmingSite> list = getFarmingSites();
		for (Iterator<FarmingSite> iterator = list.iterator(); iterator.hasNext();) {
			FarmingSite site = (FarmingSite) iterator.next();
			if (site.getId() == id)
				return site;
		}
		return null;
	}

	/**
	 * Deletes a specific FarmingSite by their ID from file defined by the @filename
	 * variable
	 * 
	 * @param id ID of the FarmingSite to delete
	 * 
	 */
	public void deleteFarmingSiteById(int id) {
		FarmingSite toDelete = getFarmingSiteById(id);
		removeData(toDelete);
	}

	/**
	 * Returns the ID of the next element in the storage file
	 * 
	 * @return the ID of the next potential entry 
	 */
	public int getNextId() {
		ArrayList<FarmingSite> list = getFarmingSites();
		if(list == null) {
			System.out.println("There are no stored sites currently");
			return 0;
		}
		return list.size();
	}
	
	public void updateFarmingSite(FarmingSite site) {
		saveData(site);
	}

}
