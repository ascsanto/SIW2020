package it.uniroma3.siw.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.uniroma3.siw.model.Project;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.ProjectRepository;

@Service
public class ProjectService { @Autowired
protected ProjectRepository projectRepository;

   
    @Transactional
    public Project getProject(long id) {
        Optional<Project> result = this.projectRepository.findById(id);
        return result.orElse(null);
    }

   
    @Transactional
    public Project saveProject(Project project) {
        return this.projectRepository.save(project);
    }

   
    @Transactional
    public void deleteProject(Project project) {
        this.projectRepository.delete(project);
    }

    
    @Transactional
    public Project shareProjectWithUser(Project project, User user) {
        project.addMember(user);
        return this.projectRepository.save(project);
    }
    @Transactional
    public List<Project> retrieveProjectsOwnedBy(User user){
    	  return this.projectRepository.findByOwner(user);
    }
    
    @Transactional
    public List<Project> retrieveSharedProjects(User user){
    	return this.projectRepository.findByMembers(user);
    }
}

