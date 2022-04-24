/**
 * 
 */
package com.ase0401.msfsdemo.management;

import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ase0401.msfsdemo.factory.ModelFactory;
import com.ase0401.msfsdemo.repository.GrowProgramRepository;

import msfs_0401.AdultTreatment;
import msfs_0401.Amount;
import msfs_0401.Fertilizing;
import msfs_0401.FruitTreatment;
import msfs_0401.GrowProgram;
import msfs_0401.HarvestMode;
import msfs_0401.Harvesting;
import msfs_0401.Irrigating;
import msfs_0401.Replanting;
import msfs_0401.SeedTreatment;
import msfs_0401.Seeding;
import msfs_0401.SeedlingTreatment;
import msfs_0401.SoilAcidity;
import msfs_0401.SoilHumidity;
import msfs_0401.YoungTreatment;

/**
 * @author stela
 *
 */
@Component
public class GrowProgramManagement {

	@Autowired
	private ModelFactory factory;

	@Autowired
	private GrowProgramRepository gpRepo;

	private String farmerUsername;

	public void setUpGrowProgramManagement(String username) {
		farmerUsername = username;
	}

	public ArrayList<GrowProgram> getGrowProgramsFromUser() {
		ArrayList<GrowProgram> resultList = new ArrayList<>();
		ArrayList<GrowProgram> allGPs = gpRepo.getGrowPrograms();
		for (Iterator<GrowProgram> iterator = allGPs.iterator(); iterator.hasNext();) {
			GrowProgram growProgram = (GrowProgram) iterator.next();
			if (growProgram.getCreator().equals(farmerUsername))
				resultList.add(growProgram);
		}
		return resultList;
	}

	public void updateGrowProgram(int id, String name, String plantName, double price, boolean forSale,
			SeedTreatment seedTreatment, SeedlingTreatment seedlingTreatment, YoungTreatment youngTreatment,
			AdultTreatment adultTreatment, FruitTreatment fruitTreatment) {

		GrowProgram toUpdate = gpRepo.getGrowProgramById(id);

		toUpdate.setName(name);

		toUpdate.setPlantSpecies(plantName);

		toUpdate.setPrice(price);

		toUpdate.setForSale(forSale);

		// add treatments specification
		toUpdate.setSeedTreatment(seedTreatment);
		toUpdate.setSeedlingTreatment(seedlingTreatment);
		toUpdate.setYoungTreatment(youngTreatment);
		toUpdate.setAdultTreatment(adultTreatment);
		toUpdate.setFruitTreatment(fruitTreatment);

		toUpdate.setCreator(farmerUsername);

		gpRepo.saveData(toUpdate);
	}

	public void addGrowProgram(String name, String plantName, double price, boolean forSale,
			SeedTreatment seedTreatment, SeedlingTreatment seedlingTreatment, YoungTreatment youngTreatment,
			AdultTreatment adultTreatment, FruitTreatment fruitTreatment) {

		GrowProgram gp = factory.createGrowProgram();

		int newId = gpRepo.getNextId();
		while (idExists(newId)) {
			newId += 1;
		}
		gp.setId(newId);

		gp.setName(name);

		gp.setPlantSpecies(plantName);

		gp.setPrice(price);

		gp.setForSale(forSale);

		// add treatments specification
		gp.setSeedTreatment(seedTreatment);
		gp.setSeedlingTreatment(seedlingTreatment);
		gp.setYoungTreatment(youngTreatment);
		gp.setAdultTreatment(adultTreatment);
		gp.setFruitTreatment(fruitTreatment);

		gp.setCreator(farmerUsername);

		gpRepo.addGrowProgram(gp);

	}

	public boolean idExists(int id) {
		ArrayList<GrowProgram> list = gpRepo.getGrowPrograms();
		for (Iterator<GrowProgram> iterator = list.iterator(); iterator.hasNext();) {
			GrowProgram gp = (GrowProgram) iterator.next();
			if (gp.getId() == id)
				return true;
		}
		return false;
	}

