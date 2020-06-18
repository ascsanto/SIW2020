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
    private CommentRepository commentRepository;

    @Transactional
    public Comment getComment(long id){
        Optional<Comment> comment = commentRepository.findById(id);
        return comment.orElse(null);
    }
    @Transactional
    public Comment saveComment(Comment comment){
        return this.commentRepository.save(comment);
    }
    @Transactional
    public void deleteComment(Comment comment){
        this.commentRepository.delete(comment);
    }


}
