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
import msfs_0401.User;

/**
 * @author stela
 * 
 *         This class handles reading and writing of Users.
 * 
 *         The User data is persisted to the following XMI file:
 *         /repository/users.xmi
 * 
 */
@Repository
public class UserRepository {

	private ResourceSet resSet;
	private Resource resource;

	public Resource getResource() {
		return this.resource;
	}

	static final String fileName = "repository/users.xmi";

	/**
	 * Constructor
	 * 
	 * Initializes the resource
	 * 
	 */
	public UserRepository() {

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
				System.out.println("Existing objects not empty: " + existingObjects);
				// file exists, get the existing resource
				this.resource = this.resSet.getResource(URI.createFileURI(fileName), true);
			} else {
				System.out.println("Existing objects IS empty: " + existingObjects);
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
	 * (e.g., @Type in @User objects)
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
	 * Returns a list of Users from the file defined by @filename
	 * 
	 * @return list of all Users
	 * 
	 */
	public ArrayList<User> getUsers() {
		ArrayList<EObject> arrayList = loadData();
		ArrayList<User> list = new ArrayList<>();
		for (Iterator<EObject> iterator = arrayList.iterator(); iterator.hasNext();) {
			EObject object = iterator.next();
			if (object instanceof User) {
				User user = (User) object;
				list.add(user);
			}
		}
		return list;
	}

	/**
	 * Adds Users defined in @usersToAdd to the file defined by @filename
	 * 
	 * @param usersToAdd
	 * 
	 */
	public void addUsers(ArrayList<User> usersToAdd) {
		for (Iterator<User> iterator = usersToAdd.iterator(); iterator.hasNext();) {
			User user = (User) iterator.next();
			saveData(user);
		}
	}

	/**
	 * Adds a single User defined by @user to the file defined by @filename
	 * 
	 * @param user
	 * 
	 */
	public void addUser(User user) {
		saveData(user);
	}

	/**
	 * Deletes a specific User from file defined by the @filename variable
	 * 
	 */
	public void deleteUser(User user) {
		removeData(user);
	}

	/**
	 * Returns a User with the specified ID if such exists, returns null otherwise
	 * 
	 * @param id
	 * @return User defined by the passed @id, or null if no such User exists
	 * 
	 */
	public User getUserById(int id) {
		ArrayList<User> list = getUsers();
		for (Iterator<User> iterator = list.iterator(); iterator.hasNext();) {
			User user = (User) iterator.next();
			if (user.getId() == id)
				return user;
		}
		return null;
	}

	/**
	 * Returns a User with the specified username if such exists, returns null
	 * otherwise
	 * 
	 * @param username
	 * @return User with the passed @username, or null if no such User exists
	 * 
	 */
	public User getUserByUsername(String username) {
		ArrayList<User> list = getUsers();
		for (Iterator<User> iterator = list.iterator(); iterator.hasNext();) {
			User user = (User) iterator.next();
			if (user.getUsername().equalsIgnoreCase(username))
				return user;
		}
		return null;
	}

	/**
	 * Deletes a specific User by their ID from file defined by the @filename
	 * variable
	 * 
	 * @param id ID of User to delete
	 */
	public void deleteUserById(int id) {
		User toDelete = getUserById(id);
		removeData(toDelete);
	}

	/**
	 * Returns the ID of the next element in the storage file
	 * 
	 * @return the ID of the next potential entry
	 */
	public int getNextId() {
		ArrayList<User> list = getUsers();
		if (list == null) {
			System.out.println("There are no stored users currently");
			return 0;
		}
		// System.out.println("Returning id " + list.size() + " ... ");
		return list.size();
	}

	public void updateUser(User user) {
		saveData(user);
	}

}
