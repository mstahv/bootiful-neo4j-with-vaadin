package org.vaadin.domain;

import org.springframework.data.neo4j.annotation.NodeEntity;

@NodeEntity
public class Project extends AbstractPositionableEntity {

    private String name;

    public Project() {
    }

    public Project(String name, int x, int y) {
        super(x, y);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String projectName) {
        this.name = projectName;
    }

    @Override
    public String toString() {
        return "Project: "+ name;
    }

}
