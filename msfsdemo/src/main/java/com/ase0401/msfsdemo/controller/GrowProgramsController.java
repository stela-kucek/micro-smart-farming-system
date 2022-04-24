package com.ase0401.msfsdemo.controller;

import java.io.File;
import java.util.ArrayList;

import org.eclipse.emf.common.util.URI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ase0401.msfsdemo.controller.form.GrowProgramForm;
import com.ase0401.msfsdemo.dsl.DslHandler;
import com.ase0401.msfsdemo.management.GrowProgramExecutionManager;
import com.ase0401.msfsdemo.management.GrowProgramManagement;

import msfs_0401.GrowProgram;

@Controller
public class GrowProgramsController {

	private static String INFO_MESSAGE = "";

	private static String SUCCESS_MESSAGE = "";

	@Autowired
	private GrowProgramManagement mgmt;

	@Autowired
	private DslHandler dslHandler;
	
	@Autowired
	GrowProgramExecutionManager gpManager;

	@GetMapping("/growPrograms")
	public String showGrowPrograms(Authentication auth, Model model) {

		String username = auth.getName();

		mgmt.setUpGrowProgramManagement(username);

		model.addAttribute("infoMessage", INFO_MESSAGE);
		model.addAttribute("success", SUCCESS_MESSAGE);
		// reset the values after displaying
		INFO_MESSAGE = "";
		SUCCESS_MESSAGE = "";
		model.addAttribute("selected", false);

		ArrayList<GrowProgram> list = mgmt.getGrowProgramsFromUser();
		gpManager.setUpForUser(username);
		ArrayList<GrowProgram> activeGPs = gpManager.getActiveGPs();
		
		model.addAttribute("activeGPs", activeGPs);
		model.addAttribute("entries", list);

		model.addAttribute("gpForm", new GrowProgramForm());

		return "growPrograms";
	}
	
	@PostMapping("/activateProgram")
	public String activateProgram(@ModelAttribute("gpForm") GrowProgramForm form, Model model) {

		int id = form.getChosenProgram();
		GrowProgram gp = mgmt.getGrowProgram(id);
		if(!gpManager.isActive(gp)) {
			gpManager.activateGP(gp);
			SUCCESS_MESSAGE = "Grow program '"+ gp.getName() + "' successfully activated!";
		}
		else {
			INFO_MESSAGE = "This grow program is already active.";
		}

		return "redirect:/growPrograms";
	}
	
	@PostMapping("/deactivateProgram")
	public String deactivateProgram(@ModelAttribute("gpForm") GrowProgramForm form, Model model) {

		int id = form.getChosenProgram();
		GrowProgram gp = mgmt.getGrowProgram(id);
		if(gpManager.isActive(gp)) {
			gpManager.deactivateGP(gp);
			SUCCESS_MESSAGE = "Grow program '"+ gp.getName() + "' successfully deactivated!";
		}
		else {
			INFO_MESSAGE = "This grow program is already inactive.";
		}

		return "redirect:/growPrograms";
	}

	@GetMapping("/createGrowProgram")
	public String showGrowProgramForm(Authentication auth, Model model) {
		model.addAttribute("gpForm", new GrowProgramForm());

		return "createGrowProgram";
	}

