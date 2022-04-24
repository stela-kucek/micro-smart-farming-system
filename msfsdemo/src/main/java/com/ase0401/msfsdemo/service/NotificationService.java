package com.ase0401.msfsdemo.service;

import java.sql.Timestamp;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MqttDefaultFilePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationService implements MqttCallback {

	@Autowired
	MessageContainer messages;

	public MessageContainer getMessages() {
		return messages;
	}

	private MqttAsyncClient client;
	private String brokerUrl;
	private boolean quietMode;
	private MqttConnectOptions conOpt;
	private boolean clean;
	private String password;
	private String userName;

	public NotificationService() {
	}

	/**
	 * Constructs an instance of the sample client wrapper
	 * 
	 * @param brokerUrl    the url to connect to
	 * @param clientId     the client id to connect with
	 * @param cleanSession clear state at end of connection or not (durable or
	 *                     non-durable subscriptions)
	 * @param quietMode    whether debug should be printed to standard out
	 * @param userName     the username to connect with
	 * @param password     the password for the user
	 * @throws MqttException
	 */
	public void setup(String brokerUrl, String clientId, boolean cleanSession, boolean quietMode, String userName,
			String password) throws MqttException {
		this.brokerUrl = brokerUrl;
		this.quietMode = quietMode;
		this.clean = cleanSession;
		this.userName = userName;
		this.password = password;
		// This sample stores in a temporary directory... where messages temporarily
		// stored until the message has been delivered to the server.
		// ..a real application ought to store them somewhere
		// where they are not likely to get deleted or tampered with
		String tmpDir = System.getProperty("java.io.tmpdir");
		MqttDefaultFilePersistence dataStore = new MqttDefaultFilePersistence(tmpDir);

		try {
			// Construct the connection options object that contains connection parameters
			// such as cleanSession and LWT
			conOpt = new MqttConnectOptions();
			conOpt.setCleanSession(clean);
			if (password != null) {
				conOpt.setPassword(this.password.toCharArray());
			}
			if (userName != null) {
				conOpt.setUserName(this.userName);
			}

			// Construct a non-blocking MQTT client instance
			client = new MqttAsyncClient(this.brokerUrl, clientId, dataStore);

			// Set this wrapper as the callback handler
			client.setCallback(this);

		} catch (MqttException e) {
			e.printStackTrace();
			log("Unable to set up client: " + e.toString());
			System.exit(1);
		}
	}

	/**
	 * Subscribe to a topic on an MQTT server Once subscribed this method waits for
	 * the messages to arrive from the server that match the subscription. It
	 * continues listening for messages until the enter key is pressed.
	 * 
	 * @param topicName to subscribe to (can be wild carded)
	 * @param qos       the maximum quality of service to receive messages at for
	 *                  this subscription
	 * @throws MqttException
	 */
	public void subscribe(String topicName, int qos) throws MqttException {

		// Connect to the MQTT server
		// issue a non-blocking connect and then use the token to wait until the
		// connect completes. An exception is thrown if connect fails.
		log("Connecting to " + brokerUrl + " with client ID " + client.getClientId());
		IMqttToken conToken = client.connect(conOpt, null, null);
		conToken.waitForCompletion();
		log("Connected");

		// Subscribe to the requested topic.
		// Control is returned as soon client has accepted to deliver the subscription.
		// Use a token to wait until the subscription is in place.
		log("Subscribing to topic \"" + topicName + "\" qos " + qos);

		IMqttToken subToken = client.subscribe(topicName, qos, null, null);
		subToken.waitForCompletion();
		log("Subscribed to topic \"" + topicName);

		/*
		 * // Disconnect the client // Issue the disconnect and then use the token to
		 * wait until // the disconnect completes. log("Disconnecting"); IMqttToken
		 * discToken = client.disconnect(null, null); discToken.waitForCompletion();
		 * log("Disconnected");
		 */
	}

	/**
	 * Utility method to handle logging. If 'quietMode' is set, this method does
	 * nothing
	 * 
	 * @param message the message to log
	 */
	private void log(String message) {
		if (!quietMode) {
			System.out.println(message);
		}
	}

	@Override
	public void connectionLost(Throwable cause) {
		// Called when the connection to the server has been lost.
		// An application may choose to implement reconnection
		// logic at this point. This sample simply exits.
		log("Connection to " + brokerUrl + " lost!" + cause);
		System.exit(1);

	}

	@Override
	public void messageArrived(String topic, MqttMessage message) throws Exception {
		// Called when a message arrives from the server that matches any
		// subscription made by the client
		String time = new Timestamp(System.currentTimeMillis()).toString();
		System.out.println("Time:\t" + time + "  Topic:\t" + topic + "  Message:\t" + new String(message.getPayload())
				+ "  QoS:\t" + message.getQos());
		storeMessage(time, topic, new String(message.getPayload()));
	
	}

	@Override
	public void deliveryComplete(IMqttDeliveryToken token) {
		// Called when a message has been delivered to the
		// server. The token passed in here is the same one
		// that was passed to or returned from the original call to publish.
		// This allows applications to perform asynchronous
		// delivery without blocking until delivery completes.
		//
		// This sample demonstrates asynchronous deliver and
		// uses the token.waitForCompletion() call in the main thread which
		// blocks until the delivery has completed.
		// Additionally the deliveryComplete method will be called if
		// the callback is set on the client
		//
		// If the connection to the server breaks before delivery has completed
		// delivery of a message will complete after the client has re-connected.
		// The getPendinTokens method will provide tokens for any messages
		// that are still to be delivered.
		try {
			log("Delivery complete callback: Publish Completed " + token.getMessage());
		} catch (Exception ex) {
			log("Exception in delivery complete callback" + ex);
		}

	}
	
	private void storeMessage(String time, String topic, String content) {
		String[] topicArr = topic.toString().split("/");
		String type = topicArr[1];
		String deviceId = topicArr[2];
		int deviceIdNr = Integer.valueOf(deviceId);
		Message message = new Message(time, content);
		
		if(type.equals("PlantStage")) {
			messages.addPlantStageMsgToDevice(deviceIdNr, message);
		}
		if(type.equals("State")) {
			messages.addDeviceStateMsgToDevice(deviceIdNr, message);
		}
		
	}

}
