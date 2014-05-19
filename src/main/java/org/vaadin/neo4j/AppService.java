package org.vaadin.neo4j;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.domain.Person;
import org.vaadin.domain.Project;

@Service
@Transactional
public class AppService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    ProjectRepository projectRepository;

    public List<Person> allAsList() {
        return personRepository.findAll(new Sort("name")).as(ArrayList.class);
    }

    public List<Project> listAllProjects() {
        return projectRepository.findAll(new Sort("projectName")).as(ArrayList.class);
    }

    public void save(Person entity) {
        personRepository.save(entity);
    }

}
