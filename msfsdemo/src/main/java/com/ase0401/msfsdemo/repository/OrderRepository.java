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
import msfs_0401.Order;

/**
 * @author stela
 * 
 *         This class handles reading and writing of Orders.
 * 
 *         The Order data is persisted to the following XMI file:
 *         /repository/orders.xmi
 * 
 */
@Repository
public class OrderRepository {

	private ResourceSet resSet;
	private Resource resource;

	static final String fileName = "repository/orders.xmi";

	/**
	 * Constructor
	 * 
	 * Initializes the resource
	 * 
	 */
	public OrderRepository() {
		
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
	 * (e.g., @OrderItem in @Order objects)
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
	 * Returns a list of Orders from the file defined by @filename
	 * 
	 * @return list of all Orders
	 * 
	 */
	public ArrayList<Order> getOrders() {
		ArrayList<EObject> arrayList = loadData();
		ArrayList<Order> list = new ArrayList<>();
		for (Iterator<EObject> iterator = arrayList.iterator(); iterator.hasNext();) {
			EObject object = iterator.next();
			if (object instanceof Order) {
				Order order = (Order) object;
				list.add(order);
			}
		}
		return list;
	}

	/**
	 * Adds Orders defined in @ordersToAdd to the file defined by @filename
	 * 
	 * @param ordersToAdd
	 * 
	 */
	public void addOrders(ArrayList<Order> ordersToAdd) {
		for (Iterator<Order> iterator = ordersToAdd.iterator(); iterator.hasNext();) {
			Order order = (Order) iterator.next();
			saveData(order);
		}
	}

	/**
	 * Adds a single Order defined by @order to the file defined by @filename
	 * 
	 * @param order
	 * 
	 */
	public void addOrder(Order order) {
		saveData(order);
	}

	/**
	 * Deletes a specific Order from file defined by the @filename variable
	 * 
	 * @param order Order to delete
	 * 
	 */
	public void deleteOrder(Order order) {
		removeData(order);
	}

	/**
	 * Returns a Order with the specified ID if such exists, returns null
	 * otherwise
	 * 
	 * @param id
	 * @return Order defined by the passed @id, or null if no such Order 
	 *         exists
	 *         
	 */
	public Order getOrderById(int id) {
		ArrayList<Order> list = getOrders();
		for (Iterator<Order> iterator = list.iterator(); iterator.hasNext();) {
			Order order = (Order) iterator.next();
			if (order.getId() == id)
				return order;
		}
		return null;
	}

	/**
	 * Deletes a specific Order by their ID from file defined by the @filename
	 * variable
	 * 
	 * @param id ID of the Order to delete
	 * 
	 */
	public void deleteOrderById(int id) {
		Order toDelete = getOrderById(id);
		removeData(toDelete);
	}

	/**
	 * Returns the ID of the next element in the storage file
	 * 
	 * @return the ID of the next potential entry 
	 */
	public int getNextId() {
		ArrayList<Order> list = getOrders();
		if(list == null) {
			System.out.println("There are no stored orders currently");
			return 0;
		}
		return list.size();
	}

}
