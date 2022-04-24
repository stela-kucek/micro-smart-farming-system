/**
 * 
 */
package com.ase0401.msfsdemo.controller;

import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ase0401.msfsdemo.controller.form.DeviceMaintenanceForm;
import com.ase0401.msfsdemo.controller.form.DeviceManagementForm;
import com.ase0401.msfsdemo.controller.form.NotificationForm;
import com.ase0401.msfsdemo.repository.FarmingDeviceRepository;
import com.ase0401.msfsdemo.service.NotificationService;
import com.ase0401.msfsdemo.service.UserService;

import msfs_0401.DeviceSupplier;
import msfs_0401.FarmingDevice;
import msfs_0401.MaintenanceData;

/**
 * @author stela
 *
 */
@Controller
public class DeviceSupplierDevicesController {
	
	@Autowired
	private UserService userService;
	
		
	@Autowired
	private FarmingDeviceRepository repo;
	
	@Autowired
	private NotificationService notifications;
	
	
	private ArrayList<FarmingDevice> devices;
	
	
	@GetMapping("/customerDevices")
	public String showDevices(Authentication auth, Model model) {
		String username = auth.getName();
		
		DeviceSupplier user = (DeviceSupplier) userService.findByUsername(username);
		devices = repo.getFarmingDevicesFromSupplier(user.getCompany());
		ArrayList<String> messages = getMessages(devices);
		
		model.addAttribute("entries", devices );
		model.addAttribute("deviceForm", new DeviceMaintenanceForm());
		model.addAttribute("states", messages);
	
		
		return "deviceSupplierDevices";
	}
	
	private ArrayList<String> getMessages(ArrayList<FarmingDevice> list) {
		ArrayList<String> msgs = new ArrayList<>();

		for (Iterator<FarmingDevice> iterator = list.iterator(); iterator.hasNext();) {
			FarmingDevice d = (FarmingDevice) iterator.next();
			String msg = notifications.getMessages().getLatestDeviceStateMsg(d.getId());
			msgs.add(msg);
		}

		return msgs;
	}
	
	@GetMapping("/inspectDevice")
	public String showinspectDevicePage(Authentication auth, @ModelAttribute("deviceForm") DeviceManagementForm device, Model model) {
		String chosen = device.getChosenDevice();
		String username = auth.getName();
		DeviceSupplier user = (DeviceSupplier) userService.findByUsername(username);
		
		if (chosen == null) {
			model.addAttribute("infoMessage", "Please select a device.");
			model.addAttribute("entries", repo.getFarmingDevicesFromSupplier(user.getCompany()));
			model.addAttribute("deviceForm", new DeviceManagementForm());
			return "deviceSupplierDevices";
		} else {
			int deviceId = Integer.valueOf(chosen);
			model.addAttribute("entry", repo.getFarmingDeviceById(deviceId));
			model.addAttribute("notification", new NotificationForm());
			return "inspectDevice";
		}

	}
	@PostMapping("/firmwareUpdate")
	public String deviceFirmwareUpdate(Authentication auth, @ModelAttribute("deviceForm") DeviceManagementForm device, Model model) {
		int deviceId = Integer.valueOf(device.getChosenDevice());
		FarmingDevice deviceFirmwareToUpdate = repo.getFarmingDeviceById(deviceId);
		MaintenanceData firmwareUpdate = repo.getFarmingDeviceById(deviceId).getMaintenanceData();
		if (firmwareUpdate.getFirmwareVersion().equals("V2")) //must be compared with the actual firmware update file 
		{ 
			model.addAttribute("infoMessage", "Your device is up to date.");
			model.addAttribute("entry", repo.getFarmingDeviceById(deviceId));
			model.addAttribute("notification", new NotificationForm());
			return "inspectDevice";
			
		}
		firmwareUpdate.setFirmwareVersion("V2");
		deviceFirmwareToUpdate.setMaintenanceData(firmwareUpdate);
		repo.updateFarmingDevice(deviceFirmwareToUpdate);
		System.out.println(repo.getFarmingDeviceById(deviceId).getName() + " Firmware successfully updated!");
		return "firmwareUpdate-success";
	}

}
