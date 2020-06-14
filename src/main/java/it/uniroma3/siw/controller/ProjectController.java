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
import it.uniroma3.siw.controller.validation.ProjectValidator;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.EditProject;
import it.uniroma3.siw.model.Project;
import it.uniroma3.siw.model.ProjectReceiver;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ProjectService;
import it.uniroma3.siw.service.UserService;

@Controller
public class ProjectController {
	@Autowired
	ProjectService projectService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	ProjectValidator projectValidator;
	
	@Autowired
	SessionData sessionData;
	
	@Autowired
	CredentialsService credentialsService;
	
	@RequestMapping(value= {"/projects"}, method = RequestMethod.GET)
	public String myOwnedProjects(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projectsList = projectService.retrieveProjectsOwnedBy(loggedUser);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectsList", projectsList);
		return "projects";
	}
	@RequestMapping(value= {"/sharedProjects"}, method=RequestMethod.GET)
	public String sharedProjects(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		List<Project> projects = this.projectService.retrieveSharedProjects(loggedUser);
		model.addAttribute("user", loggedUser);
		model.addAttribute("projectsList", projects);
		return "sharedProjects";
	}
	
	@RequestMapping(value= {"/projects/{projectId}"}, method = RequestMethod.GET)
	public String project (Model model, @PathVariable Long projectId) {
		User loggedUser = sessionData.getLoggedUser();
		Project project = projectService.getProject(projectId);
		if(project==null)
			return "redirect:/projects";
		List<User> members = userService.getMembers(project);
		if(!project.getOwner().equals(loggedUser) && !members.contains(loggedUser))
			return "redirect:/projects";
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project", project);
		model.addAttribute("members", members);
		return "project";
	}
	
	@RequestMapping(value = {"/projects/add"}, method = RequestMethod.GET)
	public String createProjectForm(Model model) {
		User loggedUser = sessionData.getLoggedUser();
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("projectForm", new Project());
		return "addProject";
	}
	
	@RequestMapping(value= {"/projects/add"}, method = RequestMethod.POST)
	public String createProject(@Valid @ModelAttribute("projectForm") Project project, BindingResult bindingResult, Model model) {
		projectValidator.validate(project, bindingResult);
		User loggedUser = sessionData.getLoggedUser();
		if(!bindingResult.hasErrors()) {
			project.setOwner(loggedUser);
			this.projectService.saveProject(project);
			return "redirect:/projects/" + project.getId();
		}
		model.addAttribute("loggedUser", loggedUser);
		return "addProject";

	}
	@RequestMapping(value= {"/projects/share/{projectId}"}, method = RequestMethod.GET)
	public String shareProjectGet(Model model,  @PathVariable Long projectId) {
		ProjectReceiver projectReceiver = new ProjectReceiver();
		projectReceiver.setSharedProjectId(projectId);
		
		model.addAttribute("projectReceiver", projectReceiver);
		return "shareProject";
	}
	@RequestMapping(value= {"/projects/share"}, method = RequestMethod.POST)
	public String shareProjectPost(@Valid @ModelAttribute("projectReceiver") ProjectReceiver pr, BindingResult br, Model model) {
		Credentials credentials = this.credentialsService.getCredentials(pr.getUsername());
		Project project = this.projectService.getProject(pr.getSharedProjectId());
		User loggedUser = this.sessionData.getLoggedUser();
		model.addAttribute("projectReceiver", pr);
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("project", project);
		if(credentials==null) {
			br.rejectValue("username", "notexist");
			return "project";
		}
		else {
		User user = credentials.getUser();
		List<User> members = project.getMembers();
		members.add(user);
		project.setMembers(members);
		this.projectService.saveProject(project);
		
		model.addAttribute("members", members);
		return "project";
		}
		//devo controllare come gestire gli errori
	}
	@RequestMapping(value = {"/projects/edit/{projectId}"}, method = RequestMethod.GET)
	public String editProjectGet(Model model, @PathVariable("projectId") Long id) {
		EditProject ep = new EditProject();
		ep.setProjectId(id);
		model.addAttribute("editProject", ep);
		return "editProject";
	}
	
	
	@RequestMapping(value = {"/projects/edit"}, method = RequestMethod.POST)
	public String editProjectPost(@Valid @ModelAttribute("editProject") EditProject ep, BindingResult br, Model model) {
		if(ep.getName()!=null&&ep.getName().length()!=0) {
			this.projectValidator.validateName(ep.getName(), br);
		}
		if(ep.getDescription()!=null&&ep.getDescription().length()!=0) {
			this.projectValidator.validateDescription(ep.getDescription(), br);
		}
		if(!br.hasErrors()) {
			Project project = this.projectService.getProject(ep.getProjectId());
			if(ep.getName()!=null&&ep.getName().length()!=0) 
				project.setName(ep.getName());
			if(ep.getDescription()!=null&&ep.getDescription().length()!=0)
				project.setDescription(ep.getDescription());
			this.projectService.saveProject(project);
			return "redirect:/projects/" + project.getId();
		}
		model.addAttribute("editProject", ep);
		return "editProject";
	}
	@RequestMapping(value = {"/projects/delete/{projectId}"}, method = RequestMethod.GET)
	public String deleteProject(Model model, @PathVariable("projectId") Long projectId) {
		Project project = this.projectService.getProject(projectId);
		User loggedUser = sessionData.getLoggedUser();
		if(project!=null&&project.getOwner().equals(loggedUser)) {
			this.projectService.deleteProject(project);
		}
		return "redirect:/projects";
	}
	
}
