package it.uniroma3.siw.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Tag;
import it.uniroma3.siw.repository.TagRepository;

@Service
public class TagService {

    @Autowired
    private TagRepository tagRepository;

    @Transactional
    public Tag getTag(long id){
        Optional<Tag> tag = tagRepository.findById(id);
        return tag.orElse(null);
    }
    @Transactional
    public Tag saveTag(Tag tag){
        return this.tagRepository.save(tag);
    }
    @Transactional
    public void deleteTag(Tag tag){
        this.tagRepository.delete(tag);
    }


}