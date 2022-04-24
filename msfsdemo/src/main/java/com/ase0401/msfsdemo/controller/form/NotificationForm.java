/**
 * 
 */
package com.ase0401.msfsdemo.controller.form;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author peyman
 *
 */
public class NotificationForm {
	
	private String notificationMsg;
	private String deviceName;
	private String ownerName;
	private String timeStamp;

	public String getNotificationMsg() {
		return notificationMsg;
	}

	public void setNotificationMsg(String notificationMsg) {
		this.notificationMsg = notificationMsg;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}


	public String setTimeStamp() {
		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
		this.timeStamp = myDateObj.format(myFormatObj);
		return timeStamp;
	}

}
