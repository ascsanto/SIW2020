package it.uniroma3.siw.model;

public class AddTask {
private Task task;
private Long projectId;
private String username;
public Task getTask() {
	return task;
}
public void setTask(Task task) {
	this.task = task;
}
public Long getProjectId() {
	return projectId;
}
public void setProjectId(Long projectId) {
	this.projectId = projectId;
}
public String getUsername() {
	return username;
}
public void setUsername(String username) {
	this.username = username;
}
}
