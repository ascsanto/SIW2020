package it.uniroma3.siw.controller.validation;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Comment;
import it.uniroma3.siw.model.User;
@Component
public class CommentValidator implements Validator{
	final Integer MAX_COMMENT_LENGTH = 1000;
	@Override
	public boolean supports(Class<?> clazz) {
		 return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Comment comment= (Comment) target;
		if(comment.getCommentText().length()==0)
			errors.rejectValue("commentText", "tooshort");
		if(comment.getCommentText().length()>MAX_COMMENT_LENGTH)
			errors.rejectValue("commentText", "toolong");
		
	}

}
