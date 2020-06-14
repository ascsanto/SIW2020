package it.uniroma3.siw.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.controller.session.SessionData;
import it.uniroma3.siw.controller.validation.CredentialsValidator;
import it.uniroma3.siw.controller.validation.UserValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.EditPassword;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;

@Controller
public class AuthenticationController {

    @Autowired
    CredentialsService credentialsService;

    @Autowired
    UserValidator userValidator;

    @Autowired
    CredentialsValidator credentialsValidator;

    @Autowired
    SessionData sessionData;

    /**
     * This method is called when a GET request is sent by the user to URL "/register".
     * This method prepares and dispatches the User registration view.
     *
     * @param model the Request model
     * @return the name of the target view, that in this case is "register"
     */
    @RequestMapping(value = { "/users/register" }, method = RequestMethod.GET)
    public String showRegisterForm(Model model) {
        model.addAttribute("userForm", new User());
        model.addAttribute("credentialsForm", new Credentials());

        return "registerUser";
    }

    /**
     * This method is called when a GET request is sent by the user to URL "/register".
     * This method prepares and dispatches the User registration view.
     *
     * @param model the Request model
     * @return the name of the target view, that in this case is "register"
     */
    @RequestMapping(value = { "/users/register" }, method = RequestMethod.POST)
    public String registerUser(@Valid @ModelAttribute("userForm") User user,
                               BindingResult userBindingResult,
                               @Valid @ModelAttribute("credentialsForm") Credentials credentials,
                               BindingResult credentialsBindingResult,
                               Model model) {

        // validate user and credentials fields
        this.userValidator.validate(user, userBindingResult);
        this.credentialsValidator.validate(credentials, credentialsBindingResult);

        // if neither of them had invalid contents, store the User and the Credentials into the DB
        if(!userBindingResult.hasErrors() && ! credentialsBindingResult.hasErrors()) {
            // set the user and store the credentials;
            // this also stores the User, thanks to Cascade.ALL policy
            credentials.setUser(user);
            credentialsService.saveCredentials(credentials);
            return "registrationSuccessful";
        }
        return "registerUser";
    }
    @RequestMapping(value= {"/users/me/editpassword"}, method = RequestMethod.GET)
    public String changePasswordGet(Model model) {
    	model.addAttribute("editPasswordForm", new EditPassword());
    	return "editPassword";
    }
    @RequestMapping(value= {"/users/me/editpassword"}, method = RequestMethod.POST)
    public String changePasswordPost(@Valid @ModelAttribute("editPasswordForm") EditPassword editPassword, BindingResult bindingResult, Model model) {
    	User loggedUser = sessionData.getLoggedUser();
    	String oldPasswordFromForm = editPassword.getOldpassword();
    	Credentials credentials = this.credentialsService.getCredentials(loggedUser);
    	String oldPasswordFromDB = credentials.getPassword();
    	this.credentialsValidator.validatePassword(editPassword.getNewpassword(), bindingResult);
    	credentialsService.matchPasswords(oldPasswordFromForm, oldPasswordFromDB, bindingResult); 
    	if(!bindingResult.hasErrors()) {
    		credentials.setPassword(editPassword.getNewpassword());
    		this.credentialsService.saveCredentials(credentials);
    		model.addAttribute("credentials", credentials);
    		model.addAttribute("user", loggedUser);
    		return "userProfile";
    	}

    	return "editPassword";

    }    	
}
