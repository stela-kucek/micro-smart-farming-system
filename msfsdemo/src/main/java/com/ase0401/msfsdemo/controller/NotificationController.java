package com.ase0401.msfsdemo.controller;

/**
 * @author peyman
 *
 */

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttAsyncClient;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ase0401.msfsdemo.controller.form.NotificationForm;



@Controller
public class NotificationController {
	


	

	private MqttAsyncClient mqttClient;
	private String broker = "tcp://test.mosquitto.org:1883";
	private String topic = "FarmingDeviceNotification/";
	private Map<String,String> msgsReceived = new HashMap<String,String>(); 
	
	
	
	
	
	@RequestMapping("/notifyCustomer")
	public String publish(@ModelAttribute("notification") NotificationForm notificationMsg, Authentication auth,Model model) {
		
		String pubTopic = topic + notificationMsg.getOwnerName() 
								+ "/" + auth.getName()
								+ "/"+ notificationMsg.getDeviceName() ;

		String msg = "<" 
					+ notificationMsg.setTimeStamp() + "> "
					+ notificationMsg.getNotificationMsg();
		try {
        	
        	mqttClient = new MqttAsyncClient(broker, auth.getName());
        	MqttConnectOptions conOpt = new MqttConnectOptions();
        	conOpt.setCleanSession(true);
        	IMqttToken token = mqttClient.connect(conOpt,null,null);
        	token.waitForCompletion();
        	MqttMessage message = new MqttMessage(msg.getBytes());
        	message.setQos(0);
        	message.setRetained(true);
        	IMqttDeliveryToken pubToken = mqttClient.publish(pubTopic, message,null,null);
        	pubToken.waitForCompletion();
        	IMqttToken dcToken = mqttClient.disconnect();
        	dcToken.waitForCompletion();
        	
        	
        }catch (MqttException e) {
            System.out.println("reason " + e.getReasonCode());
            System.out.println("msg " + e.getMessage());
            System.out.println("loc " + e.getLocalizedMessage());
            System.out.println("cause " + e.getCause());
            System.out.println("excep " + e);
            e.printStackTrace();
           
        }


		return "notify-success";
	}
	
	
	
	@GetMapping("/getNotifications")
	public String checkForNotifications(Authentication auth,Model model) {
		String subTopic = topic+ auth.getName()+"/#";
		try {
			MqttConnectOptions conOpt = new MqttConnectOptions();
        	conOpt.setCleanSession(true);

			mqttClient = new MqttAsyncClient(broker, auth.getName());
			final CountDownLatch latch = new CountDownLatch(1);
			mqttClient.setCallback(new MqttCallback() {
				
			 public void messageArrived(String topic, MqttMessage message) throws Exception {
				  			 
				    System.out.println(topic+message);
				 	msgsReceived.put(topic,new String(message.getPayload()));
				 	model.addAttribute("msgs",msgsReceived.entrySet());
		
				 		
				  latch.countDown(); 
			     }

			     public void connectionLost(Throwable cause) {
			         
			     }

			     public void deliveryComplete(IMqttDeliveryToken token) {
			     }

			 });
			
			
			IMqttToken token = mqttClient.connect(conOpt,null,null);
			token.waitForCompletion();
			IMqttToken subToken = mqttClient.subscribe(subTopic,0,null,null);
			subToken.waitForCompletion();
			IMqttToken discToken = mqttClient.disconnect(null, null);
	    	discToken.waitForCompletion();
			
			try {
					latch.await(2, TimeUnit.SECONDS);

	            } catch (InterruptedException e) {
	            	e.printStackTrace();
	            }
		} catch (MqttSecurityException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			System.out.println("reason " + e.getReasonCode());
            System.out.println("msg " + e.getMessage());
            System.out.println("loc " + e.getLocalizedMessage());
            System.out.println("cause " + e.getCause());
            System.out.println("excep " + e);
            e.printStackTrace();
			
		}
		
		//System.out.println(msgsReceived);
		return "notifications";
		
		
	}

}
