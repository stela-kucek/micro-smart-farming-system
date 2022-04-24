/**
 * 
 */
package com.ase0401.device.repository;

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

import msfs_0401.Log;
import msfs_0401.Msfs_0401Package;

/**
 * @author stela
 * 
 *         This class handles reading and writing of Farming Device Logs.
 * 
 *         The Log data is persisted to XMI files into the following directory:
 *         /device/device-logs/
 * 
 *         The XMI files contain the ID of the respective device for easier
 *         access and organization, as well as for a better overview.
 */
@Repository
public class LogRepository {

	private ResourceSet resSet;
	private Resource resource;

	static final String fileNamePrefix = "device-logs/device-";
	static final String fileNameSuffix = ".xmi";

	private String fileName = "";

	/**
	 * 
	 * Initializes the name of the file which should be operated on based on
	 * the @deviceId and prepares the resource
	 * 
	 * @param deviceId
	 */
	public void setUpRepository(int deviceId) {
		// initialize filename to be used
		fileName = fileNamePrefix + deviceId + fileNameSuffix;

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
			//System.out.println("File exists ");
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
	 * (e.g., @performedStep in @Log objects)
	 * 
	 * @return list of all EObjects in the file
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
	 * Returns a list of Logs from the file defined by @filename
	 * 
	 * @return list of all Logs
	 */
	public ArrayList<Log> getLogs() {
		ArrayList<EObject> arrayList = loadData();
		ArrayList<Log> list = new ArrayList<>();
		for (Iterator<EObject> iterator = arrayList.iterator(); iterator.hasNext();) {
			EObject object = iterator.next();
			if (object instanceof Log) {
				Log log = (Log) object;
				list.add(log);
			}
		}
		return list;
	}
	
	/**
	 * Adds a single Log defined by @log to the file defined by @filename
	 * 
	 * @param log
	 * 
	 */
	public void addLog(Log log) {
		saveData(log);
	}

	/**
	 * Adds logs defined in @logsToAdd to the file defined by @filename
	 * 
	 * @param logsToAdd
	 */
	public void addLogsToDevice(ArrayList<Log> logsToAdd) {
		for (Iterator<Log> iterator = logsToAdd.iterator(); iterator.hasNext();) {
			Log log = (Log) iterator.next();
			saveData(log);
		}
	}
	
	/**
	 * Returns the ID of the next element in the storage file
	 * 
	 * @return the ID of the next potential entry 
	 */
	public int getNextId() {
		ArrayList<Log> list = getLogs();
		if(list == null) {
			System.out.println("There are no logs currently");
			return 0;
		}
		return list.size();
	}

}
