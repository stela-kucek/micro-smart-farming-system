/**
 * 
 */
package com.ase0401.msfsdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author stela
 *
 */
@Controller
public class HomeController {
	
	@GetMapping("/")
	public String showHomePage() {
		return "index";
	}

}
