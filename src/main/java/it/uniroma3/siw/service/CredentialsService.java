package it.uniroma3.siw.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;
import it.uniroma3.siw.repository.CredentialsRepository;

@Service
public class CredentialsService {

    @Autowired
    protected CredentialsRepository credentialsRepository;

    @Autowired
    protected PasswordEncoder passwordEncoder;

    /**
     * This method retrieves an Credentials from the DB based on its ID.
     * @param id the id of the Credentials to retrieve from the DB
     * @return the retrieved Credentials, or null if no Credentials with the passed ID could be found in the DB
     */
    @Transactional
    public Credentials getCredentials(long id) {
        Optional<Credentials> result = this.credentialsRepository.findById(id);
        return result.orElse(null);
    }
    
    /**
     * This method retrieves an Credentials from the DB based on its username.
     * @param username the username of the Credentials to retrieve from the DB
     * @return the retrieved Credentials, or null if no Credentials with the passed username could be found in the DB
     */
    @Transactional
    public Credentials getCredentials(String username) {
        Optional<Credentials> result = this.credentialsRepository.findByUserName(username);
        return result.orElse(null);
    }
    
     @Transactional
     public Credentials getCredentials(User user) {
    	 Optional<Credentials> result = this.credentialsRepository.findByUser(user);
         return result.orElse(null);
     }

    /**
     * This method saves an Credentials in the DB.
     * Before saving it, it sets the Credentials role to DEFAULT, and encrypts the password.
     * @param credentials the Credentials to save into the DB
     * @return the saved Credentials
     * @throws DataIntegrityViolationException if an Credentials with the same username
     *                                  as the passed Credentials already exists in the DB
     */
    @Transactional
    public Credentials saveCredentials(Credentials credentials) {
        credentials.setRole(Credentials.DEFAULT_ROLE);
       credentials.setPassword(this.passwordEncoder.encode(credentials.getPassword()));
        return this.credentialsRepository.save(credentials);
    }

    /**
     * This method retrieves all Credentials from the DB.
     * @return a List with all the retrieved Credentials
     */
    @Transactional
    public List<Credentials> getAllCredentials() {
        List<Credentials> result = new ArrayList<>();
        Iterable<Credentials> iterable = this.credentialsRepository.findAll();
        for(Credentials credentials : iterable)
            result.add(credentials);
        return result;
    }
    
    public void matchPasswords(String pw1, String pw2, Errors errors) {
    	String pw1Enc = this.passwordEncoder.encode(pw1);
    	String pw2Enc = this.passwordEncoder.encode(pw2);
    	if(!pw1Enc.equals(pw2Enc))
    		errors.rejectValue("oldpassword", "match");
    	
    }
}
