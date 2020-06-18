package it.uniroma3.siw.controller;

import java.util.ArrayList;
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
import it.uniroma3.siw.controller.validation.CommentValidator;
import it.uniroma3.siw.controller.validation.TaskValidator;
import it.uniroma3.siw.model.AddComment;
import it.uniroma3.siw.model.AddTag;
import it.uniroma3.siw.model.AddTask;
import it.uniroma3.siw.model.Comment;
import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.EntrustTask;
import it.uniroma3.siw.model.Project;
import it.uniroma3.siw.model.Tag;
import it.uniroma3.siw.model.Task;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.service.CommentService;
import it.uniroma3.siw.service.CredentialsService;
import it.uniroma3.siw.service.ProjectService;
import it.uniroma3.siw.service.TagService;
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
	
	@Autowired
	TagService tagService;
	
	@Autowired
	CommentValidator commentValidator;
	
	@Autowired
	CommentService commentService;
	
	
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
			task.setCommentList(new ArrayList<Comment>());
			task.setUser(loggedUser);
			task.setProject(pj);;
			List<Task> tasks = pj.getTasks();
			tasks.add(task);
			pj.setTasks(tasks);
			this.taskService.saveTask(addTask.getTask());
			this.projectService.saveProject(pj);
			return "redirect:/projects";
		}
		model.addAttribute("addTask", addTask);
		return "redirect:/projects/addTask/" + addTask.getProjectId();
	}
	
	@RequestMapping(value= {"/tasks/{taskID}"}, method=RequestMethod.GET)
	public String taskGet(Model model, @PathVariable("taskID") Long taskId) {
		Task task = this.taskService.getTask(taskId);
		Project pj = task.getProject();
		User loggedUser = sessionData.getLoggedUser();
		if(!pj.getOwner().equals(loggedUser)&&!pj.getMembers().contains(loggedUser))
			return "redirect:/projects";
		model.addAttribute("task", task);
		AddComment addComment = new AddComment();
		addComment.setTaskId(taskId);
		System.out.println("impostato id task" + taskId);
		addComment.setComment(new Comment());
		model.addAttribute("loggedUser", loggedUser);
		model.addAttribute("addComment",addComment);
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
			return "redirect:/tasks/"+taskDB.getId();
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
	
	@RequestMapping(value= {"/tasks/addTag/{taskID}"}, method = RequestMethod.GET)
	public String addTagGet(Model model, @PathVariable("taskID") Long taskId) {
		AddTag addTag = new AddTag();
		addTag.setTaskID(taskId);
		Task task = this.taskService.getTask(taskId);
		List<Tag> tags = task.getProject().getTagList();
		addTag.setTagList(tags);
		model.addAttribute("addTag", addTag);
		return "addTagToTask";
	}
	
	@RequestMapping(value = {"/tasks/addTagToTask/{tagID}"}, method= RequestMethod.POST)
	public String addTagToTask(@Valid @ModelAttribute("addTag") AddTag addTag, @PathVariable("tagID") Long tagID, Model model) {
		Task task = this.taskService.getTask(addTag.getTaskID());
		List<Tag> tagList = task.getTagList();
		Tag tag = this.tagService.getTag(tagID);
		tagList.add(tag);
		task.setTagList(tagList);
		this.taskService.saveTask(task);
		
		return "redirect:/tasks/" + task.getId();
	}
	
	@RequestMapping(value= {"/tasks/addComment"}, method=RequestMethod.POST)
	public String addComment(@Valid @ModelAttribute("addComment") AddComment addComment, BindingResult brComment, Model model) {
		Comment comment = addComment.getComment();
		Task task = this.taskService.getTask(addComment.getTaskId());
		this.commentValidator.validate(comment, brComment);
		
		if(!brComment.hasErrors()) {
			
			User loggedUser = this.sessionData.getLoggedUser();
			comment.setTask(task);
			comment.setAuthor(loggedUser);
			this.commentService.saveComment(comment);
		}
		return "redirect:/tasks/" + task.getId();
	}
	
	@RequestMapping(value= {"/comment/delete/{commentID}"}, method=RequestMethod.GET)
	public String deleteComment(Model model, @PathVariable("commentID") Long commentID) {
		User loggedUser = this.sessionData.getLoggedUser();
		Comment comment = this.commentService.getComment(commentID);
		Task task = comment.getTask();
		if(loggedUser.equals(comment.getAuthor()))
			this.commentService.deleteComment(comment);
		return "redirect:/tasks/"+task.getId();
	}
	@RequestMapping(value= {"/tag/delete/{tagID}"}, method=RequestMethod.GET)
	public String deleteTag(Model model, @PathVariable("tagID") Long tagID) {
		Tag tag = this.tagService.getTag(tagID);
		Project project = tag.getProject();
		User projectOwner = project.getOwner();
		User loggedUser = this.sessionData.getLoggedUser();
		if(loggedUser.equals(projectOwner))
			this.tagService.deleteTag(tag);
		return "redirect:/projects/"+project.getId();
	}
	
	@RequestMapping(value= {"/task/removeTag/{taskID}/{tagID}"}, method=RequestMethod.GET )
	public String removeTag(Model model, @PathVariable("taskID") Long taskID, @PathVariable("tagID") Long tagID) {
		Task task = this.taskService.getTask(taskID);
		User loggedUser = this.sessionData.getLoggedUser();
		if(loggedUser.equals(task.getUser())) {
			task.getTagList().remove(this.tagService.getTag(tagID));
			this.taskService.saveTask(task);
		}
		return "redirect:/tasks/"+task.getId();
	}
//	public String shareTaskPost(...)
	//DEVO RESETTARE DB
}
