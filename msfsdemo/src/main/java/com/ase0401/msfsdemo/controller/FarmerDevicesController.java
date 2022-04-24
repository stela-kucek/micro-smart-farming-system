/**
 * 
 */
package com.ase0401.msfsdemo.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ase0401.msfsdemo.controller.form.DeviceActionForm;
import com.ase0401.msfsdemo.controller.form.DeviceManagementForm;
import com.ase0401.msfsdemo.controller.form.DeviceRegistrationForm;
import com.ase0401.msfsdemo.management.FarmingSiteManagement;
import com.ase0401.msfsdemo.service.DeviceControlService;
import com.ase0401.msfsdemo.service.NotificationService;
import com.ase0401.msfsdemo.service.UserService;
import com.ase0401.msfsdemo.service.model.LogModel;

import msfs_0401.DeviceState;
import msfs_0401.FarmingDevice;

/**
 * @author stela
 *
 */
@Controller
public class FarmerDevicesController {

	@Autowired
	private UserService userService;

	@Autowired
	private FarmingSiteManagement mgmt;

	@Autowired
	private DeviceControlService deviceService;

	@Autowired
	private NotificationService notifications;

	private static String INFO_MESSAGE = "";

	private static String SUCCESS_MESSAGE = "";

	@GetMapping("/devices")
	public String showDevices(Authentication auth, Model model) {

		String username = auth.getName();

		mgmt.setUpFarmingSiteManagement(username);

		model.addAttribute("infoMessage", INFO_MESSAGE);
		model.addAttribute("success", SUCCESS_MESSAGE);
		// reset the values after displaying
		INFO_MESSAGE = "";
		SUCCESS_MESSAGE = "";
		model.addAttribute("selected", false);

		ArrayList<FarmingDevice> list = mgmt.getDevicesFromUser();
		// turnOffEmptyDevices(list);
		ArrayList<String> messages = getMessages(list);

		model.addAttribute("messages", messages);

		model.addAttribute("entries", list);

		model.addAttribute("deviceForm", new DeviceManagementForm());

		return "farmerDevices";
	}

