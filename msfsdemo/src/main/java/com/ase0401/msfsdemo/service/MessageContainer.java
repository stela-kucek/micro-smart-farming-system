package com.ase0401.msfsdemo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class MessageContainer {

	/**
	 * Contains plant stage notifications from devices when they detect that a plant
	 * is at fruit stage.
	 * 
	 * Keys are device ids Values are lists of messages from the corresponding
	 * device
	 */
	private Map<Integer, List<Message>> plantStageMessages = new HashMap<Integer, List<Message>>();

	/**
	 * Contains device state notifications from devices when they detect a randomly
	 * generated failure event, or firmware update requirement
	 * 
	 * Keys are device ids Values are lists of messages from the corresponding
	 * device
	 */
	private Map<Integer, List<Message>> deviceStateMessages = new HashMap<Integer, List<Message>>();

	public void addPlantStageMsgToDevice(int id, Message msg) {
		if (!plantStageMessages.containsKey(id)) {
			plantStageMessages.put(id, new ArrayList<Message>());
		}
		plantStageMessages.get(id).add(msg);
	}

	public void addDeviceStateMsgToDevice(int id, Message msg) {
		if (!deviceStateMessages.containsKey(id)) {
			deviceStateMessages.put(id, new ArrayList<Message>());
		}
		deviceStateMessages.get(id).add(msg);
	}

	public List<Message> getPlantStageMsgs(int id) {
		if (!plantStageMessages.containsKey(id)) {
			plantStageMessages.put(id, new ArrayList<Message>());
		}
		return plantStageMessages.get(id);
	}

	public List<Message> getDeviceStateMsgs(int id) {
		if (!deviceStateMessages.containsKey(id)) {
			deviceStateMessages.put(id, new ArrayList<Message>());
		}
		return deviceStateMessages.get(id);
	}

	public String getLatestPlantStageMsg(int id) {
		if (plantStageMessages.containsKey(id)) {
			int size = plantStageMessages.get(id).size();
			if (size != 0) {
				return plantStageMessages.get(id).get(size - 1).getContent();
			}
		}

		return "No recent messages";
	}

	public String getLatestDeviceStateMsg(int id) {
		if (deviceStateMessages.containsKey(id)) {
			int size = deviceStateMessages.get(id).size();
			if (size != 0) {
				return deviceStateMessages.get(id).get(size - 1).getContent();
			}
		}

		return "No recent messages";
	}

}
