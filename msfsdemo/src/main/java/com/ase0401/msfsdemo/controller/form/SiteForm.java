package com.ase0401.msfsdemo.controller.form;

import java.util.List;

public class SiteForm {
	
	String chosenSite;
	List<PlantLayoutForm> plantLayout;

	public SiteForm() {}

	public String getChosenSite() {
		return chosenSite;
	}

	public void setChosenSite(String chosenSite) {
		this.chosenSite = chosenSite;
	}

	public List<PlantLayoutForm> getPlantLayout() {
		return plantLayout;
	}

	public void setPlantLayout(List<PlantLayoutForm> plantLayout) {
		this.plantLayout = plantLayout;
	}

}
