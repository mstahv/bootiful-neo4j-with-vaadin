package org.vaadin.neo4j.vaadin;

import org.vaadin.neo4j.vaadin.events.PersonsModified;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.domain.Person;
import org.vaadin.maddon.form.AbstractForm;
import org.vaadin.maddon.form.AbstractForm.SavedHandler;
import org.vaadin.neo4j.AppService;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventScope;

@UIScope
@Component
public class PersonFormController implements SavedHandler<Person>,
        AbstractForm.ResetHandler<Person> {

    @Autowired
    AppService personService;

    @Autowired
    EventBus eventBus;

    @Override
    public void onSave(Person entity) {
        personService.save(entity);
        eventBus.publish(EventScope.UI, this, new PersonsModified());
    }

    @Override
    public void onReset(Person entity) {
    }

}
