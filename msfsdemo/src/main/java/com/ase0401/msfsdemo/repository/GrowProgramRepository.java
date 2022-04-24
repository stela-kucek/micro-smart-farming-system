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
import msfs_0401.GrowProgram;

/**
 * @author stela
 * 
 *         This class handles reading and writing of GrowPrograms.
 * 
 *         The GrowProgram data is persisted to the following XMI file:
 *         /repository/growPrograms.xmi
 * 
 */
@Repository
public class GrowProgramRepository {

	private ResourceSet resSet;
	private Resource resource;

	static final String fileName = "repository/growPrograms.xmi";

	/**
	 * Constructor
	 * 
	 * Initializes the resource
	 * 
	 */
	public GrowProgramRepository() {
		
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
	 * (e.g., @SeedTreatment in @GrowProgram objects)
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
	 * Returns a list of GrowPrograms from the file defined by @filename
	 * 
	 * @return list of all GrowPrograms
	 * 
	 */
	public ArrayList<GrowProgram> getGrowPrograms() {
		ArrayList<EObject> arrayList = loadData();
		ArrayList<GrowProgram> list = new ArrayList<>();
		for (Iterator<EObject> iterator = arrayList.iterator(); iterator.hasNext();) {
			EObject object = iterator.next();
			if (object instanceof GrowProgram) {
				GrowProgram growProgram = (GrowProgram) object;
				list.add(growProgram);
			}
		}
		return list;
	}

	/**
	 * Adds GrowPrograms defined in @growProgramsToAdd to the file defined by @filename
	 * 
	 * @param growProgramsToAdd
	 * 
	 */
	public void addGrowPrograms(ArrayList<GrowProgram> growProgramsToAdd) {
		for (Iterator<GrowProgram> iterator = growProgramsToAdd.iterator(); iterator.hasNext();) {
			GrowProgram growProgram = (GrowProgram) iterator.next();
			saveData(growProgram);
		}
	}

	/**
	 * Adds a single GrowProgram defined by @growProgram to the file defined by @filename
	 * 
	 * @param growProgram
	 * 
	 */
	public void addGrowProgram(GrowProgram growProgram) {
		saveData(growProgram);
	}

	/**
	 * Deletes a specific GrowProgram from file defined by the @filename variable
	 * 
	 * @param growProgram GrowProgram to delete
	 * 
	 */
	public void deleteGrowProgram(GrowProgram growProgram) {
		removeData(growProgram);
	}

	/**
	 * Returns a GrowProgram with the specified ID if such exists, returns null
	 * otherwise
	 * 
	 * @param id
	 * @return GrowProgram defined by the passed @id, or null if no such GrowProgram 
	 *         exists
	 *         
	 */
	public GrowProgram getGrowProgramById(int id) {
		ArrayList<GrowProgram> list = getGrowPrograms();
		for (Iterator<GrowProgram> iterator = list.iterator(); iterator.hasNext();) {
			GrowProgram growProgram = (GrowProgram) iterator.next();
			if (growProgram.getId() == id)
				return growProgram;
		}
		return null;
	}

	/**
	 * Deletes a specific GrowProgram by their ID from file defined by the @filename
	 * variable
	 * 
	 * @param id ID of the GrowProgram to delete
	 * 
	 */
	public void deleteGrowProgramById(int id) {
		GrowProgram toDelete = getGrowProgramById(id);
		removeData(toDelete);
	}

	/**
	 * Returns the ID of the next element in the storage file
	 * 
	 * @return the ID of the next potential entry 
	 */
	public int getNextId() {
		ArrayList<GrowProgram> list = getGrowPrograms();
		if(list == null) {
			System.out.println("There are no stored growPrograms currently");
			return 0;
		}
		return list.size();
	}

}
