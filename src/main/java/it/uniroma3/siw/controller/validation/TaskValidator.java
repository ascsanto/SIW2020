package it.uniroma3.siw.controller.validation;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import it.uniroma3.siw.model.Task;
import it.uniroma3.siw.model.User;
@Component
public class TaskValidator implements Validator {

	final Integer MAX_NAME_LENGTH = 100;
	final Integer MIN_NAME_LENGTH = 2;
	final Integer MAX_DESCRIPTION_LENGTH = 1000;
	@Override
	public boolean supports(Class<?> clazz) {
		 return User.class.equals(clazz);
	}

	@Override
	public void validate(Object o, Errors errors) {
		Task task = (Task) o;
		 String name = task.getName().trim();
	        String description = task.getDescription().trim();

	        if (name.isBlank())
	            errors.rejectValue("task.name", "required");
	        else if(name.length()<MIN_NAME_LENGTH||name.length()>MAX_NAME_LENGTH)
	        	errors.rejectValue("task.name", "namesize");
	        if(description.length()>MAX_DESCRIPTION_LENGTH)
	        	errors.rejectValue("task.description", "descsize");
	        
	    }
	}


