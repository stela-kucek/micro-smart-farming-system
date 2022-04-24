package com.ase0401.msfsdemo.dsl;

import java.io.File;

import org.eclipse.emf.common.util.URI;

public class Tester {
	public static void main(String[] args) {
		DslHandler handler = new DslHandler();
		
		String input = "			Grow program carrotprog\r\n" + 
				"			Plant carrot\r\n" + 
				"			SEED\r\n" + 
				"				Seeding\r\n" + 
				"					seed at 2 cm\r\n" + 
				"				Replanting\r\n" + 
				"					plant out after 2 weeks\r\n" + 
				"				Irrigating\r\n" + 
				"					water every day\r\n" + 
				"					amount: 1 L\r\n" + 
				"				Fertilizing\r\n" + 
				"					fertilizer: 'Urea'\r\n" + 
				"					amount: 2 TS\r\n" + 
				"					apply every 3 days, 3 times\r\n" + 
				"				Soil humidity\r\n" + 
				"					ideally 70%\r\n" + 
				"				Soil acidity\r\n" + 
				"					ideally pH 4\r\n" + 
				"			SEEDLING\r\n" + 
				"				Irrigating\r\n" + 
				"					water every 2 days\r\n" + 
				"					amount: 1 L\r\n" + 
				"				Fertilizing\r\n" + 
				"					fertilizer: 'Urea'\r\n" + 
				"					amount: 2 TS\r\n" + 
				"					apply every 5 days, 2 times\r\n" + 
				"				Soil humidity\r\n" + 
				"					ideally 60%\r\n" + 
				"				Soil acidity\r\n" + 
				"					ideally pH 4\r\n" + 
				"			YOUNG\r\n" + 
				"				Irrigating\r\n" + 
				"					water every 2 days\r\n" + 
				"					amount: 1 L\r\n" + 
				"				Fertilizing\r\n" + 
				"					fertilizer: 'Urea'\r\n" + 
				"					amount: 2 TS\r\n" + 
				"					apply every 7 days, 2 times\r\n" + 
				"				Soil humidity\r\n" + 
				"					ideally 50%\r\n" + 
				"				Soil acidity\r\n" + 
				"					ideally pH 4\r\n" + 
				"			ADULT\r\n" + 
				"				Irrigating\r\n" + 
				"					water every 3 days\r\n" + 
				"					amount: 0.5 L\r\n" + 
				"				Fertilizing\r\n" + 
				"					fertilizer: 'Urea'\r\n" + 
				"					amount: 1 TS\r\n" + 
				"					apply every 7 days, 2 times\r\n" + 
				"				Soil humidity\r\n" + 
				"					ideally 60%\r\n" + 
				"				Soil acidity\r\n" + 
				"					ideally pH 4\r\n" + 
				"			FRUIT\r\n" + 
				"				Harvesting\r\n" + 
				"					mode: manual";
		handler.generateDslFromString(input);
		
		handler.setResourceFromFile(URI.createFileURI("." + File.separator + "input.msfsdsl"));
		
		boolean valid = handler.isModelValid();
		if(valid) System.out.println("Success!");
		else System.out.println("Model invalid");

	}
}
