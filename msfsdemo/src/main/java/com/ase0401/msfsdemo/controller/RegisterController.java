/**
 * 
 */
package com.ase0401.msfsdemo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.ase0401.msfsdemo.controller.form.UserForm;
import com.ase0401.msfsdemo.service.UserService;

import msfs_0401.User;

/**
 * @author stela
 *
 */
@Controller
public class RegisterController {

	@Autowired
	private UserService userService;

	@GetMapping("/register")
	public String showRegisterPage(Model model) {
		model.addAttribute("user", new UserForm());
		return "register";
	}

	@PostMapping("/register")
	public String processRegistration(@ModelAttribute("user") UserForm user, Model model) {
		
		String warningMessage = checkFields(user);
		
		if(warningMessage.length() > 0) {
			model.addAttribute("user", user);
			model.addAttribute("registrationError", warningMessage);
			return "register";
		}

		User existing = userService.findByUsername(user.getUsername());

		if (existing != null) {
			model.addAttribute("user", new UserForm());
			model.addAttribute("registrationError", "Username already exists.");
			return "register";
		}

		userService.save(user);

		return "register-success";
	}
	
	private String checkFields(UserForm user) {
		String message = "";
		
		if (user.getFirstname().isEmpty()) {
			message += " First name is required. ";
		}
		
		if (user.getLastname().isEmpty()) {
			message += " Last name is required. ";
		}
		
		if (user.getUsername().isEmpty()) {
			message += " Username is required. ";
		}
		
		if (user.getPassword().isEmpty()) {
			message += " Password is required. ";
		}
		
		if (user.getPassword().length() < 6) {
			message += " Password must be at least 6 characters long. ";
		}
		
		if (user.getType().equals("Seed Supplier") && user.getCompany().isEmpty()) {
			message += " Seed Suppliers must provide a company name. ";
		}
		
		if (user.getType().equals("Device Supplier") && user.getCompany().isEmpty()) {
			message += " Device Suppliers must provide a company name. ";
		}
		
		return message;
	}
}
