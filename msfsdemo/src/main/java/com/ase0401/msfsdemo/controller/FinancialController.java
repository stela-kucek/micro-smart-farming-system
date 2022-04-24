package com.ase0401.msfsdemo.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.ase0401.msfsdemo.management.FinancialManagement;
import com.ase0401.msfsdemo.service.UserService;

import msfs_0401.MicroFarmer;


/**
 * @author Eiman
 *
 */
@Controller


public class FinancialController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private FinancialManagement mgmt;
	
	@SuppressWarnings("deprecation")
	@GetMapping("/finances")
	public String showFinances(Authentication auth, Model model) {
		String username = auth.getName();
		
		MicroFarmer user = (MicroFarmer) userService.findByUsername(username);
		int userId = user.getId();

		mgmt.setUpUserFinancesById(userId, userService.getUserRepository());
		mgmt.addFinancialEntry("Revenue", 1283.0, "Selling yield", "€", new Date(120,2,15,11,10,00));
		mgmt.addFinancialEntry("Revenue", 970.0, "Selling plant", "€", new Date(120,9,5,3,22,43));
		mgmt.addFinancialEntry("Revenue", 1090.0, "Selling grow-programs", "€", new Date());
		
		mgmt.addFinancialEntry("Expense", 467.0, "Buying grow-programs", "€", new Date(120,2,23,9,28,00));
		mgmt.addFinancialEntry("Expense", 220.0, "Buying plant saplings", "€", new Date(120,9,16,11,18,3));
		mgmt.addFinancialEntry("Expense", 720.0, "Buying seeds", "€", new Date(120,10,1,43,23,12));
		
		mgmt.addFinancialEntry("Operational Cost", 123.2, "Fertilizer cost", "$", new Date(120,2,4,2,46,34));
		mgmt.addFinancialEntry("Operational Cost", 102.2, "Water cost", "$", new Date(120,4,19,2,22,39));
		
		model.addAttribute("OperationalCost", mgmt.getOperationalCostsOfUser(userId));
		model.addAttribute("ExpensesOfUser", mgmt.getExpensesOfUser());
		model.addAttribute("Revenues", mgmt.getRevenuesOfUser());
		
		String dataPoints = mgmt.getDataPoints();
		model.addAttribute("dataPoints", dataPoints);
		

		return "finances";
	}
}

