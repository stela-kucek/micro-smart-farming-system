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

import com.ase0401.msfsdemo.controller.form.PlantLayoutForm;
import com.ase0401.msfsdemo.controller.form.SiteForm;
import com.ase0401.msfsdemo.management.FarmingSiteManagement;
import com.ase0401.msfsdemo.management.PlantManagement;
import com.ase0401.msfsdemo.service.UserService;

import msfs_0401.PlantLayout;
import msfs_0401.PlantToPosMapping;

/**
 * @author stela
 *
 */
@Controller
public class FarmerSitesController {

	@Autowired
	private UserService userService;

	@Autowired
	private FarmingSiteManagement mgmt;

	@Autowired
	private PlantManagement plantMgmt;

	private static String INFO_MESSAGE = "";

	private static String SUCCESS_MESSAGE = "";

	@GetMapping("/sites")
	public String showSites(Authentication auth, Model model) {

		String username = auth.getName();

		mgmt.setUpFarmingSiteManagement(username);

		model.addAttribute("infoMessage", INFO_MESSAGE);
		model.addAttribute("success", SUCCESS_MESSAGE);
		// reset the values after displaying
		INFO_MESSAGE = "";
		SUCCESS_MESSAGE = "";

		model.addAttribute("entries", mgmt.getUserSites());

		model.addAttribute("site", new SiteForm());

		return "farmerSites";
	}

	@GetMapping("/plantLayout")
	public String showPlantLayout(@ModelAttribute("site") SiteForm site, Model model) {
		String chosen = site.getChosenSite();
		if (chosen == null) {
			INFO_MESSAGE = "Please select a site to open its plant layout.";
			return "redirect:/sites";
		}
		int id = Integer.valueOf(chosen);
		String name = mgmt.getSiteName(id);
		model.addAttribute("siteName", name);
		model.addAttribute("plants", plantMgmt.getPlantSpecies());
		
		model.addAttribute("site", populatedLayout(mgmt.getPlantLayoutBySiteId(id), chosen));
		return "plantLayout";
	}

	@PostMapping("/changePlantLayout")
	public String saveChangesToPlantLayout(@ModelAttribute("site") SiteForm site, Model model) {
		String chosen = site.getChosenSite();
		List<PlantLayoutForm> list = site.getPlantLayout();
		updatePlantLayout(list, chosen);
		SUCCESS_MESSAGE = "Plant layout successfully adapted!";
		return "redirect:/sites";
	}

	private SiteForm populatedLayout(PlantLayout layout, String siteId) {
		SiteForm site = new SiteForm();
		site.setChosenSite(siteId);
		List<PlantLayoutForm> list = new ArrayList<PlantLayoutForm>();
		for (Integer i = 1; i <= 55; i++) {
			PlantLayoutForm pl = new PlantLayoutForm();
			pl.setPosition("" + i);
			String plant = layout.getMappings().get(i-1).getPlant();
			pl.setPlant(plant);
			list.add(pl);
		}
		site.setPlantLayout(list);
		return site;
	}
	
	private void updatePlantLayout(List<PlantLayoutForm> list, String siteId) {
		int id = Integer.valueOf(siteId);
		PlantLayout newPL = mgmt.getNewPlantLayout();
		
		for (Iterator<PlantLayoutForm> iterator = list.iterator(); iterator.hasNext();) {
			PlantLayoutForm plantLayoutForm = (PlantLayoutForm) iterator.next();
			PlantToPosMapping p2p = mgmt.getNewP2Pmapping();
			int pos = Integer.valueOf(plantLayoutForm.getPosition());	
			p2p.setPosition(pos);
			p2p.setPlant(plantLayoutForm.getPlant());
			newPL.getMappings().add(p2p);
		}
		
		mgmt.updatePlantLayout(id, newPL);
	}

}
