package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {

}