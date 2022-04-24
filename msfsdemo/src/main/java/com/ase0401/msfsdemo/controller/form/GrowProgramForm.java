/**
 * 
 */
package com.ase0401.msfsdemo.controller.form;

/**
 * @author stela
 *
 */
public class GrowProgramForm {
	
	// for user input as DSL 
	String inputDSL;

	public String getInputDSL() {
		return inputDSL;
	}

	public void setInputDSL(String inputDSL) {
		this.inputDSL = inputDSL;
	}

	// for selecting an existing program
	int chosenProgram;
	/*
	 * General GrowProgramVariables
	 */
	String name;
	String plantName;
	double price;
	boolean forSale;
	
	/*
	 * SeedVariables
	 */
	// seeding
	double seedAt;
	String seedUnit;
	// replanting
	double replantAfter;
	String replantTimeUnit;
	// irrigating
	double seedIrrigateHowMuch;
	double seedIrrigateEvery;
	String seedIrrigateTimeUnit;
	// fertilizing
	String seedFertilizer;
	double seedFertilizeHowMuch;
	String seedFertilizerUnit; 
	double seedFertilizeEvery;
	String seedFertilizeTimeUnit;
	int seedFertilizerReps;
	// conditions
	double seedHumidValue;
	double seedAcidValue;
	
	/*
	 * SeedlingVariables
	 */
	// irrigating
	double seedlingIrrigateHowMuch;
	double seedlingIrrigateEvery;
	String seedlingIrrigateTimeUnit;
	// fertilizing
	String seedlingFertilizer;
	double seedlingFertilizeHowMuch;
	String seedlingFertilizerUnit; 
	double seedlingFertilizeEvery;
	String seedlingFertilizeTimeUnit;
	int seedlingFertilizerReps;
	// conditions
	double seedlingHumidValue;
	double seedlingAcidValue;
	
	
	/*
	 * YoungVariables
	 */
	// irrigating
	double youngIrrigateHowMuch;
	double youngIrrigateEvery;
	String youngIrrigateTimeUnit;
	// fertilizing
	String youngFertilizer;
	double youngFertilizeHowMuch;
	String youngFertilizerUnit; 
	double youngFertilizeEvery;
	String youngFertilizeTimeUnit;
	int youngFertilizerReps;
	// conditions
	double youngHumidValue;
	double youngAcidValue;
	
	/*
	 * AdultVariables
	 */
	// irrigating
	double adultIrrigateHowMuch;
	double adultIrrigateEvery;
	String adultIrrigateTimeUnit;
	// fertilizing
	String adultFertilizer;
	double adultFertilizeHowMuch;
	String adultFertilizerUnit; 
	double adultFertilizeEvery;
	String adultFertilizeTimeUnit;
	int adultFertilizerReps;
	// conditions
	double adultHumidValue;
	double adultAcidValue;
	
	/*
	 * FruitVariable
	 */
	String harvestMode;
	
	/*
	 * Getters and Setters
	 */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlantName() {
		return plantName;
	}