	@PostMapping("/createGrowProgram")
	public String createGrowProgram(@ModelAttribute("gpForm") GrowProgramForm form, Model model) {
		if (form.getName().isEmpty() || form.getPlantName().isEmpty()) {
			model.addAttribute("error",
					"Please specify the name of the program, and the plant you want to describe the treatment for.");
			model.addAttribute("gpForm", form);
			return "createGrowProgram";
		}

		mgmt.addGrowProgram(form.getName(), form.getPlantName(), form.getPrice(), form.isForSale(),
				mgmt.defineSeedTreatment(form.getSeedAt(), form.getSeedUnit(), form.getReplantAfter(),
						form.getReplantTimeUnit(), form.getSeedHumidValue(), form.getSeedAcidValue(),
						form.getSeedIrrigateHowMuch(), form.getSeedIrrigateEvery(), form.getSeedIrrigateTimeUnit(),
						form.getSeedFertilizer(), form.getSeedFertilizeHowMuch(), form.getSeedFertilizerUnit(),
						form.getSeedFertilizeEvery(), form.getSeedFertilizeTimeUnit(), form.getSeedFertilizerReps()),
				mgmt.defineSeedlingTreatment(form.getSeedlingHumidValue(), form.getSeedlingAcidValue(),
						form.getSeedlingIrrigateHowMuch(), form.getSeedlingIrrigateEvery(),
						form.getSeedlingIrrigateTimeUnit(), form.getSeedlingFertilizer(),
						form.getSeedlingFertilizeHowMuch(), form.getSeedlingFertilizerUnit(),
						form.getSeedlingFertilizeEvery(), form.getSeedlingFertilizeTimeUnit(),
						form.getSeedlingFertilizerReps()),
				mgmt.defineYoungTreatment(form.getYoungHumidValue(), form.getYoungAcidValue(),
						form.getYoungIrrigateHowMuch(), form.getYoungIrrigateEvery(), form.getYoungIrrigateTimeUnit(),
						form.getYoungFertilizer(), form.getYoungFertilizeHowMuch(), form.getYoungFertilizerUnit(),
						form.getYoungFertilizeEvery(), form.getYoungFertilizeTimeUnit(), form.getYoungFertilizerReps()),
				mgmt.defineAdultTreatment(form.getAdultHumidValue(), form.getAdultAcidValue(),
						form.getAdultIrrigateHowMuch(), form.getAdultIrrigateEvery(), form.getAdultIrrigateTimeUnit(),
						form.getAdultFertilizer(), form.getAdultFertilizeHowMuch(), form.getAdultFertilizerUnit(),
						form.getAdultFertilizeEvery(), form.getAdultFertilizeTimeUnit(), form.getAdultFertilizerReps()),
				mgmt.defineFruitTreatment(form.getHarvestMode()));

		SUCCESS_MESSAGE = "New grow program '" + form.getName() + "' successfully created!";

		return "redirect:/growPrograms";
	}

	@PostMapping("/deleteGrowProgram")
	public String deleteGrowProgram(@ModelAttribute("gpForm") GrowProgramForm form, Model model) {
		int id = form.getChosenProgram();

		mgmt.deleteGrowProgram(id);

		SUCCESS_MESSAGE = "Grow program successfully deleted!";

		return "redirect:/growPrograms";
	}

	@GetMapping("/editGrowProgram")
	public String showEditGrowProgramPage(@ModelAttribute("gpForm") GrowProgramForm form, Model model) {
		int id = form.getChosenProgram();
		String name = mgmt.getNameOfGrowProgram(id);
		model.addAttribute("gpName", name);

		GrowProgramForm editForm = populatedForm(mgmt.getGrowProgram(id));
		model.addAttribute("gpForm", editForm);

		model.addAttribute("gpId", id);

		return "editGrowProgram";
	}

	@PostMapping("/updateGrowProgram")
	public String updateGrowProgram(@ModelAttribute("gpForm") GrowProgramForm form, Model model) {
		if (form.getName().isEmpty() || form.getPlantName().isEmpty()) {
			model.addAttribute("error",
					"Please specify the name of the program, and the plant you want to describe the treatment for.");
			model.addAttribute("gpForm", form);
			return "createGrowProgram";
		}

		mgmt.updateGrowProgram(form.getChosenProgram(), form.getName(), form.getPlantName(), form.getPrice(),
				form.isForSale(),
				mgmt.defineSeedTreatment(form.getSeedAt(), form.getSeedUnit(), form.getReplantAfter(),
						form.getReplantTimeUnit(), form.getSeedHumidValue(), form.getSeedAcidValue(),
						form.getSeedIrrigateHowMuch(), form.getSeedIrrigateEvery(), form.getSeedIrrigateTimeUnit(),
						form.getSeedFertilizer(), form.getSeedFertilizeHowMuch(), form.getSeedFertilizerUnit(),
						form.getSeedFertilizeEvery(), form.getSeedFertilizeTimeUnit(), form.getSeedFertilizerReps()),
				mgmt.defineSeedlingTreatment(form.getSeedlingHumidValue(), form.getSeedlingAcidValue(),
						form.getSeedlingIrrigateHowMuch(), form.getSeedlingIrrigateEvery(),
						form.getSeedlingIrrigateTimeUnit(), form.getSeedlingFertilizer(),
						form.getSeedlingFertilizeHowMuch(), form.getSeedlingFertilizerUnit(),
						form.getSeedlingFertilizeEvery(), form.getSeedlingFertilizeTimeUnit(),
						form.getSeedlingFertilizerReps()),
				mgmt.defineYoungTreatment(form.getYoungHumidValue(), form.getYoungAcidValue(),
						form.getYoungIrrigateHowMuch(), form.getYoungIrrigateEvery(), form.getYoungIrrigateTimeUnit(),
						form.getYoungFertilizer(), form.getYoungFertilizeHowMuch(), form.getYoungFertilizerUnit(),
						form.getYoungFertilizeEvery(), form.getYoungFertilizeTimeUnit(), form.getYoungFertilizerReps()),
				mgmt.defineAdultTreatment(form.getAdultHumidValue(), form.getAdultAcidValue(),
						form.getAdultIrrigateHowMuch(), form.getAdultIrrigateEvery(), form.getAdultIrrigateTimeUnit(),
						form.getAdultFertilizer(), form.getAdultFertilizeHowMuch(), form.getAdultFertilizerUnit(),
						form.getAdultFertilizeEvery(), form.getAdultFertilizeTimeUnit(), form.getAdultFertilizerReps()),
				mgmt.defineFruitTreatment(form.getHarvestMode()));

		SUCCESS_MESSAGE = "Grow Program '" + form.getName() + "' successfully updated!";

		return "redirect:/growPrograms";
	}

