package com.ase0401.msfsdemo.service;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ase0401.msfsdemo.repository.UserRepository;

@Service
public class MqttFarmerClient {
	
	@Autowired
	NotificationService notifService;
	
	@Autowired
	UserRepository userRepo;
	
	private static final String broker = "tcp://test.mosquitto.org:1883";
	
	private static final String topic = "DeviceMessages/#";
	
	public void setupClient() {
		try {
			notifService.setup(broker, "client-0", true, false, null, null);
			System.out.println("MqttFarmerClient initialized successfully.");
		} catch (MqttException e) {
			System.out.println("Initializing MqttFarmerClient threw an exception.");
			e.printStackTrace();
		}
	}
	
	public void subscribe() {
		try {
			notifService.subscribe(topic, 0);
		} catch (MqttException e) {
			System.out.println("Unable to subscribe to topic: " + topic + ", exception occurred.");
			e.printStackTrace();
		}
	}
	
}
