package org.vaadin.neo4j.vaadin;

import com.vaadin.server.FontAwesome;
import com.vaadin.ui.Button;
import java.util.List;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.domain.Project;
import org.vaadin.maddon.button.MButton;
import org.vaadin.maddon.button.PrimaryButton;
import org.vaadin.maddon.fields.MTable;
import org.vaadin.maddon.label.RichText;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.neo4j.AppService;
import org.vaadin.neo4j.ProjectRepository;
import org.vaadin.neo4j.vaadin.events.ProjectsModified;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;

@Component
@UIScope
class ProjectView extends MVerticalLayout {

    @Autowired
    AppService service;

    @Autowired
    ProjectRepository repo;

    @Autowired
    EventBus eventBus;

    private List<Project> allProjects;

    MTable<Project> listing = new MTable<>(Project.class).
            withProperties("name");
    
    @Autowired
    EventBus bus;

    Button saveChanges = new MButton(FontAwesome.FLOPPY_O, e -> {
        repo.save(allProjects);
        bus.publish(ProjectView.this, new ProjectsModified());
    });
    
    Button addNew = new MButton(FontAwesome.PLUS, e->{
        Project p = new Project("-- New Project --", 250, 5);
        listing.addBeans(p);
        listing.setCurrentPageFirstItemId(p);
    });
    
    Button reset = new MButton(FontAwesome.UNDO, e->{
        listGroups();
    });

    public ProjectView() {
        setCaption("Projects");
        listing.setEditable(true);
        addComponents(
                new RichText().withMarkDownResource("/projectsview.md"),
                new MHorizontalLayout(saveChanges,addNew,reset),
                listing
        );
        expand(listing).withFullHeight();

    }

    @PostConstruct
    void init() {
        listGroups();
        eventBus.subscribe(new EventBusListener<ProjectsModified>() {

            @Override
            public void onEvent(
                    org.vaadin.spring.events.Event<ProjectsModified> event) {
                listGroups();
            }
        });
    }

    void listGroups() {
        allProjects = service.listAllProjects();
        listing.setBeans(allProjects);
    }

}
