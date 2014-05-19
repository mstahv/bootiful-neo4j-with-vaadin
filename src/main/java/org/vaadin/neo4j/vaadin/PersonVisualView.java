package org.vaadin.neo4j.vaadin;

import com.vaadin.ui.Button;
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
import org.vaadin.maddon.layouts.MVerticalLayout;
import org.vaadin.neo4j.PersonRepository;
import org.vaadin.neo4j.PersonService;
import org.vaadin.spring.UIScope;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.EventBusListener;
import org.vaadin.spring.events.EventScope;

@Component
@UIScope
class PersonVisualView extends MVerticalLayout {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PersonService personService;

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

    public PersonVisualView() {
        setCaption("Visual editor");
        diagramBuilder.setSizeFull();
        addComponents(saveButton, diagramBuilder);
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
    }

    private void drawState() {
        ArrayList<Node> nodes = new ArrayList<>();
        ArrayList<Transition> transitions = new ArrayList<>();

        List<Person> allAsList = personService.allAsList();

        for (Person person : allAsList) {
            nodes.add(new Node(person.getName(), "condition", person.
                    getX(),
                    person.getY()));

            for (Person mate : person.getTeammates()) {
                transitions.add(new Transition(person.getName(), mate.
                        getName(), "worksWith"));
            }
        }

        diagramBuilder.setAvailableFields(new NodeType(
                "diagram-node-condition-icon", "Person",
                "condition"));
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
            List<Node> nodes = event.getNodes();
            // first save all Persons
            for (Node node : nodes) {
                Person person = new Person(node.getName(), node.getX(), node.
                        getY());
                personRepository.save(person);
            }
            // set connections
            for (Node node : nodes) {
                Person person = personRepository.findByName(node.getName());
                List<Transition> transitions = node.getTransitions();
                for (Transition transition : transitions) {
                    Person target = personRepository.findByName(transition.
                            getTarget());
                    person.worksWith(target);
                }
                personRepository.save(person);
            }
            tx.success();
        }

        eventBus.publish(EventScope.UI, this, new PersonsModified());
    }

}
