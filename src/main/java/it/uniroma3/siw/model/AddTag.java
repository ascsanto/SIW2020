package it.uniroma3.siw.model;

import java.util.List;

public class AddTag {

	private Long taskID;
	private List<Tag> tagList;
	public Long getTaskID() {
		return taskID;
	}
	public void setTaskID(Long taskID) {
		this.taskID = taskID;
	}
	public List<Tag> getTagList() {
		return tagList;
	}
	public void setTagList(List<Tag> tagList) {
		this.tagList = tagList;
	}
}
