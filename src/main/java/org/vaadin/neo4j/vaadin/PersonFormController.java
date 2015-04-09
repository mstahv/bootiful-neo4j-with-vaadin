package org.vaadin.neo4j.vaadin;

import com.vaadin.spring.annotation.UIScope;
import org.vaadin.neo4j.vaadin.events.PersonsModified;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.domain.Person;
import org.vaadin.neo4j.AppService;
import org.vaadin.neo4j.vaadin.events.PersonsChangedNotifier;
//import org.vaadin.spring.events.EventBus;
//import org.vaadin.spring.events.EventScope;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.form.AbstractForm.SavedHandler;

@UIScope
@Component
public class PersonFormController implements SavedHandler<Person>,
        AbstractForm.ResetHandler<Person> {

    @Autowired
    AppService personService;

    @Autowired
    PersonsChangedNotifier eventBus;
//    @Autowired
//    EventBus.SessionEventBus eventBus;

    @Override
    public void onSave(Person entity) {
        personService.save(entity);
        eventBus.onEvent();
//        eventBus.publish(EventScope.UI, this, new PersonsModified());
    }

    @Override
    public void onReset(Person entity) {
    }

}
