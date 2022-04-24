/**
 * 
 */
package com.ase0401.device.rest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author stela
 *
 */
@Component
public class EventProducer {

	private MqttAsyncClient mqttClient;

	private static final String broker = "tcp://test.mosquitto.org:1883";

	private static final String topic = "DeviceMessages/";

	public static String getBroker() {
		return broker;
	}

	public static String getTopic() {
		return topic;
	}

	private static final String firmwareOutdated = "The device's firmware is out-of-date.";
	private static final String imminentFailure = "The device is about to fail.";

	private static final List<String> events = Arrays.asList(firmwareOutdated, imminentFailure);
	private ArrayList<Integer> deviceList;

	public void setDeviceList(ArrayList<Integer> deviceList) {
		this.deviceList = deviceList;
	}

	@Scheduled(fixedRate = 180000) // send a random event regarding a random device every 15 minutes
	public void publishMessage() {
		if (deviceList != null && !deviceList.isEmpty()) {
			// randomly pick a device from active devices
			int randomIndex = randomInt(0, deviceList.size());
			int deviceId = deviceList.get(randomIndex);
			String fullTopic = topic + "State/" + deviceId;

			// randomly pick an event to publish
			int eventIndex = randomInt(0, 2);
			String event = events.get(eventIndex);

			try {

				mqttClient = new MqttAsyncClient(broker, "Device " + deviceId);
				MqttConnectOptions conOpt = new MqttConnectOptions();
				conOpt.setCleanSession(true);
				IMqttToken token = mqttClient.connect(conOpt, null, null);
				token.waitForCompletion();
				MqttMessage message = new MqttMessage(event.getBytes());
				System.out.println("Publishing device state message: " + message + " for device + " + deviceId);
				message.setQos(0);
				message.setRetained(true);
				IMqttDeliveryToken pubToken = mqttClient.publish(fullTopic, message, null, null);
				pubToken.waitForCompletion();
				IMqttToken dcToken = mqttClient.disconnect();
				dcToken.waitForCompletion();

			} catch (MqttException e) {

				System.out.println("reason " + e.getReasonCode());
				System.out.println("msg " + e.getMessage());
				System.out.println("loc " + e.getLocalizedMessage());
				System.out.println("cause " + e.getCause());
				System.out.println("excep " + e);
				e.printStackTrace();
			}
		}
	}
	
	public static void publishNotificationToHarvest(int deviceId, int pos) {
		String subtopic = topic + "PlantStage/" + deviceId;
		MqttAsyncClient mqttClient;
		String msg = "Plant at position "+pos+" is ready for harvest!";
		try {

			mqttClient = new MqttAsyncClient(broker, "" + deviceId);
			MqttConnectOptions conOpt = new MqttConnectOptions();
			conOpt.setCleanSession(true);
			IMqttToken token = mqttClient.connect(conOpt, null, null);
			token.waitForCompletion();
			MqttMessage message = new MqttMessage(msg.getBytes());
			System.out.println("Publishing notification to harvest message: " + message + " for device + " + deviceId);
			message.setQos(0);
			message.setRetained(true);
			IMqttDeliveryToken pubToken = mqttClient.publish(subtopic, message, null, null);
			pubToken.waitForCompletion();
			IMqttToken dcToken = mqttClient.disconnect();
			dcToken.waitForCompletion();

		} catch (MqttException e) {

			System.out.println("reason " + e.getReasonCode());
			System.out.println("msg " + e.getMessage());
			System.out.println("loc " + e.getLocalizedMessage());
			System.out.println("cause " + e.getCause());
			System.out.println("excep " + e);
			e.printStackTrace();
		}
	}

	private static int randomInt(int from, int to) {
		return from + (int) (new Random().nextFloat() * (to - from));
	}

}
