package it.uniroma3.siw.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.controller.session.SessionData;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.UserService;

@Controller
public class AdminController {
	@Autowired
	UserService userService;
	
	@Autowired
	CredentialsService credentialsService;
	
	@Autowired
	SessionData sessionData;
	
	@RequestMapping(value= {"/admin"}, method = RequestMethod.GET)
	public String adminGet(Model model) {
		List<Credentials> credentials = this.credentialsService.getAllCredentials();
		model.addAttribute("credentials", credentials);
		return "admin";
	}
	@RequestMapping(value= {"/admin/delete/{username}"}, method = RequestMethod.GET)
	public String deleteUser(@PathVariable("username") String username, Model model) {
		Credentials c = this.credentialsService.getCredentials(username);
		if(c!=null)
		this.credentialsService.deleteCredentials(username);
		return "redirect:/admin";
	}
}
