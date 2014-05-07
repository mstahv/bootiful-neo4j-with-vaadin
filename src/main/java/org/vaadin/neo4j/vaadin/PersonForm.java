package org.vaadin.neo4j.vaadin;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import java.util.Collections;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.domain.Person;
import org.vaadin.maddon.ListContainer;
import org.vaadin.maddon.fields.MTextField;
import org.vaadin.maddon.form.AbstractForm;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.neo4j.PersonService;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;

@UIScope
@VaadinComponent
public class PersonForm extends AbstractForm<Person> {

    TextField name = new MTextField("name");

    TextField x = new MTextField("x");

    TextField y = new MTextField("y");

    // Select to another entity, options must be populated!!
    Table teammates = new Table("Teammates");

    @Autowired
    PersonFormController personFormController;

    @Autowired
    PersonService personService;

    @Autowired
    EventBus eventBus;

    private Window window;

    @PostConstruct
    void init() {

        setSavedHandler(personFormController);
        setResetHandler(personFormController);

        teammates.setMultiSelect(true);
        teammates.setSelectable(true);
        teammates.setPageLength(6);

        populatePersons();
        
        eventBus.subscribe(new EventBusListener<Object>(){

            @Override
            public void onEvent(org.vaadin.spring.events.Event<Object> event) {
                if(event.getPayload().equals("DB updated")) {
                    populatePersons();
                }
            }
        });

    }

    private void populatePersons() {
        teammates.setContainerDataSource(
                new ListContainer(Person.class, personService.allAsList()), 
                Collections.singletonList("name"));
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new FormLayout(
                        name,
                        x,
                        y,
                        teammates
                ),
                getToolbar()
        );
    }

    public PersonForm() {
    }

    @Override
    public void setEntity(Person entity) {
        super.setEntity(entity);
        showInWindow();
    }

    private void showInWindow() {
        window = new Window("Edit person", this);
        window.setModal(true);
        window.setClosable(false);
        UI.getCurrent().addWindow(window);
    }

    @Override
    protected void save(Button.ClickEvent e) {
        super.save(e);
        window.close();
    }

    @Override
    protected void reset(Button.ClickEvent e) {
        super.reset(e);
        window.close();
    }

}
