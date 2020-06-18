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
import it.uniroma3.siw.controller.validation.TagValidator;
import it.uniroma3.siw.model.Project;
import it.uniroma3.siw.model.Tag;
import it.uniroma3.siw.service.ProjectService;
import it.uniroma3.siw.service.TagService;

@Controller
public class TagController {

	@Autowired
	TagValidator tagValidator;
	
	@Autowired
	TagService tagService;
	
	@Autowired
	ProjectService projectService;
	
	@Autowired
	SessionData sessionData;
	
	
	
	
	@RequestMapping(value= {"/projects/addTag/{projectID}"}, method = RequestMethod.GET)
	public String addTagGet(Model model, @PathVariable("projectID") Long id) {
		Tag tag = new Tag();
		tag.setProject(this.projectService.getProject(id));
		model.addAttribute("tag", tag);
		return "addTag";
	}
	@RequestMapping(value= {"/projects/addTag"}, method = RequestMethod.POST)
	public String addTagPost(@Valid @ModelAttribute ("tag") Tag tag,BindingResult bindingResult, Model model ){
		this.tagValidator.validate(tag, bindingResult);
		if(!bindingResult.hasErrors()) {
			Project pj = tag.getProject();
			List<Tag> tagList = pj.getTagList();
			tagList.add(tag);
			this.tagService.saveTag(tag);
			this.projectService.saveProject(pj);
			return "redirect:/projects/"+pj.getId();
		}
		model.addAttribute("tag", tag);
		return "addTag";
	}
}
