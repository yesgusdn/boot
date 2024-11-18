package com.in28minutes.springboot.myfirstwebapp.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@SessionAttributes("name")
public class LoginController {
	
	private AuthenticationService authenticationService;
	
	public LoginController(AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
	
	@RequestMapping(value="login",method = RequestMethod.GET)
	private String gotoLoginPage() {
		return "login";
	}
	


	@RequestMapping(value="login",method = RequestMethod.POST)
	private String gotoWelcomePage(@RequestParam String name, @RequestParam String password
			, ModelMap model) {
		
		if(authenticationService.authenticate(name, password)) {
			model.put("name", name);
			return "welcome";
		}
		
		model.put("ErrorMessage", "Invalid Credentials");
		return "login";

	}	
}
