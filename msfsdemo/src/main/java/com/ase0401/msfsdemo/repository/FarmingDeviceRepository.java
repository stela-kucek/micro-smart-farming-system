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

import msfs_0401.FarmingDevice;
import msfs_0401.Msfs_0401Package;

/**
 * @author stela
 * 
 *         This class handles reading and writing of FarmingDevices.
 * 
 *         The FarmingDevice data is persisted to the following XMI file:
 *         /repository/devices.xmi
 * 
 */
@Repository
public class FarmingDeviceRepository {

	private ResourceSet resSet;
	private Resource resource;

	static final String fileName = "repository/devices.xmi";

	/**
	 * Constructor
	 * 
	 * Initializes the resource
	 * 
	 */
	public FarmingDeviceRepository() {
		
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
		System.out.println("removing device " + data);
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
	 * (e.g., @Plants in @FarmingDevice objects)
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
	 * Returns a list of FarmingDevices from the file defined by @filename
	 * 
	 * @return list of all FarmingDevices
	 * 
	 */
	public ArrayList<FarmingDevice> getFarmingDevices() {
		ArrayList<EObject> arrayList = loadData();
		ArrayList<FarmingDevice> list = new ArrayList<>();
		for (Iterator<EObject> iterator = arrayList.iterator(); iterator.hasNext();) {
			EObject object = iterator.next();
			if (object instanceof FarmingDevice) {
				FarmingDevice device = (FarmingDevice) object;
				list.add(device);
			}
		}
		return list;
	}
	

	/**
	 * Returns a list of FarmingDevices from a specific supplier from the file defined by @filename
	 * 
	 * @param name of the supplier 
	 * @return
	 */
	public ArrayList<FarmingDevice> getFarmingDevicesFromSupplier(String name) {
		ArrayList<EObject> arrayList = loadData();
		ArrayList<FarmingDevice> list = new ArrayList<>();
		for (Iterator<EObject> iterator = arrayList.iterator(); iterator.hasNext();) {
			EObject object = iterator.next();
			if (object instanceof FarmingDevice) {
				FarmingDevice device = (FarmingDevice) object;
				if(device.getSupplier().equals(name)) {
					list.add(device);
				}
			}
		}
		return list;
	}

	/**
	 * Adds FarmingDevices defined in @devicesToAdd to the file defined by @filename
	 * 
	 * @param devicesToAdd
	 * 
	 */
	public void addFarmingDevices(ArrayList<FarmingDevice> devicesToAdd) {
		for (Iterator<FarmingDevice> iterator = devicesToAdd.iterator(); iterator.hasNext();) {
			FarmingDevice device = (FarmingDevice) iterator.next();
			saveData(device);
		}
	}

	/**
	 * Adds a single FarmingDevice defined by @device to the file defined by @filename
	 * 
	 * @param device
	 * 
	 */
	public void addFarmingDevice(FarmingDevice device) {
		saveData(device);
	}

	/**
	 * Deletes a specific FarmingDevice from file defined by the @filename variable
	 * 
	 * @param device FarmingDevice to delete
	 * 
	 */
	public void deleteFarmingDevice(FarmingDevice device) {
		removeData(device);
	}

	/**
	 * Returns a FarmingDevice with the specified ID if such exists, returns null
	 * otherwise
	 * 
	 * @param id
	 * @return FarmingDevice defined by the passed @id, or null if no such FarmingDevice 
	 *         exists
	 *         
	 */
	public FarmingDevice getFarmingDeviceById(int id) {
		ArrayList<FarmingDevice> list = getFarmingDevices();
		for (Iterator<FarmingDevice> iterator = list.iterator(); iterator.hasNext();) {
			FarmingDevice device = (FarmingDevice) iterator.next();
			if (device.getId() == id)
				return device;
		}
		return null;
	}

	/**
	 * Deletes a specific FarmingDevice by their ID from file defined by the @filename
	 * variable
	 * 
	 * @param id ID of the FarmingDevice to delete
	 * 
	 */
	public void deleteFarmingDeviceById(int id) {
		FarmingDevice toDelete = getFarmingDeviceById(id);
		removeData(toDelete);
	}

	/**
	 * Returns the ID of the next element in the storage file
	 * 
	 * @return the ID of the next potential entry 
	 */
	public int getNextId() {
		ArrayList<FarmingDevice> list = getFarmingDevices();
		if(list == null) {
			System.out.println("There are no stored devices currently");
			return 0;
		}
		return list.size();
	}
	
	public void updateFarmingDevice(FarmingDevice device) {
		saveData(device);
	}

}
