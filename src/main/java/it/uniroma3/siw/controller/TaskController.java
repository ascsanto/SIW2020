package it.uniroma3.siw.controller;

import java.util.List;

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
import it.uniroma3.siw.model.AddTask;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.EntrustTask;
import it.uniroma3.siw.model.Project;
import it.uniroma3.siw.model.Task;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
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
	CredentialsService credentialsService;
	
	@Autowired
	SessionData sessionData;
	
	@Autowired
	TaskValidator taskValidator;
	
	
@RequestMapping(value= {"/projects/addTask/{projectId}"}, method = RequestMethod.GET)
	public String getAddTask(Model model, @PathVariable("projectId") Long projectId) {
	Project pj = this.projectService.getProject(projectId);
	User loggedUser = this.sessionData.getLoggedUser();
	if(pj==null||(!pj.getOwner().equals(loggedUser)&&!pj.getMembers().contains(loggedUser)))
			return "redirect:/projects";
	AddTask addTask = new AddTask();
	addTask.setTask(new Task());
	addTask.setProjectId(projectId);
		model.addAttribute("addTask", addTask);
		return "addTask";
	}
	@RequestMapping(value = {"/projects/addTask"}, method = RequestMethod.POST)
	public String postAddTask(@Valid @ModelAttribute("addTask") AddTask addTask, 
			BindingResult br, Model model) {
		this.taskValidator.validate(addTask.getTask(), br);
		if(!br.hasErrors()) {
			User loggedUser = sessionData.getLoggedUser();
			Project pj = this.projectService.getProject(addTask.getProjectId());
			if(pj==null||(!pj.getOwner().equals(loggedUser)&&!pj.getMembers().contains(loggedUser)))
				return "redirect:/projects"+pj.getId();
			Task task = addTask.getTask();
			task.setUser(loggedUser);
			task.setProject(pj);
			List<Task> tasks = pj.getTasks();
			tasks.add(task);
			pj.setTasks(tasks);
			this.taskService.saveTask(addTask.getTask());
			this.projectService.saveProject(pj);
			return "redirect:/projects";
		}
		model.addAttribute("addTask", addTask);
		return "redirect: /projects/addTask/" + addTask.getProjectId();
	}
	
	@RequestMapping(value= {"/tasks/{taskID}"}, method=RequestMethod.GET)
	public String taskGet(Model model, @PathVariable("taskID") Long taskId) {
		Task task = this.taskService.getTask(taskId);
		Project pj = task.getProject();
		User loggedUser = sessionData.getLoggedUser();
		if(!pj.getOwner().equals(loggedUser)&&!pj.getMembers().contains(loggedUser))
			return "redirect:/projects";
		model.addAttribute("task", task);
		return "task";
	}
	@RequestMapping(value= {"/tasks/edit/{taskID}"}, method=RequestMethod.GET)
	public String editTaskGet(Model model, @PathVariable("taskID") Long taskID) {
		Task taskFromDB = this.taskService.getTask(taskID);
		Project pj = taskFromDB.getProject();
		User loggedUser = sessionData.getLoggedUser();
		if(!pj.getOwner().equals(loggedUser)&&!pj.getMembers().contains(loggedUser))
			return "redirect:/projects";
		model.addAttribute("task",taskFromDB);
		return "editTask";
	}
	@RequestMapping(value = {"/tasks/edit"}, method=RequestMethod.POST)
	public String editTaskPost(@Valid @ModelAttribute("task") Task task, BindingResult br, Model model) {
		this.taskValidator.validate(task, br);
		Task taskDB = this.taskService.getTask(task.getId());
		if(!br.hasErrors()) {
			taskDB.setName(task.getName());
			taskDB.setDescription(task.getDescription());
			this.taskService.saveTask(taskDB);;
			model.addAttribute("task", taskDB);
			return "task";
		}
		
		model.addAttribute("task", taskDB);
		return "editTask";
	}
	
	@RequestMapping(value = {"/tasks/delete/{taskID}"}, method=RequestMethod.GET)
	public String deleteTask(Model model, @PathVariable("taskID") Long id) {
		Task task = this.taskService.getTask(id);
		Project pj = task.getProject();
		User loggedUser = sessionData.getLoggedUser();
		if(!pj.getOwner().equals(loggedUser)&&!pj.getMembers().contains(loggedUser))
			return "redirect:/projects";
		this.taskService.deleteTask(task);
		return "redirect:/projects";
	}
	@RequestMapping(value= {"/tasks/entrust/{taskID}"}, method=RequestMethod.GET)
	public String entrustTaskGet(Model model, @PathVariable("taskID") Long id) {
		Task task = this.taskService.getTask(id);
		Project pj = task.getProject();
		User loggedUser = sessionData.getLoggedUser();
		if(!pj.getOwner().equals(loggedUser)&&!pj.getMembers().contains(loggedUser))
			return "redirect:/projects";
		EntrustTask et = new EntrustTask();
		et.setId(id);
		et.setUsername(this.sessionData.getLoggedCredentials().getUserName());
		model.addAttribute("entrustTask", et);
		return "entrustTask";
	}
	
	@RequestMapping(value= {"/tasks/entrust"}, method = RequestMethod.POST)
	public String entrustTaskPost(@Valid @ModelAttribute("entrustTask") EntrustTask entrustTask, BindingResult br, Model model) {
		Credentials cr = this.credentialsService.getCredentials(entrustTask.getUsername());
		Task task  = this.taskService.getTask(entrustTask.getId());
		if(cr==null)
				br.rejectValue("username", "notvalid");
		else if(!task.getProject().getMembers().contains(cr.getUser())&&!task.getProject().getOwner().equals(cr.getUser()))
			br.rejectValue("username", "notshared");
		if(!br.hasErrors()) {
			task.setUser(cr.getUser());
			this.taskService.saveTask(task);
			return "redirect:/tasks/"+task.getId();
		}
		model.addAttribute("entrustTask", entrustTask);
		return "entrustTask";
	}
	@RequestMapping(value= {"/tasks/state/{taskID}"}, method=RequestMethod.GET)
	public String changeStateGet(Model model, @PathVariable("taskID") Long id) {
		Task task = this.taskService.getTask(id);
		Project pj = task.getProject();
		User loggedUser = sessionData.getLoggedUser();
		if(!pj.getOwner().equals(loggedUser)&&!pj.getMembers().contains(loggedUser))
			return "redirect:/projects";
		task.setCompleted(!task.isCompleted());
		this.taskService.saveTask(task);
		return "redirect:/tasks/"+task.getId();
	}
	
//	public String shareTaskPost(...)
	//DEVO RESETTARE DB
}
