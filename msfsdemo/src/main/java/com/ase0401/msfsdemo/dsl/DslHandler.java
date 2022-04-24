/**
 * 
 */
package com.ase0401.msfsdemo.dsl;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.resource.XtextResourceSet;
import org.eclipse.xtext.util.CancelIndicator;
import org.eclipse.xtext.validation.CheckMode;
import org.eclipse.xtext.validation.IResourceValidator;
import org.eclipse.xtext.validation.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xtext.ase0401.gpdsl.MsfsDslStandaloneSetup;
import org.xtext.ase0401.gpdsl.msfsDsl.AdultTreatment;
import org.xtext.ase0401.gpdsl.msfsDsl.FruitTreatment;
import org.xtext.ase0401.gpdsl.msfsDsl.GrowProgram;
import org.xtext.ase0401.gpdsl.msfsDsl.SeedTreatment;
import org.xtext.ase0401.gpdsl.msfsDsl.SeedlingTreatment;
import org.xtext.ase0401.gpdsl.msfsDsl.YoungTreatment;

import com.ase0401.msfsdemo.management.GrowProgramManagement;
import com.google.inject.Injector;

/**
 * @author stela
 *
 */
@Component
public class DslHandler {
	private Injector injector;
	private XtextResourceSet resourceSet;
	private Resource resource;

	@Autowired
	private GrowProgramManagement mgmt;

	public void setUpHandler() {
		injector = new MsfsDslStandaloneSetup().createInjectorAndDoEMFRegistration();
		resourceSet = injector.getInstance(XtextResourceSet.class);
	}

	public void generateDslFromString(String input) {
		FileOutputStream fileOutputStream = null;

		try {
			fileOutputStream = new FileOutputStream("input.msfsdsl");
			fileOutputStream.write(input.getBytes());
			fileOutputStream.flush();
			fileOutputStream.close();
		} catch (IOException e) {
			System.out.println("Exception thrown while trying to write file:");
			e.printStackTrace();
		}

	}

	public void setResourceFromFile(URI uri) {
		resource = resourceSet.getResource(uri, true);
		System.out.println("------ RESOURCE: \n" + resource);
		System.out.println("------------RESOURCE SET: \n" + resourceSet);
		if (resource == null) {
			System.out.println("Something went wrong. Resource is null.");
		}
	}

	public boolean isModelValid() {
		IResourceValidator validator = ((XtextResource) resource).getResourceServiceProvider().getResourceValidator();
		List<Issue> issues = validator.validate(resource, CheckMode.ALL, CancelIndicator.NullImpl);
		if (issues.isEmpty()) {
			return true;
		}
		for (Iterator<Issue> iterator = issues.iterator(); iterator.hasNext();) {
			Issue issue = (Issue) iterator.next();
			System.out.println("Issue: " + issue.getMessage());
		}
		return false;
	}

