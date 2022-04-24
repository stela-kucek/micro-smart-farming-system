/**
 * 
 */
package com.ase0401.msfsdemo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author stela
 * 
 *         Login Controller processing requests from the login.jsp
 * 
 *         Since we are using Spring Security, the login page will be displayed
 *         first with our custom login form. (See @WebSecurityConfig class for
 *         details)
 * 
 *         The corresponding jsp (View) files are located in
 *         /msfsdemo/src/main/webapp/WEB-INF/views
 * 
 */
@Controller
public class LoginController {

	@GetMapping("/login")
	public String showLoginPage() {
		return "login";
	}

}
