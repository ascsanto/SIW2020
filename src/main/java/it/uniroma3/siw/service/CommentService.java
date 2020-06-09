package it.uniroma3.siw.service;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.model.Comment;
import it.uniroma3.siw.repository.CommentRepository;

@Service
public class CommentService {

    @Autowired
    private CommentRepository tagRepository;

    @Transactional
    public Comment getTag(long id){
        Optional<Comment> comment = tagRepository.findById(id);
        return comment.orElse(null);
    }
    @Transactional
    public Comment saveTag(Comment comment){
        return this.tagRepository.save(comment);
    }
    @Transactional
    public void deleteTag(Comment comment){
        this.tagRepository.delete(comment);
    }


}
