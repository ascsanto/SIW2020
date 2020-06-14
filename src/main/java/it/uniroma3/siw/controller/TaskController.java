package it.uniroma3.siw.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.controller.session.SessionData;
import it.uniroma3.siw.controller.validation.TaskValidator;
import it.uniroma3.siw.model.Project;
import it.uniroma3.siw.model.Task;
import it.uniroma3.siw.service.ProjectService;
import it.uniroma3.siw.service.TaskService;
import it.uniroma3.siw.service.UserService;

@Controller
public class TaskController {

	@Autowired
	TaskService taskService;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	SessionData sessionData;
	
	@Autowired
	TaskValidator taskValidator;
	
	
/*	@RequestMapping(value= {"/projects/addTask/{projectId}"}, method = RequestMethod.GET)
	public String getAddTask(Model model, @PathVariable("projectId") Long projectId) {
		model.addAttribute("projectId", projectId);
		model.addAttribute("task", new Task());
		return "/projects/addTask";
	}
	@RequestMapping(value = {"/projects/addTask"}, method = RequestMethod.POST)
	public String postAddTask(@Valid @ModelAttribute("projectId") Long projectId, @Valid @ModelAttribute("task") Task task, 
			BindingResult br, Model model) {
		this.taskValidator.validate(task, br);
		if(this.userService.getUser(task.getUser().getId())==null)
			br.rejectValue("user", "userNotFound"); //TODO creare messaggio di errore
		if(!br.hasErrors()) {
			this.taskService.saveTask(task);
			return "redirect:/projects";
		}
		model.addAttribute("projectId", projectId);
		model.addAttribute("task", new Task());
		return "/projects/addTask";
	} */
}