	@PostMapping("/chargeDevice")
	public String chargeDevice(@ModelAttribute("deviceForm") DeviceManagementForm device, Model model) {
		String chosen = device.getChosenDevice();
		if (chosen == null) {
			INFO_MESSAGE = "Please select a device to charge.";
		} else {
			int deviceId = Integer.valueOf(chosen);
			System.out.println("Charging device...");
			mgmt.chargeDevice(deviceId);
			SUCCESS_MESSAGE = "Successfully charged!";
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return "redirect:/devices";
	}

	@PostMapping("/deleteDevice")
	public String deleteDevice(@ModelAttribute("deviceForm") DeviceManagementForm device, Model model) {

		String chosen = device.getChosenDevice();
		if (chosen == null) {
			INFO_MESSAGE = "Please select a device to delete.";
		} else {
			int deviceId = Integer.valueOf(chosen);
			String deviceName = mgmt.getDeviceName(deviceId);
			mgmt.removeDevice(deviceId);
			SUCCESS_MESSAGE = deviceName + " has been removed";
		}

		model.addAttribute("entries", mgmt.getDevicesFromUser());
		model.addAttribute("deviceForm", new DeviceManagementForm());

		return "farmerDevices";

	}

	@GetMapping("/reassignDevice")
	public String showReassignDevicePage(@ModelAttribute("deviceForm") DeviceManagementForm device, Model model) {
		String chosen = device.getChosenDevice();

		if (chosen == null) {
			INFO_MESSAGE = "Please select a device to reassign.";
			return "redirect:/devices";
		} else {
			int deviceId = Integer.valueOf(chosen);
			String deviceName = mgmt.getDeviceName(deviceId);
			String currentlyAssigned = mgmt.getAssignedSite(deviceId);
			model.addAttribute("newAssignment", new DeviceRegistrationForm());
			model.addAttribute("deviceName", deviceName);
			model.addAttribute("deviceId", chosen);
			model.addAttribute("currentSite", currentlyAssigned);
			model.addAttribute("entries", mgmt.getFreeSites());
			return "reassignDevice";
		}

	}

	@PostMapping("/reassignDevice")
	public String reassignDevice(@ModelAttribute("newAssignment") DeviceRegistrationForm assignment, Model model) {

		String newSite = assignment.getSite();
		int deviceId = Integer.valueOf(assignment.getDeviceId());
		mgmt.reassignDevice(deviceId, newSite);
		SUCCESS_MESSAGE = "Your device " + assignment.getName() + " has been reassigned successfully!";
		return "redirect:/devices";
	}

	@PostMapping("/removeAssignment")
	public String removeDeviceAssignment(@ModelAttribute("newAssignment") DeviceRegistrationForm assignment,
			Model model) {
		int deviceId = Integer.valueOf(assignment.getDeviceId());
		mgmt.reassignDevice(deviceId, null);
		SUCCESS_MESSAGE = "Your device " + assignment.getName() + " has been un-assigned successfully!";
		return "redirect:/devices";
	}

	@GetMapping("/registerDevice")
	public String showRegisterDevicePage(Model model) {
		model.addAttribute("device", new DeviceRegistrationForm());
		model.addAttribute("entries", mgmt.getFreeSites());
		return "registerDevice";
	}

	@PostMapping("/registerDevice")
	public String registerDevice(@ModelAttribute("device") DeviceRegistrationForm device, Model model) {
		if (device.getName().isEmpty()) {
			model.addAttribute("deviceRegistrationError", "Your new device needs a name!");
			model.addAttribute("device", device);
			model.addAttribute("entries", mgmt.getFreeSites());
			return "registerDevice";
		}
		if (mgmt.deviceNameExists(device.getName())) {
			model.addAttribute("deviceRegistrationError", "This name already exists. Pick another one!");
			model.addAttribute("device", device);
			model.addAttribute("entries", mgmt.getFreeSites());
			return "registerDevice";
		}

		String siteAssignment = device.getSite();
		mgmt.registerNewDevice(device.getName(), device.getSupplier(), siteAssignment);

		deviceService.registerDevice(mgmt.getDeviceByName(device.getName()).getId());
		SUCCESS_MESSAGE = "Your device " + device.getName() + " has been registered successfully!";

		return "redirect:/devices";
	}

	@PostMapping("/toggleActive")
	public String toggleActiveState(@ModelAttribute("deviceForm") DeviceManagementForm device, Model model) {
		String chosen = device.getChosenDevice();
		System.out.println("CHOSEN: " + chosen);
		if (chosen == null) {
			INFO_MESSAGE = "Please select a device to turn on/off.";
		} else {
			int deviceId = Integer.valueOf(chosen);
			String deviceName = mgmt.getDeviceName(deviceId);

			if (mgmt.getAssignedSite(deviceId) == null) {
				INFO_MESSAGE = "Cannot turn on/off an unassigned device.";
			} 
			
			else {
				boolean currentlyActive = mgmt.getDeviceActiveState(deviceId);
				mgmt.toggleDeviceState(deviceId);
				if (currentlyActive) {
					deviceService.toggleState(deviceId, "off");
				} 
				else if(mgmt.isDeviceEmpty(deviceId)) {
					INFO_MESSAGE = "Cannot turn on an empty device. Charge the device first.";
				}
				else {
					deviceService.toggleState(deviceId, "on");
				}

				SUCCESS_MESSAGE = deviceName + "'s active state has been updated.";
			}

		}

		return "redirect:/devices";
	}

	@GetMapping("/showSensorData")
	public String showSensorData(@ModelAttribute("deviceForm") DeviceManagementForm device, Model model) {
		String chosen = device.getChosenDevice();
		if (chosen == null) {
			INFO_MESSAGE = "Please select a device to show sensor data.";
			return "redirect:/devices";
		} else {
			int deviceId = Integer.valueOf(chosen);
			List<LogModel> response = deviceService.getSensorData(deviceId);
			model.addAttribute("entries", response);
			return "deviceSensorData";
		}
	}

	@GetMapping("/setAction")
	public String showSetActionPage(@ModelAttribute("deviceForm") DeviceManagementForm device, Model model) {
		String chosen = device.getChosenDevice();
		if (chosen == null) {
			INFO_MESSAGE = "Please select a device to set an action.";
			return "redirect:/devices";
		} else {
			int deviceId = Integer.valueOf(chosen);
			String deviceName = mgmt.getDeviceName(deviceId);

			String site = mgmt.getAssignedSite(deviceId);
			if (site == null) {
				INFO_MESSAGE = "Cannot set an action to an unassigned device.";
				return "redirect:/devices";
			}
			model.addAttribute("entries", mgmt.findSiteByName(site).getPositions());
			model.addAttribute("deviceName", deviceName);
			model.addAttribute("deviceId", chosen);
			model.addAttribute("actionForm", new DeviceActionForm());

			return "deviceSetAction";
		}
	}

	@PostMapping("/setAction")
	public String setAction(@ModelAttribute("actionForm") DeviceActionForm form, Model model) {
		String chosen = form.getChosenDevice();
		int deviceId = Integer.valueOf(chosen);
		String action = form.getChosenAction();
		String pos = form.getPosition();
		int posNr = Integer.valueOf(pos);
		double value = 0.0;
		String unit = null;

		if (!form.getValue().isEmpty() && form.getUnit() != null) {
			value = Double.valueOf(form.getValue());
			String[] units = form.getUnit().split(",");
			if (action.equals("Seeding"))
				unit = units[0];
			else if (action.equals("Irrigating"))
				unit = units[1];
			else if (action.equals("Fertilizing"))
				unit = units[2];
		}

		System.out.println("Chosen device: " + deviceId);
		System.out.println("Chosen action: " + action);
		System.out.println("Chosen position: " + pos);

		deviceService.setAction(deviceId, mgmt.getActionId(action.toLowerCase()), posNr, value, unit);

		SUCCESS_MESSAGE = "Action \"" + action + "\" successfully set!";

		return "redirect:/devices";

	}

	@GetMapping("/setMeasurement")
	public String showSetMeasurementPage(@ModelAttribute("deviceForm") DeviceManagementForm device, Model model) {
		String chosen = device.getChosenDevice();
		if (chosen == null) {
			INFO_MESSAGE = "Please select a device to set a measurement.";
			return "redirect:/devices";
		} else {
			int deviceId = Integer.valueOf(chosen);
			String deviceName = mgmt.getDeviceName(deviceId);

			String site = mgmt.getAssignedSite(deviceId);
			if (site == null) {
				INFO_MESSAGE = "Cannot perform measurements with an unassigned device.";
				return "redirect:/devices";
			}

			model.addAttribute("entries", mgmt.findSiteByName(site).getPositions());
			model.addAttribute("deviceName", deviceName);
			model.addAttribute("deviceId", chosen);
			model.addAttribute("actionForm", new DeviceActionForm());

			return "deviceSetMeasurement";
		}
	}

	@PostMapping("/setMeasurement")
	public String setMeasurement(@ModelAttribute("actionForm") DeviceActionForm form, Model model) {
		String chosen = form.getChosenDevice();
		int deviceId = Integer.valueOf(chosen);
		String action = form.getChosenAction();
		String pos = form.getPosition();
		int posNr = Integer.valueOf(pos);

		System.out.println("Chosen device: " + deviceId);
		System.out.println("Chosen measurement: " + action);
		System.out.println("Chosen position: " + pos);

		deviceService.setMeasurement(deviceId, mgmt.getMeasurementId(action.toLowerCase()), posNr);

		SUCCESS_MESSAGE = "Measurement \"" + action + "\" successfully set!";

		return "redirect:/devices";

	}

	private void turnOffEmptyDevices(ArrayList<FarmingDevice> list) {
		for (Iterator<FarmingDevice> iterator = list.iterator(); iterator.hasNext();) {
			FarmingDevice device = (FarmingDevice) iterator.next();
			if (device.getMaintenanceData().getState().equals(DeviceState.BATTERY_EMPTY)) {
				int id = device.getId();
				deviceService.toggleState(id, "off");
			}
		}
	}

	private ArrayList<String> getMessages(ArrayList<FarmingDevice> list) {
		ArrayList<String> msgs = new ArrayList<>();

		for (Iterator<FarmingDevice> iterator = list.iterator(); iterator.hasNext();) {
			FarmingDevice d = (FarmingDevice) iterator.next();
			String msg = notifications.getMessages().getLatestPlantStageMsg(d.getId());
			msgs.add(msg);
		}

		return msgs;
	}

}
