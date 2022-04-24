/**
 * 
 */
package com.ase0401.msfsdemo.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ase0401.msfsdemo.controller.form.MarketItemForm;
import com.ase0401.msfsdemo.controller.form.PlantControllerForm;
import com.ase0401.msfsdemo.management.PlantManagement;
import com.ase0401.msfsdemo.service.DeviceControlService;
import com.ase0401.msfsdemo.service.NotificationService;
import com.ase0401.msfsdemo.service.UserService;

import msfs_0401.Plant;

/**
 * @author azar
 *
 */
@Controller
public class MyPlantsController {

	@Autowired
	private UserService userService;

	@Autowired
	private PlantManagement plmt;

	@Autowired
	private DeviceControlService deviceService;

	@Autowired
	private NotificationService notifications;

	private static String INFO_MESSAGE = "";

	private static String SUCCESS_MESSAGE = "";
	
	@GetMapping("/plants")
	public String showDevices(Authentication auth, Model model) {

		String username = auth.getName();

		//mgmt.setUpFarmingSiteManagement(username, userService.getUserRepository());

		model.addAttribute("infoMessage", INFO_MESSAGE);
		model.addAttribute("success", SUCCESS_MESSAGE);
		// reset the values after displaying
		INFO_MESSAGE = "";
		SUCCESS_MESSAGE = "";
	
		ArrayList<Plant> list = plmt.getPlantsFromUser(username);
		
		model.addAttribute("plants", list);

		model.addAttribute("plantForm", new MarketItemForm());

		return "plants";
	}
	
	@GetMapping("/editPlant")
	public String showEditPlantPage(@ModelAttribute("plantForm") MarketItemForm form, Authentication auth, Model model) {
		String id = form.getChosenItem();
		model.addAttribute("pId", id);
		model.addAttribute("plantForm", new PlantControllerForm());
		
		return "editPlant";
		
	}
	
	
	@PostMapping("/editPlant")
	public String saveChangesToPlant(@ModelAttribute("plantForm") PlantControllerForm form, Authentication auth, Model model) {
		String id = form.getId();
		
		plmt.updatePlant(Integer.valueOf(id), form.getPrice(), form.getQuantity(), form.isForSale());
		
		SUCCESS_MESSAGE = "Plant successfully updated!";
		
		return "redirect:/plants";
		
	}
	
}