	@GetMapping("/createDSLGrowProgram")
	public String showDSLInputForm(Model model) {

		model.addAttribute("input", new GrowProgramForm());

		return "createGrowProgramDSL";
	}

	@PostMapping("/createDSLGrowProgram")
	public String tryToAddGrowProgram(@ModelAttribute("input") GrowProgramForm input, Model model) {
		System.out.println("INPUT IS: " + input.getInputDSL());
		dslHandler.setUpHandler();
		dslHandler.generateDslFromString(input.getInputDSL());
		dslHandler.setResourceFromFile(URI.createFileURI("." + File.separator + "input.msfsdsl"));
		if (dslHandler.isModelValid()) {
			dslHandler.addGrowProgramFromDSL();
			SUCCESS_MESSAGE = "New grow program successfully created!";

			return "redirect:/growPrograms";
		}
		model.addAttribute("error", "Your input seems to be invalid. Please review it and try again.");
		model.addAttribute("input", input);

		return "createGrowProgramDSL";
	}

	private GrowProgramForm populatedForm(GrowProgram p) {
		GrowProgramForm form = new GrowProgramForm();

		form.setName(p.getName());
		form.setPlantName(p.getPlantSpecies());
		form.setPrice(p.getPrice());
		form.setForSale(p.isForSale());

		if (p.getSeedTreatment() != null) {
			if (p.getSeedTreatment().getSeeding() != null) {
				form.setSeedAt(p.getSeedTreatment().getSeeding().getSpec().getValue());
				form.setSeedUnit(p.getSeedTreatment().getSeeding().getSpec().getUnit());
			}
			if (p.getSeedTreatment().getReplanting() != null) {
				form.setReplantAfter(p.getSeedTreatment().getReplanting().getSpec().getValue());
				form.setReplantTimeUnit(p.getSeedTreatment().getReplanting().getSpec().getUnit());
			}

			form.setSeedIrrigateHowMuch(p.getSeedTreatment().getIrrigating().getSpec().getValue());
			form.setSeedIrrigateEvery(p.getSeedTreatment().getIrrigating().getPeriod().getValue());
			form.setSeedIrrigateTimeUnit(p.getSeedTreatment().getIrrigating().getPeriod().getUnit());
			form.setSeedFertilizer(p.getSeedTreatment().getFertilizing().getFertilizer());
			form.setSeedFertilizeHowMuch(p.getSeedTreatment().getFertilizing().getSpec().getValue());
			form.setSeedFertilizerUnit(p.getSeedTreatment().getFertilizing().getSpec().getUnit());
			form.setSeedFertilizeEvery(p.getSeedTreatment().getFertilizing().getPeriod().getValue());
			form.setSeedFertilizeTimeUnit(p.getSeedTreatment().getFertilizing().getPeriod().getUnit());
			form.setSeedFertilizerReps(p.getSeedTreatment().getFertilizing().getRepetitions());
			form.setSeedHumidValue(p.getSeedTreatment().getSoilHumidity().getSpec().getValue());
			form.setSeedAcidValue(p.getSeedTreatment().getSoilAcidity().getSpec().getValue());
		}

		if (p.getSeedlingTreatment() != null) {
			form.setSeedlingIrrigateHowMuch(p.getSeedlingTreatment().getIrrigating().getSpec().getValue());
			form.setSeedlingIrrigateEvery(p.getSeedlingTreatment().getIrrigating().getPeriod().getValue());
			form.setSeedlingIrrigateTimeUnit(p.getSeedlingTreatment().getIrrigating().getPeriod().getUnit());
			form.setSeedlingFertilizer(p.getSeedlingTreatment().getFertilizing().getFertilizer());
			form.setSeedlingFertilizeHowMuch(p.getSeedlingTreatment().getFertilizing().getSpec().getValue());
			form.setSeedlingFertilizerUnit(p.getSeedlingTreatment().getFertilizing().getSpec().getUnit());
			form.setSeedlingFertilizeEvery(p.getSeedlingTreatment().getFertilizing().getPeriod().getValue());
			form.setSeedlingFertilizeTimeUnit(p.getSeedlingTreatment().getFertilizing().getPeriod().getUnit());
			form.setSeedlingFertilizerReps(p.getSeedlingTreatment().getFertilizing().getRepetitions());
			form.setSeedlingHumidValue(p.getSeedlingTreatment().getSoilHumidity().getSpec().getValue());
			form.setSeedlingAcidValue(p.getSeedlingTreatment().getSoilAcidity().getSpec().getValue());
		}

		if (p.getYoungTreatment() != null) {
			form.setYoungIrrigateHowMuch(p.getYoungTreatment().getIrrigating().getSpec().getValue());
			form.setYoungIrrigateEvery(p.getYoungTreatment().getIrrigating().getPeriod().getValue());
			form.setYoungIrrigateTimeUnit(p.getYoungTreatment().getIrrigating().getPeriod().getUnit());
			form.setYoungFertilizer(p.getYoungTreatment().getFertilizing().getFertilizer());
			form.setYoungFertilizeHowMuch(p.getYoungTreatment().getFertilizing().getSpec().getValue());
			form.setYoungFertilizerUnit(p.getYoungTreatment().getFertilizing().getSpec().getUnit());
			form.setYoungFertilizeEvery(p.getYoungTreatment().getFertilizing().getPeriod().getValue());
			form.setYoungFertilizeTimeUnit(p.getYoungTreatment().getFertilizing().getPeriod().getUnit());
			form.setYoungFertilizerReps(p.getYoungTreatment().getFertilizing().getRepetitions());
			form.setYoungHumidValue(p.getYoungTreatment().getSoilHumidity().getSpec().getValue());
			form.setYoungAcidValue(p.getYoungTreatment().getSoilAcidity().getSpec().getValue());
		}

		if (p.getAdultTreatment() != null) {
			form.setAdultIrrigateHowMuch(p.getAdultTreatment().getIrrigating().getSpec().getValue());
			form.setAdultIrrigateEvery(p.getAdultTreatment().getIrrigating().getPeriod().getValue());
			form.setAdultIrrigateTimeUnit(p.getAdultTreatment().getIrrigating().getPeriod().getUnit());
			form.setAdultFertilizer(p.getAdultTreatment().getFertilizing().getFertilizer());
			form.setAdultFertilizeHowMuch(p.getAdultTreatment().getFertilizing().getSpec().getValue());
			form.setAdultFertilizerUnit(p.getAdultTreatment().getFertilizing().getSpec().getUnit());
			form.setAdultFertilizeEvery(p.getAdultTreatment().getFertilizing().getPeriod().getValue());
			form.setAdultFertilizeTimeUnit(p.getAdultTreatment().getFertilizing().getPeriod().getUnit());
			form.setAdultFertilizerReps(p.getAdultTreatment().getFertilizing().getRepetitions());
			form.setAdultHumidValue(p.getAdultTreatment().getSoilHumidity().getSpec().getValue());
			form.setAdultAcidValue(p.getAdultTreatment().getSoilAcidity().getSpec().getValue());
		}

		if (p.getFruitTreatment() != null) {
			form.setHarvestMode(p.getFruitTreatment().getHarvesting().getMode().getName());
		}

		return form;
	}

}
