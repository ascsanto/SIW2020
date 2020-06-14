package it.uniroma3.siw.model;

public class EditProject {
private String name;
private String description;
private Long projectId;
public Long getProjectId() {
	return projectId;
}
public void setProjectId(Long projectId) {
	this.projectId = projectId;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getDescription() {
	return description;
}
public void setDescription(String description) {
	this.description = description;
}
}
