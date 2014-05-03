package org.vaadin.neo4j.vaadin;

import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.domain.Person;
import org.vaadin.maddon.fields.MTable;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.neo4j.PersonService;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;

@Component
@UIScope
class PersonView extends MVerticalLayout {

    @Autowired
    PersonService personService;

    @Autowired
    PersonForm personForm;

    @Autowired
    EventBus eventBus;

    MTable<Person> listing = new MTable<>(Person.class).
            withProperties("name", "x", "y");

    public PersonView() {
        setCaption("Person listing");
        listing.addMValueChangeListener(event -> {
            if (event.getValue() != null) {
                personForm.setEntity(event.getValue());
                listing.setValue(null);
            }
        });

        addComponents(listing);
        expand(listing).withFullHeight();

    }

    @PostConstruct
    void init() {
        listPersons();
        eventBus.subscribe(new EventBusListener<Object>() {

            @Override
            public void onEvent(org.vaadin.spring.events.Event<Object> event) {
                listPersons();
            }
        });
    }

    void listPersons() {
        listing.setBeans(personService.allAsList());
    }

}
