## Bootiful Neo4J with Vaadin

Work in progress [example/demo/learning app](https://github.com/mstahv/bootiful-neo4j-with-vaadin) with 
[Vaadin](https://vaadin.com/home) + [Neo4J](http://www.neo4j.org) + 
[Spring Data](http://projects.spring.io/spring-data/) + 
[Spring Boot](http://projects.spring.io/spring-boot/) + 
[Vaadin4Spring](https://github.com/peholmst/vaadin4spring).

There is a Vaadin UI to edit entities (Person, Project) persisted in a Neo4j backend. As such this project works as a CRUD example for the above stack.

There is also a bit hackish "visual editor" mode on the last tab that uses [AlloYUI Diagram Builder](http://alloyui.com/examples/diagram-builder/) to viasualize, and also modify the data. Due to its experimental Vaadin wrapper, saving entities back form the DiagramBuilder completely rewrites the database, so don't use that as a bases for you mission critical app just yet :-)

### Checking out, launching and playing with the project

Note, this is a technology demo using modern tools, so it uses a modern version of Java as well. In case you still don't have [Java 8](https://jdk8.java.net) installed on your workstation, do it **NOW**. If, for some reason, you still can't use Java 8 in production, note that core technologies used in the app still work fine with Java 7 as well.

The project is Maven built Spring Boot project. This makes it really simple to test the application locally and importing it to your favorite IDE. After you have checked out the project and imported into your IDE. Execute *mvn install* once to compile the project, including the custom widgetset needed for Diagram Builder component. The just run the plain old *main method* from the *Application* class.

In case you wish to deploy the project as war file to an application server, the app is configured to build one by default. Just deploy the war file to you application server that runs on Java 8.

### TODO

 * Improve diagram-builder to support identifiers, now updates from there rebuild the whole DB content.


