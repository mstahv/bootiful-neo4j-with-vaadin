package org.vaadin.neo4j.vaadin;

import org.vaadin.neo4j.vaadin.events.ProjectsChangedNotifier;
import com.vaadin.spring.annotation.UIScope;
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
import org.vaadin.domain.Project;
import org.vaadin.neo4j.AppService;
import org.vaadin.neo4j.vaadin.events.ProjectsModified;
//import org.vaadin.spring.events.EventBus;
//import org.vaadin.spring.events.EventBusListener;
import org.vaadin.viritin.ListContainer;
import org.vaadin.viritin.MBeanFieldGroup;
import org.vaadin.viritin.fields.MTextField;
import org.vaadin.viritin.form.AbstractForm;
import org.vaadin.viritin.layouts.MVerticalLayout;

@org.springframework.stereotype.Component
@UIScope
public class PersonForm extends AbstractForm<Person> {

    TextField name = new MTextField("Name");

    TextField x = new MTextField("x");

    TextField y = new MTextField("y");

    Table projects = new Table("Projects");

    @Autowired
    PersonFormController personFormController;

    @Autowired
    AppService service;

    @Autowired
    ProjectsChangedNotifier eventBus;
//    EventBus.SessionEventBus eventBus;

    @PostConstruct
    void init() {

        setSavedHandler(personFormController);
        setResetHandler(personFormController);

        projects.setMultiSelect(true);
        projects.setSelectable(true);
        projects.setPageLength(6);

        populateProjects();
        
        eventBus.subscribe(this::populateProjects);
        
//
//        eventBus.subscribe(new EventBusListener<ProjectsModified>() {
//
//            @Override
//            public void onEvent(
//                    org.vaadin.spring.events.Event<ProjectsModified> event) {
//                populateProjects();
//            }
//        });

    }

    private void populateProjects() {
        projects.setContainerDataSource(
                new ListContainer(Project.class, service.listAllProjects()),
                Collections.singletonList("name"));
    }

    @Override
    protected Component createContent() {
        return new MVerticalLayout(
                new FormLayout(
                        name,
                        x,
                        y,
                        projects
                ),
                getToolbar()
        );
    }

    public PersonForm() {
    }

    @Override
    public MBeanFieldGroup<Person> setEntity(Person entity) {
        final MBeanFieldGroup<Person> setEntity = super.setEntity(entity);
        openInModalPopup();
        return setEntity;
    }

    @Override
    protected void save(Button.ClickEvent e) {
        super.save(e);
        getPopup().close();
    }

    @Override
    protected void reset(Button.ClickEvent e) {
        super.reset(e);
        getPopup().close();
    }

}
