package org.vaadin.neo4j.vaadin;

import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.vaadin.diagrambuilder.DiagramBuilder;
import org.vaadin.diagrambuilder.DiagramStateEvent;
import org.vaadin.diagrambuilder.Node;
import org.vaadin.diagrambuilder.NodeType;
import org.vaadin.diagrambuilder.Transition;
import org.vaadin.domain.Person;
import org.vaadin.domain.Project;
import org.vaadin.maddon.label.RichText;
import org.vaadin.maddon.layouts.MHorizontalLayout;
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.neo4j.AppService;
import org.vaadin.neo4j.PersonRepository;
import org.vaadin.neo4j.ProjectRepository;
import org.vaadin.neo4j.vaadin.events.PersonsModified;
import org.vaadin.neo4j.vaadin.events.ProjectsModified;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;
import org.vaadin.spring.events.EventScope;

@Component
@UIScope
class VisualEditor extends MVerticalLayout {

    @Autowired
    PersonRepository personRepository;
    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    AppService personService;

    @Autowired
    GraphDatabaseService graphDatabase;

    @Autowired
    EventBus eventBus;

    DiagramBuilder diagramBuilder = new DiagramBuilder();

    Button saveButton = new Button("Save back to Neo4J DB",
            new Button.ClickListener() {

                @Override
                public void buttonClick(Button.ClickEvent event) {
                    diagramBuilder.getDiagramState(
                            new DiagramBuilder.StateCallback() {

                                @Override
                                public void onStateReceived(
                                        DiagramStateEvent event) {
                                            updateFromDiagram(event);
                                        }
                            });
                }
            });

    public VisualEditor() {
        setCaption("Visual editor");
        diagramBuilder.setSizeFull();
        RichText info = new RichText().withMarkDownResource("/visual.md");
        addComponents(
                new MHorizontalLayout(saveButton, info).expand(info),
                diagramBuilder);
        expand(diagramBuilder).withFullHeight();
    }

    @PostConstruct
    public void init() {
        drawState();
        eventBus.subscribe(new EventBusListener<PersonsModified>() {

            @Override
            public void onEvent(
                    org.vaadin.spring.events.Event<PersonsModified> event) {
                drawState();
            }
        });
        eventBus.subscribe(new EventBusListener<ProjectsModified>() {

            @Override
            public void onEvent(
                    org.vaadin.spring.events.Event<ProjectsModified> event) {
                drawState();
            }
        });
    }

    /**
     * Maps the state from graph db to the diagram builder
     */
    private void drawState() {

        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Transition> transitions = new ArrayList<>();

        List<Project> projects = personService.listAllProjects();
        for (Project project : projects) {
            nodes.add(new Node(project.getName(), "task", project.
                    getX(),
                    project.getY()));

        }

        List<Person> allAsList = personService.allAsList();

        for (Person person : allAsList) {
            nodes.add(new Node(person.getName(), "state", person.
                    getX(),
                    person.getY()));

            for (Project mate : person.getProjects()) {
                transitions.add(new Transition(person.getName(), mate.getName(),
                        "worksIn"));
            }
        }

        diagramBuilder.setAvailableFields(
                new NodeType("diagram-node-state-icon", "Person", "state"),
                new NodeType("diagram-node-task-icon", "Project", "task")
        );
        diagramBuilder.setFields(nodes.toArray(new Node[0]));
        diagramBuilder.setTransitions(transitions.toArray(new Transition[0]));
    }

    void updateFromDiagram(DiagramStateEvent event) {
        try (Transaction tx = graphDatabase.beginTx()) {
            /*
             * TODO enhance diagram-builder to support identifiers, then could update
             * existing nodes instead of deleteAll + rebuild whole model. Now name 
             * can change!!
             */
            personRepository.deleteAll();
            projectRepository.deleteAll();
            List<Node> nodes = event.getNodes();
            // first save all Persons
            for (Node node : nodes) {
                if (node.getType().equals("task")) {
                    Project project = new Project(node.getName(), node.getX(),
                            node.getY());
                    projectRepository.save(project);
                } else {
                    Person person = new Person(node.getName(), node.getX(),
                            node.
                            getY());
                    personRepository.save(person);
                }
            }
            // set connections
            for (Node node : nodes) {
                if (node.getType().equals("state")) {
                    Person person = personRepository.findByName(node.getName());
                    for (Transition transition : node.getTransitions()) {
                        Project target = projectRepository.findByName(
                                transition.getTarget());
                        if (target == null) {
                            Notification.show(
                                    "Person to person relations are not supported",
                                    Notification.Type.WARNING_MESSAGE);
                        } else {
                            person.worksIn(target);
                        }
                    }
                    personRepository.save(person);
                } else {
                    // also support connections with "wrong direction"
                    Project project = projectRepository.findByName(node.
                            getName());
                    for (Transition transition : node.getTransitions()) {
                        Person target = personRepository.findByName(
                                transition.getTarget());
                        if (target == null) {
                            Notification.show(
                                    "Project to project relations are not supported",
                                    Notification.Type.WARNING_MESSAGE);
                        } else {
                            target.worksIn(project);
                        }
                        personRepository.save(target);
                    }
                }
            }

            tx.success();
        }

        eventBus.publish(EventScope.UI, this, new PersonsModified());
        eventBus.publish(EventScope.UI, this, new ProjectsModified());
    }

}
