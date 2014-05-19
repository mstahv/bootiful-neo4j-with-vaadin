package org.vaadin.neo4j.vaadin;

import org.vaadin.neo4j.vaadin.events.PersonsModified;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.domain.Person;
import org.vaadin.maddon.fields.MTable;
import org.vaadin.maddon.label.RichText;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.neo4j.AppService;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;

@Component
@UIScope
class PersonView extends MVerticalLayout {

    @Autowired
    AppService personService;

    @Autowired
    PersonForm personForm;

    @Autowired
    EventBus eventBus;

    MTable<Person> listing = new MTable<>(Person.class).
            withProperties("name", "x", "y");

    public PersonView() {
        setCaption("Persons");
        listing.addMValueChangeListener(event -> {
            if (event.getValue() != null) {
                personForm.setEntity(event.getValue());
                listing.setValue(null);
            }
        });

        addComponents(
                new RichText().withMarkDownResource("/personsview.md"),
                listing
        );
        expand(listing).withFullHeight();

    }

    @PostConstruct
    void init() {
        listPersons();
        eventBus.subscribe(new EventBusListener<PersonsModified>() {

            @Override
            public void onEvent(
                    org.vaadin.spring.events.Event<PersonsModified> event) {
                listPersons();
            }
        });
    }

    void listPersons() {
        listing.setBeans(personService.allAsList());
    }

}
