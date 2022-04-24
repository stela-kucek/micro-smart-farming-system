/**
 * 
 */
package com.ase0401.msfsdemo.constants;

import java.util.ArrayList;

import org.springframework.stereotype.Component;

import com.ase0401.msfsdemo.factory.ModelFactory;

import msfs_0401.Capability;
import msfs_0401.CapabilityType;

/**
 * @author stela
 *
 */
@Component
public final class FarmbotCapabilities {
	
	private static final ModelFactory f = new ModelFactory();
	
	public static final ArrayList<Capability> capabilities =  f.createCapabilityList(8);
	
	public FarmbotCapabilities() {
	
		capabilities.get(0).setName("Irrigating");
		capabilities.get(0).setType(CapabilityType.ACTION);
		capabilities.get(1).setName("Seeding");
		capabilities.get(1).setType(CapabilityType.ACTION);
		capabilities.get(2).setName("Fertilizing");
		capabilities.get(2).setType(CapabilityType.ACTION);
		capabilities.get(3).setName("Harvesting");
		capabilities.get(3).setType(CapabilityType.ACTION);
		capabilities.get(4).setName("Replanting");
		capabilities.get(4).setType(CapabilityType.ACTION);
		
		capabilities.get(5).setName("Soil Acidity");
		capabilities.get(5).setType(CapabilityType.MEASUREMENT);
		capabilities.get(6).setName("Soil Humidity");
		capabilities.get(6).setType(CapabilityType.MEASUREMENT);
		capabilities.get(7).setName("PlantGrowth");
		capabilities.get(7).setType(CapabilityType.MEASUREMENT);	
		
	}

	public static ArrayList<Capability> getCapabilities() {
		return capabilities;
	}

}
