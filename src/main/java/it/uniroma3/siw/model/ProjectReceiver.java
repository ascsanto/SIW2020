package it.uniroma3.siw.model;

public class ProjectReceiver {
 private String username;
 private Long sharedProjectId;
public Long getSharedProjectId() {
	return sharedProjectId;
}

public void setSharedProjectId(Long sharedProjectId) {
	this.sharedProjectId = sharedProjectId;
}

public String getUsername() {
	return username;
}

public void setUsername(String username) {
	this.username = username;
}
}
