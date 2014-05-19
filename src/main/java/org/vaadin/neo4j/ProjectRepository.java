package org.vaadin.neo4j;

import org.springframework.data.neo4j.repository.GraphRepository;
import org.vaadin.domain.Project;

public interface ProjectRepository extends GraphRepository<Project> {

    Project findByName(String name);
    
}