	public void deleteGrowProgram(int id) {
		gpRepo.deleteGrowProgramById(id);
	}

	public String getNameOfGrowProgram(int id) {
		return gpRepo.getGrowProgramById(id).getName();
	}

	public GrowProgram getNewGrowProgram() {
		return factory.createGrowProgram();
	}

	public GrowProgram getGrowProgram(int id) {
		return gpRepo.getGrowProgramById(id);
	}

	public void updateGrowProgram(GrowProgram gp) {
		gpRepo.saveData(gp);
	}

	public SeedTreatment defineSeedTreatment(double seedAt, String seedUnit, double replantAfter,
			String replantTimeUnit, double humidValue, double acidValue, double irrigateHowMuch, double irrigateEvery,
			String irrigateTimeUnit, String fertilizer, double fertilizeHowMuch, String fertilizerUnit,
			double fertilizeEvery, String fertilizeTimeUnit, int reps) {

		SeedTreatment treatment = factory.createSeedTreatment();

		// set specific actions
		if (Double.valueOf(seedAt) != null && seedUnit != null) {
			Seeding seeding = (Seeding) factory.createAction("seeding");
			Amount amtSeeding = (Amount) factory.createAmount();
			amtSeeding.setValue(seedAt);
			amtSeeding.setUnit(seedUnit);
			seeding.setSpec(amtSeeding);
			treatment.setSeeding(seeding);
		}

		if (Double.valueOf(replantAfter) != null && replantTimeUnit != null) {
			Replanting replanting = (Replanting) factory.createAction("replanting");
			Amount amtReplanting = (Amount) factory.createAmount();
			amtReplanting.setValue(replantAfter);
			amtReplanting.setUnit(replantTimeUnit);
			replanting.setSpec(amtReplanting);
			treatment.setReplanting(replanting);
		}

		// set common actions
		// irrigation
		Irrigating irrigating = (Irrigating) factory.createAction("irrigating");
		Amount amtIrrigating = (Amount) factory.createAmount();
		amtIrrigating.setValue(irrigateHowMuch);
		amtIrrigating.setUnit("L");
		irrigating.setSpec(amtIrrigating);

		Amount amtIrrigating2 = (Amount) factory.createAmount();
		amtIrrigating2.setValue(irrigateEvery);
		amtIrrigating2.setUnit(irrigateTimeUnit);
		irrigating.setPeriod(amtIrrigating2);
		treatment.setIrrigating(irrigating);

		// fertilizing
		Fertilizing fertilizing = (Fertilizing) factory.createAction("fertilizing");
		Amount amtFertilizing = (Amount) factory.createAmount();
		amtFertilizing.setValue(fertilizeHowMuch);
		amtFertilizing.setUnit(fertilizerUnit);
		fertilizing.setSpec(amtFertilizing);

		Amount amtFertilizing2 = (Amount) factory.createAmount();
		amtFertilizing2.setValue(fertilizeEvery);
		amtFertilizing2.setUnit(fertilizeTimeUnit);
		fertilizing.setPeriod(amtFertilizing2);

		fertilizing.setFertilizer(fertilizer);
		fertilizing.setRepetitions(reps);
		treatment.setFertilizing(fertilizing);

		// set conditions
		SoilHumidity humidity = (SoilHumidity) factory.createCondition("humidity");
		Amount amtHumidity = (Amount) factory.createAmount();
		amtHumidity.setValue(humidValue);
		amtHumidity.setUnit("%");
		humidity.setSpec(amtHumidity);
		treatment.setSoilHumidity(humidity);

		SoilAcidity acidity = (SoilAcidity) factory.createCondition("acidity");
		Amount amtAcidity = (Amount) factory.createAmount();
		amtAcidity.setValue(acidValue);
		amtAcidity.setUnit("pH");
		acidity.setSpec(amtAcidity);
		treatment.setSoilAcidity(acidity);

		return treatment;
	}

