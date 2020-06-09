package it.uniroma3.siw.repository;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Tag;

public interface TagRepository  extends CrudRepository<Tag, Long> {
}
