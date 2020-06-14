package it.uniroma3.siw.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.model.Credentials;
import it.uniroma3.siw.model.User;

public interface CredentialsRepository extends CrudRepository<Credentials, Long> {

    /**
     * Retrieve Credentials by its username
     * @param username the username of the Credentials to retrieve
     * @return an Optional for the Credentials with the passed username
     */
    public Optional<Credentials> findByUserName(String username);

	public Optional<Credentials> findByUser(User user);

}
