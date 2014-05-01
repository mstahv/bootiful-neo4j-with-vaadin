package org.vaadin.neo4j.vaadin;

import java.util.Set;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.domain.Person;
import org.vaadin.maddon.form.AbstractForm;
import org.vaadin.maddon.form.AbstractForm.SavedHandler;
import org.vaadin.neo4j.PersonRepository;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;

@UIScope
@Component
public class PersonFormController implements SavedHandler<Person>,
        AbstractForm.ResetHandler<Person> {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    GraphDatabaseService graphDatabase;

    @Autowired
    EventBus eventBus;

    @Override
    public void onSave(Person entity) {
        try (Transaction tx = graphDatabase.beginTx()) {
            Set<Person> teammates = entity.getTeammates();
            Person saved = personRepository.save(entity);
            // TODO figure out why relations needs to be set on "fresh" object and
            // resaved, must be doing something wrong.
            saved.setTeammates(teammates);
            personRepository.save(saved);
            tx.success();
        }
        eventBus.publish(EventScope.UI, this, "DB updated");
    }

    @Override
    public void onReset(Person entity) {
    }

}
