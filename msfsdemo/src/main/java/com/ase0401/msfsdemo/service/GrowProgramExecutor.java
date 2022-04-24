package com.ase0401.msfsdemo.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.common.util.EList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.ase0401.msfsdemo.management.ConfigItem;
import com.ase0401.msfsdemo.management.FarmingSiteManagement;
import com.ase0401.msfsdemo.service.model.LogModel;

import msfs_0401.FarmingDevice;
import msfs_0401.FarmingSite;
import msfs_0401.GrowProgram;
import msfs_0401.HarvestMode;
import msfs_0401.LifeCycleStage;
import msfs_0401.PlantToPosMapping;

@Service
@Scope("prototype")
public class GrowProgramExecutor implements GrowProgramExecutionService {

	String farmerUsername;
	GrowProgram program;
	ArrayList<ConfigItem> items = new ArrayList<ConfigItem>();
	ArrayList<LifeCycleStage> stages;
	ArrayList<FarmingSite> sites;
	String plant;
	// Map<Integer, ArrayList<ConfigItem>> tasks = new HashMap<Integer,
	// ArrayList<ConfigItem>>();
	@Autowired
	FarmingSiteManagement mgmt;
	@Autowired
	DeviceControlService service;

	public void setUp(GrowProgram prog) {
		program = prog;
		farmerUsername = program.getCreator();
		System.out.println("Farmers username is: " + farmerUsername);
		mgmt.setUpFarmingSiteManagement(farmerUsername);
		sites = mgmt.getUserSites();
	}