	public SeedlingTreatment defineSeedlingTreatment(double humidValue, double acidValue, double irrigateHowMuch,
			double irrigateEvery, String irrigateTimeUnit, String fertilizer, double fertilizeHowMuch,
			String fertilizerUnit, double fertilizeEvery, String fertilizeTimeUnit, int reps) {
		SeedlingTreatment treatment = factory.createSeedlingTreatment();

		// set common actions
		// irrigation
		Irrigating irrigating = (Irrigating) factory.createAction("irrigating");
		Amount amtIrrigating = (Amount) factory.createAmount();
		amtIrrigating.setValue(irrigateHowMuch);
		amtIrrigating.setUnit("L");
		irrigating.setSpec(amtIrrigating);

		Amount amtIrrigating2 = (Amount) factory.createAmount();
		amtIrrigating2.setValue(irrigateEvery);
		amtIrrigating2.setUnit(irrigateTimeUnit);
		irrigating.setPeriod(amtIrrigating2);
		treatment.setIrrigating(irrigating);

		// fertilizing
		Fertilizing fertilizing = (Fertilizing) factory.createAction("fertilizing");
		Amount amtFertilizing = (Amount) factory.createAmount();
		amtFertilizing.setValue(fertilizeHowMuch);
		amtFertilizing.setUnit(fertilizerUnit);
		fertilizing.setSpec(amtFertilizing);

		Amount amtFertilizing2 = (Amount) factory.createAmount();
		amtFertilizing2.setValue(fertilizeEvery);
		amtFertilizing2.setUnit(fertilizeTimeUnit);
		fertilizing.setPeriod(amtFertilizing2);

		fertilizing.setFertilizer(fertilizer);
		fertilizing.setRepetitions(reps);
		treatment.setFertilizing(fertilizing);

		// set conditions
		SoilHumidity humidity = (SoilHumidity) factory.createCondition("humidity");
		Amount amtHumidity = (Amount) factory.createAmount();
		amtHumidity.setValue(humidValue);
		amtHumidity.setUnit("%");
		humidity.setSpec(amtHumidity);
		treatment.setSoilHumidity(humidity);

		SoilAcidity acidity = (SoilAcidity) factory.createCondition("acidity");
		Amount amtAcidity = (Amount) factory.createAmount();
		amtAcidity.setValue(acidValue);
		amtAcidity.setUnit("pH");
		acidity.setSpec(amtAcidity);
		treatment.setSoilAcidity(acidity);

		return treatment;
	}

	public YoungTreatment defineYoungTreatment(double humidValue, double acidValue, double irrigateHowMuch,
			double irrigateEvery, String irrigateTimeUnit, String fertilizer, double fertilizeHowMuch,
			String fertilizerUnit, double fertilizeEvery, String fertilizeTimeUnit, int reps) {
		YoungTreatment treatment = factory.createYoungTreatment();

		// set common actions
		// irrigation
		Irrigating irrigating = (Irrigating) factory.createAction("irrigating");
		Amount amtIrrigating = (Amount) factory.createAmount();
		amtIrrigating.setValue(irrigateHowMuch);
		amtIrrigating.setUnit("L");
		irrigating.setSpec(amtIrrigating);

		Amount amtIrrigating2 = (Amount) factory.createAmount();
		amtIrrigating2.setValue(irrigateEvery);
		amtIrrigating2.setUnit(irrigateTimeUnit);
		irrigating.setPeriod(amtIrrigating2);
		treatment.setIrrigating(irrigating);

		// fertilizing
		Fertilizing fertilizing = (Fertilizing) factory.createAction("fertilizing");
		Amount amtFertilizing = (Amount) factory.createAmount();
		amtFertilizing.setValue(fertilizeHowMuch);
		amtFertilizing.setUnit(fertilizerUnit);
		fertilizing.setSpec(amtFertilizing);

		Amount amtFertilizing2 = (Amount) factory.createAmount();
		amtFertilizing2.setValue(fertilizeEvery);
		amtFertilizing2.setUnit(fertilizeTimeUnit);
		fertilizing.setPeriod(amtFertilizing2);

		fertilizing.setFertilizer(fertilizer);
		fertilizing.setRepetitions(reps);
		treatment.setFertilizing(fertilizing);

		// set conditions
		SoilHumidity humidity = (SoilHumidity) factory.createCondition("humidity");
		Amount amtHumidity = (Amount) factory.createAmount();
		amtHumidity.setValue(humidValue);
		amtHumidity.setUnit("%");
		humidity.setSpec(amtHumidity);
		treatment.setSoilHumidity(humidity);

		SoilAcidity acidity = (SoilAcidity) factory.createCondition("acidity");
		Amount amtAcidity = (Amount) factory.createAmount();
		amtAcidity.setValue(acidValue);
		amtAcidity.setUnit("pH");
		acidity.setSpec(amtAcidity);
		treatment.setSoilAcidity(acidity);

		return treatment;
	}