	public void setPlantName(String plantName) {
		this.plantName = plantName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public boolean isForSale() {
		return forSale;
	}

	public void setForSale(boolean forSale) {
		this.forSale = forSale;
	}

	public double getSeedAt() {
		return seedAt;
	}

	public void setSeedAt(double seedAt) {
		this.seedAt = seedAt;
	}

	public String getSeedUnit() {
		return seedUnit;
	}

	public void setSeedUnit(String seedUnit) {
		this.seedUnit = seedUnit;
	}

	public double getReplantAfter() {
		return replantAfter;
	}

	public void setReplantAfter(double replantAfter) {
		this.replantAfter = replantAfter;
	}

	public String getReplantTimeUnit() {
		return replantTimeUnit;
	}

	public void setReplantTimeUnit(String replantTimeUnit) {
		this.replantTimeUnit = replantTimeUnit;
	}

	public double getSeedIrrigateHowMuch() {
		return seedIrrigateHowMuch;
	}

	public void setSeedIrrigateHowMuch(double seedIrrigateHowMuch) {
		this.seedIrrigateHowMuch = seedIrrigateHowMuch;
	}

	public double getSeedIrrigateEvery() {
		return seedIrrigateEvery;
	}

	public void setSeedIrrigateEvery(double seedIrrigateEvery) {
		this.seedIrrigateEvery = seedIrrigateEvery;
	}

	public String getSeedIrrigateTimeUnit() {
		return seedIrrigateTimeUnit;
	}

	public void setSeedIrrigateTimeUnit(String seedIrrigateTimeUnit) {
		this.seedIrrigateTimeUnit = seedIrrigateTimeUnit;
	}

	public String getSeedFertilizer() {
		return seedFertilizer;
	}

	public void setSeedFertilizer(String seedFertilizer) {
		this.seedFertilizer = seedFertilizer;
	}

	public double getSeedFertilizeHowMuch() {
		return seedFertilizeHowMuch;
	}

	public void setSeedFertilizeHowMuch(double seedFertilizeHowMuch) {
		this.seedFertilizeHowMuch = seedFertilizeHowMuch;
	}

	public String getSeedFertilizerUnit() {
		return seedFertilizerUnit;
	}

	public void setSeedFertilizerUnit(String seedFertilizerUnit) {
		this.seedFertilizerUnit = seedFertilizerUnit;
	}

	public double getSeedFertilizeEvery() {
		return seedFertilizeEvery;
	}

	public void setSeedFertilizeEvery(double seedFertilizeEvery) {
		this.seedFertilizeEvery = seedFertilizeEvery;
	}

	public String getSeedFertilizeTimeUnit() {
		return seedFertilizeTimeUnit;
	}

	public void setSeedFertilizeTimeUnit(String seedFertilizeTimeUnit) {
		this.seedFertilizeTimeUnit = seedFertilizeTimeUnit;
	}

	public int getSeedFertilizerReps() {
		return seedFertilizerReps;
	}

	public void setSeedFertilizerReps(int seedFertilizerReps) {
		this.seedFertilizerReps = seedFertilizerReps;
	}

	public double getSeedHumidValue() {
		return seedHumidValue;
	}

	public void setSeedHumidValue(double seedHumidValue) {
		this.seedHumidValue = seedHumidValue;
	}

	public double getSeedAcidValue() {
		return seedAcidValue;
	}

	public void setSeedAcidValue(double seedAcidValue) {
		this.seedAcidValue = seedAcidValue;
	}

	public double getSeedlingIrrigateHowMuch() {
		return seedlingIrrigateHowMuch;
	}

	public void setSeedlingIrrigateHowMuch(double seedlingIrrigateHowMuch) {
		this.seedlingIrrigateHowMuch = seedlingIrrigateHowMuch;
	}

	public double getSeedlingIrrigateEvery() {
		return seedlingIrrigateEvery;
	}

	public void setSeedlingIrrigateEvery(double seedlingIrrigateEvery) {
		this.seedlingIrrigateEvery = seedlingIrrigateEvery;
	}

	public String getSeedlingIrrigateTimeUnit() {
		return seedlingIrrigateTimeUnit;
	}

	public void setSeedlingIrrigateTimeUnit(String seedlingIrrigateTimeUnit) {
		this.seedlingIrrigateTimeUnit = seedlingIrrigateTimeUnit;
	}

	public String getSeedlingFertilizer() {
		return seedlingFertilizer;
	}

	public void setSeedlingFertilizer(String seedlingFertilizer) {
		this.seedlingFertilizer = seedlingFertilizer;
	}

	public double getSeedlingFertilizeHowMuch() {
		return seedlingFertilizeHowMuch;
	}

	public void setSeedlingFertilizeHowMuch(double seedlingFertilizeHowMuch) {
		this.seedlingFertilizeHowMuch = seedlingFertilizeHowMuch;
	}

	public String getSeedlingFertilizerUnit() {
		return seedlingFertilizerUnit;
	}

	public void setSeedlingFertilizerUnit(String seedlingFertilizerUnit) {
		this.seedlingFertilizerUnit = seedlingFertilizerUnit;
	}

	public double getSeedlingFertilizeEvery() {
		return seedlingFertilizeEvery;
	}

	public void setSeedlingFertilizeEvery(double seedlingFertilizeEvery) {
		this.seedlingFertilizeEvery = seedlingFertilizeEvery;
	}

	public String getSeedlingFertilizeTimeUnit() {
		return seedlingFertilizeTimeUnit;
	}

	public void setSeedlingFertilizeTimeUnit(String seedlingFertilizeTimeUnit) {
		this.seedlingFertilizeTimeUnit = seedlingFertilizeTimeUnit;
	}

	public int getSeedlingFertilizerReps() {
		return seedlingFertilizerReps;
	}

	public void setSeedlingFertilizerReps(int seedlingFertilizerReps) {
		this.seedlingFertilizerReps = seedlingFertilizerReps;
	}

	public double getSeedlingHumidValue() {
		return seedlingHumidValue;
	}

	public void setSeedlingHumidValue(double seedlingHumidValue) {
		this.seedlingHumidValue = seedlingHumidValue;
	}

	public double getSeedlingAcidValue() {
		return seedlingAcidValue;
	}

	public void setSeedlingAcidValue(double seedlingAcidValue) {
		this.seedlingAcidValue = seedlingAcidValue;
	}

	public double getYoungIrrigateHowMuch() {
		return youngIrrigateHowMuch;
	}

	public void setYoungIrrigateHowMuch(double youngIrrigateHowMuch) {
		this.youngIrrigateHowMuch = youngIrrigateHowMuch;
	}

	public double getYoungIrrigateEvery() {
		return youngIrrigateEvery;
	}

	public void setYoungIrrigateEvery(double youngIrrigateEvery) {
		this.youngIrrigateEvery = youngIrrigateEvery;
	}

	public String getYoungIrrigateTimeUnit() {
		return youngIrrigateTimeUnit;
	}

	public void setYoungIrrigateTimeUnit(String youngIrrigateTimeUnit) {
		this.youngIrrigateTimeUnit = youngIrrigateTimeUnit;
	}

	public String getYoungFertilizer() {
		return youngFertilizer;
	}

	public void setYoungFertilizer(String youngFertilizer) {
		this.youngFertilizer = youngFertilizer;
	}

	public double getYoungFertilizeHowMuch() {
		return youngFertilizeHowMuch;
	}

	public void setYoungFertilizeHowMuch(double youngFertilizeHowMuch) {
		this.youngFertilizeHowMuch = youngFertilizeHowMuch;
	}

	public String getYoungFertilizerUnit() {
		return youngFertilizerUnit;
	}

	public void setYoungFertilizerUnit(String youngFertilizerUnit) {
		this.youngFertilizerUnit = youngFertilizerUnit;
	}

	public double getYoungFertilizeEvery() {
		return youngFertilizeEvery;
	}

	public void setYoungFertilizeEvery(double youngFertilizeEvery) {
		this.youngFertilizeEvery = youngFertilizeEvery;
	}

	public String getYoungFertilizeTimeUnit() {
		return youngFertilizeTimeUnit;
	}

	public void setYoungFertilizeTimeUnit(String youngFertilizeTimeUnit) {
		this.youngFertilizeTimeUnit = youngFertilizeTimeUnit;
	}

	public int getYoungFertilizerReps() {
		return youngFertilizerReps;
	}

	public void setYoungFertilizerReps(int youngFertilizerReps) {
		this.youngFertilizerReps = youngFertilizerReps;
	}

	public double getYoungHumidValue() {
		return youngHumidValue;
	}

	public void setYoungHumidValue(double youngHumidValue) {
		this.youngHumidValue = youngHumidValue;
	}

	public double getYoungAcidValue() {
		return youngAcidValue;
	}

	public void setYoungAcidValue(double youngAcidValue) {
		this.youngAcidValue = youngAcidValue;
	}

	public double getAdultIrrigateHowMuch() {
		return adultIrrigateHowMuch;
	}

	public void setAdultIrrigateHowMuch(double adultIrrigateHowMuch) {
		this.adultIrrigateHowMuch = adultIrrigateHowMuch;
	}

	public double getAdultIrrigateEvery() {
		return adultIrrigateEvery;
	}

	public void setAdultIrrigateEvery(double adultIrrigateEvery) {
		this.adultIrrigateEvery = adultIrrigateEvery;
	}

	public String getAdultIrrigateTimeUnit() {
		return adultIrrigateTimeUnit;
	}

	public void setAdultIrrigateTimeUnit(String adultIrrigateTimeUnit) {
		this.adultIrrigateTimeUnit = adultIrrigateTimeUnit;
	}

	public String getAdultFertilizer() {
		return adultFertilizer;
	}

	public void setAdultFertilizer(String adultFertilizer) {
		this.adultFertilizer = adultFertilizer;
	}

	public double getAdultFertilizeHowMuch() {
		return adultFertilizeHowMuch;
	}

	public void setAdultFertilizeHowMuch(double adultFertilizeHowMuch) {
		this.adultFertilizeHowMuch = adultFertilizeHowMuch;
	}

	public String getAdultFertilizerUnit() {
		return adultFertilizerUnit;
	}

	public void setAdultFertilizerUnit(String adultFertilizerUnit) {
		this.adultFertilizerUnit = adultFertilizerUnit;
	}

	public double getAdultFertilizeEvery() {
		return adultFertilizeEvery;
	}

	public void setAdultFertilizeEvery(double adultFertilizeEvery) {
		this.adultFertilizeEvery = adultFertilizeEvery;
	}

	public String getAdultFertilizeTimeUnit() {
		return adultFertilizeTimeUnit;
	}

	public void setAdultFertilizeTimeUnit(String adultFertilizeTimeUnit) {
		this.adultFertilizeTimeUnit = adultFertilizeTimeUnit;
	}

	public int getAdultFertilizerReps() {
		return adultFertilizerReps;
	}

	public void setAdultFertilizerReps(int adultFertilizerReps) {
		this.adultFertilizerReps = adultFertilizerReps;
	}

	public double getAdultHumidValue() {
		return adultHumidValue;
	}

	public void setAdultHumidValue(double adultHumidValue) {
		this.adultHumidValue = adultHumidValue;
	}

	public double getAdultAcidValue() {
		return adultAcidValue;
	}

	public void setAdultAcidValue(double adultAcidValue) {
		this.adultAcidValue = adultAcidValue;
	}

	public String getHarvestMode() {
		return harvestMode;
	}

	public void setHarvestMode(String harvestMode) {
		this.harvestMode = harvestMode;
	}

	public int getChosenProgram() {
		return chosenProgram;
	}

	public void setChosenProgram(int chosenProgram) {
		this.chosenProgram = chosenProgram;
	}
}
