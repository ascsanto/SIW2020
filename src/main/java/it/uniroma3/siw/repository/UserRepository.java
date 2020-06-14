package it.uniroma3.siw.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Project;
import it.uniroma3.siw.model.User;

public interface UserRepository extends CrudRepository<User, Long> {

    /**
     * Retrieve all Users that have visibility over the passed project
     * @param project The Project to find the members of
     * @return the List of Users that have visibility over the passed Project
     */
    public List<User> findByVisibleProjects(Project project);

}