package org.vaadin.neo4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.vaadin.domain.Person;

@Repository
@Scope(value = "singleton")
public class PersonService {

    @Autowired
    PersonRepository personRepository;

    @Transactional
    public List<Person> allAsList() {
        return personRepository.findAll(new Sort("name")).as(ArrayList.class);
    }

    @Transactional
    public void saveWithConnections(Person entity) {
        Set<Person> teammates = entity.getTeammates();
        Person saved = personRepository.save(entity);
        // TODO figure out why relations needs to be set on "fresh" object and
        // resaved, must be doing something wrong. Without this hack all 
        // relations are lost.
        saved.setTeammates(teammates);
        personRepository.save(saved);

    }

}