	public void addGrowProgramFromDSL() {
		EList<EObject> list = resource.getContents();
		GrowProgram gp = null;
		for (Iterator<EObject> iterator = list.iterator(); iterator.hasNext();) {
			EObject eObject = (EObject) iterator.next();
			if (eObject instanceof GrowProgram) {
				gp = (GrowProgram) eObject;
				break;
			}
		}

		SeedTreatment seedTreatment = gp.getDescription().getSeedTreatment();
		msfs_0401.SeedTreatment seedT;
		if (seedTreatment == null) {
			seedT = null;
		} else {
			seedT = mgmt.defineSeedTreatment(
					Double.valueOf(gp.getDescription().getSeedTreatment().getSeeding().getSeedAt()),
					gp.getDescription().getSeedTreatment().getSeeding().getUnit(),
					Double.valueOf(gp.getDescription().getSeedTreatment().getReplanting().getPlantOuAfter()),
					gp.getDescription().getSeedTreatment().getReplanting().getUnit(),
					Double.valueOf(gp.getDescription().getSeedTreatment().getSoilHumidtiy().getHumidity()),
					Double.valueOf(gp.getDescription().getSeedTreatment().getSoilAcidity().getAcidity()),
					Double.valueOf(gp.getDescription().getSeedTreatment().getIrrigating().getAmount()),
					Double.valueOf(gp.getDescription().getSeedTreatment().getIrrigating().getPeriod()),
					gp.getDescription().getSeedTreatment().getIrrigating().getUnit(),
					gp.getDescription().getSeedTreatment().getFertilizing().getFertilizer(),
					Double.valueOf(gp.getDescription().getSeedTreatment().getFertilizing().getAmount()),
					gp.getDescription().getSeedTreatment().getFertilizing().getUnit(),
					Double.valueOf(gp.getDescription().getSeedTreatment().getFertilizing().getPeriod()),
					gp.getDescription().getSeedTreatment().getFertilizing().getTimeUnit(),
					gp.getDescription().getSeedTreatment().getFertilizing().getRepetitions());
		}

		SeedlingTreatment seedlingTreatment = gp.getDescription().getSeedlingTreatment();
		msfs_0401.SeedlingTreatment seedlingT;
		if (seedlingTreatment == null) {
			seedlingT = null;
		} else {
			seedlingT = mgmt.defineSeedlingTreatment(
					Double.valueOf(gp.getDescription().getSeedlingTreatment().getSoilHumidtiy().getHumidity()),
					Double.valueOf(gp.getDescription().getSeedlingTreatment().getSoilAcidity().getAcidity()),
					Double.valueOf(gp.getDescription().getSeedlingTreatment().getIrrigating().getAmount()),
					Double.valueOf(gp.getDescription().getSeedlingTreatment().getIrrigating().getPeriod()),
					gp.getDescription().getSeedlingTreatment().getIrrigating().getUnit(),
					gp.getDescription().getSeedlingTreatment().getFertilizing().getFertilizer(),
					Double.valueOf(gp.getDescription().getSeedlingTreatment().getFertilizing().getAmount()),
					gp.getDescription().getSeedlingTreatment().getFertilizing().getUnit(),
					Double.valueOf(gp.getDescription().getSeedlingTreatment().getFertilizing().getPeriod()),
					gp.getDescription().getSeedlingTreatment().getFertilizing().getTimeUnit(),
					gp.getDescription().getSeedlingTreatment().getFertilizing().getRepetitions());
		}

		YoungTreatment youngTreatment = gp.getDescription().getYoungTreatment();

		msfs_0401.YoungTreatment youngT;
		if (youngTreatment == null) {
			youngT = null;
		} else {
			youngT = mgmt.defineYoungTreatment(
					Double.valueOf(gp.getDescription().getYoungTreatment().getSoilHumidtiy().getHumidity()),
					Double.valueOf(gp.getDescription().getYoungTreatment().getSoilAcidity().getAcidity()),
					Double.valueOf(gp.getDescription().getYoungTreatment().getIrrigating().getAmount()),
					Double.valueOf(gp.getDescription().getYoungTreatment().getIrrigating().getPeriod()),
					gp.getDescription().getYoungTreatment().getIrrigating().getUnit(),
					gp.getDescription().getYoungTreatment().getFertilizing().getFertilizer(),
					Double.valueOf(gp.getDescription().getYoungTreatment().getFertilizing().getAmount()),
					gp.getDescription().getYoungTreatment().getFertilizing().getUnit(),
					Double.valueOf(gp.getDescription().getYoungTreatment().getFertilizing().getPeriod()),
					gp.getDescription().getYoungTreatment().getFertilizing().getTimeUnit(),
					gp.getDescription().getYoungTreatment().getFertilizing().getRepetitions());
		}

		AdultTreatment adultTreatment = gp.getDescription().getAdultTreatment();

		msfs_0401.AdultTreatment adultT;
		if (adultTreatment == null) {
			adultT = null;
		} else {
			adultT = mgmt.defineAdultTreatment(
					Double.valueOf(gp.getDescription().getAdultTreatment().getSoilHumidtiy().getHumidity()),
					Double.valueOf(gp.getDescription().getAdultTreatment().getSoilAcidity().getAcidity()),
					Double.valueOf(gp.getDescription().getAdultTreatment().getIrrigating().getAmount()),
					Double.valueOf(gp.getDescription().getAdultTreatment().getIrrigating().getPeriod()),
					gp.getDescription().getAdultTreatment().getIrrigating().getUnit(),
					gp.getDescription().getAdultTreatment().getFertilizing().getFertilizer(),
					Double.valueOf(gp.getDescription().getAdultTreatment().getFertilizing().getAmount()),
					gp.getDescription().getAdultTreatment().getFertilizing().getUnit(),
					Double.valueOf(gp.getDescription().getAdultTreatment().getFertilizing().getPeriod()),
					gp.getDescription().getAdultTreatment().getFertilizing().getTimeUnit(),
					gp.getDescription().getAdultTreatment().getFertilizing().getRepetitions());
		}

		FruitTreatment fruitTreatment = gp.getDescription().getFruitTreatment();
		msfs_0401.FruitTreatment fruitT;
		if (fruitTreatment == null) {
			fruitT = null;
		} else {
			fruitT = mgmt.defineFruitTreatment(
					gp.getDescription().getFruitTreatment().getHarvesting().getHarvestMode().getName());
		}

		mgmt.addGrowProgram(gp.getName(), gp.getDescription().getName(), 0.0, false, seedT, seedlingT, youngT, adultT,
				fruitT);

	}

}
