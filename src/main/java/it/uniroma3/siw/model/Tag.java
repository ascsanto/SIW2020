package it.uniroma3.siw.model;

import javax.persistence.*;
import java.util.List;

@Entity
public class Tag {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String tagName;

    @Column(nullable = false)
    private String color;

    @Column
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToMany
    private List<Task> taskList;

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