	@Override
	public void run() {
		filterGrowProgramInstructions();
		try {
			executeOnDevices();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void filterGrowProgramInstructions() {
		plant = program.getPlantSpecies();
		stages = getStagesInProgram();
		if (!stages.isEmpty()) {
			for (Iterator<FarmingSite> iterator2 = sites.iterator(); iterator2.hasNext();) {
				FarmingSite farmingSite = (FarmingSite) iterator2.next();
				FarmingDevice device = mgmt.getDeviceByName(farmingSite.getAssignedDevice());
				if (device != null) {
					int deviceId = device.getId();
					EList<PlantToPosMapping> mappings = farmingSite.getCurrentLayout().getMappings();
					for (Iterator<PlantToPosMapping> iterator3 = mappings.iterator(); iterator3.hasNext();) {
						PlantToPosMapping p = (PlantToPosMapping) iterator3.next();
						if (p.getPlant().equals(plant)) {
							int pos = p.getPosition();
							items.add(new ConfigItem(deviceId, pos, 2));
						}
					}
				}
			}
		}

	}

	@Override
	public void executeOnDevices() throws InterruptedException {
		if (!items.isEmpty()) {
			for (Iterator<ConfigItem> iterator = items.iterator(); iterator.hasNext();) {
				ConfigItem item = (ConfigItem) iterator.next();
				int id = item.getDeviceId();
				int pos = item.getPos();
				service.clearSchedule(id);
				service.setMeasurement(id, item.getMeasurementId(), pos);
				service.toggleState(id, "on");

				// wait for the device to finish detection
				Thread.sleep(10000);

				List<LogModel> sensordata = service.getSensorData(id);
				List<LogModel> actuatordata = service.getActuatorData(id);

				LogModel logOfInterest = sensordata.get(sensordata.size() - 1); // the log of interest is the latest,
																				// i.e., last one in the list
				LifeCycleStage stageAtPos = logOfInterest.getPlantGrowth().getStage();

				Date lastIrrigated = null;
				Date lastFertilized = null;
				Date now = new Date();
				int irrPeriod = 0;
				double irrAmount = 0.0;
				int ferPeriod = 0;
				double ferAmount = 0.0;
				String ferUnit = "";
				boolean isFruit = false;
				HarvestMode harvest = HarvestMode.MANUAL;
				if (stages.contains(stageAtPos)) {
					// find out timestamp of last irrigation and fertilization
					for (Iterator<LogModel> iterator2 = actuatordata.iterator(); iterator2.hasNext();) {
						LogModel log = (LogModel) iterator2.next();
						com.ase0401.msfsdemo.service.model.Action action = log.getAction();
						if (action.getSpec().getUnit().equalsIgnoreCase("L")) {
							Date ts = log.getTimestamp();
							if (lastIrrigated == null) {
								lastIrrigated = ts;
							}
							else if (ts.after(lastIrrigated)) {
								lastIrrigated = ts;
							}
						}
						if (action.getSpec().getUnit().equalsIgnoreCase("TS")
								|| action.getSpec().getUnit().equalsIgnoreCase("g")) {
							Date ts = log.getTimestamp();
							if (lastFertilized == null)
								lastFertilized = ts;
							else if (ts.after(lastFertilized)) {
								lastFertilized = ts;
							}
						}
					}

					// fetch instruction for this stage
					switch (stageAtPos) {
					case SEED:
						System.out.println("Its a seed");
						irrPeriod = (int) program.getSeedTreatment().getIrrigating().getPeriod().getValue();
						ferPeriod = (int) program.getSeedTreatment().getFertilizing().getPeriod().getValue();
						irrAmount = program.getSeedTreatment().getIrrigating().getSpec().getValue();
						ferAmount = program.getSeedTreatment().getFertilizing().getSpec().getValue();
						ferUnit = program.getSeedTreatment().getFertilizing().getSpec().getUnit();
						break;
					case SEEDLING:
						System.out.println("Its a seedling");
						irrPeriod = (int) program.getSeedlingTreatment().getIrrigating().getPeriod().getValue();
						ferPeriod = (int) program.getSeedlingTreatment().getFertilizing().getPeriod().getValue();
						irrAmount = program.getSeedlingTreatment().getIrrigating().getSpec().getValue();
						ferAmount = program.getSeedlingTreatment().getFertilizing().getSpec().getValue();
						ferUnit = program.getSeedlingTreatment().getFertilizing().getSpec().getUnit();
						break;
					case YOUNG:
						System.out.println("Its a young");
						irrPeriod = (int) program.getYoungTreatment().getIrrigating().getPeriod().getValue();
						ferPeriod = (int) program.getYoungTreatment().getFertilizing().getPeriod().getValue();
						irrAmount = program.getYoungTreatment().getIrrigating().getSpec().getValue();
						ferAmount = program.getYoungTreatment().getFertilizing().getSpec().getValue();
						ferUnit = program.getYoungTreatment().getFertilizing().getSpec().getUnit();
						break;
					case ADULT:
						System.out.println("Its an adult");
						irrPeriod = (int) program.getAdultTreatment().getIrrigating().getPeriod().getValue();
						ferPeriod = (int) program.getAdultTreatment().getFertilizing().getPeriod().getValue();
						irrAmount = program.getAdultTreatment().getIrrigating().getSpec().getValue();
						ferAmount = program.getAdultTreatment().getFertilizing().getSpec().getValue();
						ferUnit = program.getAdultTreatment().getFertilizing().getSpec().getUnit();
						break;
					case FRUIT:
						System.out.println("Its a fruit");
						harvest = program.getFruitTreatment().getHarvesting().getMode();
						isFruit = true;
						break;
					}

					if (isFruit) {
						if (harvest.equals(HarvestMode.AUTO)) {
							service.setAction(id, 4, pos, 0.0, null);
						}

						// wait for the device to finish harvest
						Thread.sleep(10000);

						// turn off the device
						service.toggleState(id, "off");
					} else {
						if (now.compareTo(lastIrrigated) >= irrPeriod) {
							service.setAction(id, 3, pos, irrAmount, "L");

							// wait for the device to finish irrigating
							Thread.sleep(10000);

						}
						if (now.compareTo(lastFertilized) >= ferPeriod) {
							service.setAction(id, 2, pos, ferAmount, ferUnit);

							// wait for the device to finish fertilizing
							Thread.sleep(10000);

						}
						// turn off the device
						service.toggleState(id, "off");
					}

				}
			}
		}

		else
			System.out.println("There are no " + plant + "s on your farming sites.");
	}

	private ArrayList<LifeCycleStage> getStagesInProgram() {
		ArrayList<LifeCycleStage> stages = new ArrayList<LifeCycleStage>();
		if (program.getSeedTreatment() != null) {
			stages.add(LifeCycleStage.SEED);
		}
		if (program.getSeedlingTreatment() != null) {
			stages.add(LifeCycleStage.SEEDLING);
		}
		if (program.getYoungTreatment() != null) {
			stages.add(LifeCycleStage.YOUNG);
		}
		if (program.getAdultTreatment() != null) {
			stages.add(LifeCycleStage.ADULT);
		}
		if (program.getFruitTreatment() != null) {
			stages.add(LifeCycleStage.FRUIT);
		}
		return stages;
	}

}
