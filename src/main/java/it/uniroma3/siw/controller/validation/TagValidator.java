package it.uniroma3.siw.controller.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Tag;
import it.uniroma3.siw.model.User;

@Component
public class TagValidator implements Validator{
	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 2;
	final Integer MAX_DESCRIPTION_LENGTH = 1000;
	
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors errors) {
		Tag tag = (Tag) o;
		String name = tag.getTagName().trim();
		String description = tag.getDescription().trim();
		
		 if (name.isBlank())
	           errors.rejectValue("name", "required");
	     else if(name.length()<MIN_NAME_LENGTH||name.length()>MAX_NAME_LENGTH)
	        	errors.rejectValue("name", "size");
		 if(description.length()>MAX_DESCRIPTION_LENGTH)
	        	errors.rejectValue("description", "size");
	}

}