	public AdultTreatment defineAdultTreatment(double humidValue, double acidValue, double irrigateHowMuch,
			double irrigateEvery, String irrigateTimeUnit, String fertilizer, double fertilizeHowMuch,
			String fertilizerUnit, double fertilizeEvery, String fertilizeTimeUnit, int reps) {
		AdultTreatment treatment = factory.createAdultTreatment();

		// set common actions
		// irrigation
		Irrigating irrigating = (Irrigating) factory.createAction("irrigating");
		Amount amtIrrigating = (Amount) factory.createAmount();
		amtIrrigating.setValue(irrigateHowMuch);
		amtIrrigating.setUnit("L");
		irrigating.setSpec(amtIrrigating);

		Amount amtIrrigating2 = (Amount) factory.createAmount();
		amtIrrigating2.setValue(irrigateEvery);
		amtIrrigating2.setUnit(irrigateTimeUnit);
		irrigating.setPeriod(amtIrrigating2);
		treatment.setIrrigating(irrigating);

		// fertilizing
		Fertilizing fertilizing = (Fertilizing) factory.createAction("fertilizing");
		Amount amtFertilizing = (Amount) factory.createAmount();
		amtFertilizing.setValue(fertilizeHowMuch);
		amtFertilizing.setUnit(fertilizerUnit);
		fertilizing.setSpec(amtFertilizing);

		Amount amtFertilizing2 = (Amount) factory.createAmount();
		amtFertilizing2.setValue(fertilizeEvery);
		amtFertilizing2.setUnit(fertilizeTimeUnit);
		fertilizing.setPeriod(amtFertilizing2);

		fertilizing.setFertilizer(fertilizer);
		fertilizing.setRepetitions(reps);
		treatment.setFertilizing(fertilizing);

		// set conditions
		SoilHumidity humidity = (SoilHumidity) factory.createCondition("humidity");
		Amount amtHumidity = (Amount) factory.createAmount();
		amtHumidity.setValue(humidValue);
		amtHumidity.setUnit("%");
		humidity.setSpec(amtHumidity);
		treatment.setSoilHumidity(humidity);

		SoilAcidity acidity = (SoilAcidity) factory.createCondition("acidity");
		Amount amtAcidity = (Amount) factory.createAmount();
		amtAcidity.setValue(acidValue);
		amtAcidity.setUnit("pH");
		acidity.setSpec(amtAcidity);
		treatment.setSoilAcidity(acidity);

		return treatment;
	}

	public FruitTreatment defineFruitTreatment(String harvestMode) {
		FruitTreatment treatment = factory.createFruitTreatment();

		Harvesting harvesting = (Harvesting) factory.createAction("harvesting");

		switch (harvestMode) {
		case "onClick":
			harvesting.setMode(HarvestMode.ON_CLICK);
			break;
		case "auto":
			harvesting.setMode(HarvestMode.AUTO);
			break;
		case "manual":
			harvesting.setMode(HarvestMode.MANUAL);
			break;
		}
		treatment.setHarvesting(harvesting);

		return treatment;
	}

}
