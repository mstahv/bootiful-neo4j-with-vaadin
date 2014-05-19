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
import org.vaadin.domain.Project;
import org.vaadin.maddon.ListContainer;
import org.vaadin.maddon.fields.MTextField;
import org.vaadin.maddon.form.AbstractForm;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.neo4j.AppService;
import org.vaadin.neo4j.vaadin.events.ProjectsModified;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.VaadinComponent;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;

@UIScope
@VaadinComponent
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
    EventBus eventBus;

    private Window window;

    @PostConstruct
    void init() {

        setSavedHandler(personFormController);
        setResetHandler(personFormController);

        projects.setMultiSelect(true);
        projects.setSelectable(true);
        projects.setPageLength(6);

        populateProjects();

        eventBus.subscribe(new EventBusListener<ProjectsModified>() {

            @Override
            public void onEvent(
                    org.vaadin.spring.events.Event<ProjectsModified> event) {
                populateProjects();
            }
        });

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
