/**
 * 
 */
package com.ase0401.device;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import msfs_0401.Configuration;

/**
 * @author stela
 *
 */
@Component
public class DeviceScheduler {
	
	private Map<Integer, List<Configuration>> todos = new HashMap<Integer, List<Configuration>>();

	public Map<Integer, List<Configuration>> getTodos() {
		return todos;
	}

	public void setTodos(Map<Integer, List<Configuration>> todos) {
		this.todos = todos;
	}
	
	public List<Configuration> getTodosOfDevice(int id) {
		return todos.get(id);
	}
	
	public void addTodoToDevice(int id, Configuration config) {
		if(todos.get(id) == null) {
			todos.put(id, new ArrayList<Configuration>());
		}
		todos.get(id).add(config);
	}
}
