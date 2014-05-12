## Bootiful Neo4J with Vaadin

Work in progress [example/demo/learning app](https://github.com/mstahv/bootiful-neo4j-with-vaadin) with 
[Vaadin](https://vaadin.com/home) + [Neo4J](http://www.neo4j.org) + 
[Spring Data](http://projects.spring.io/spring-data/) + 
[Spring Boot](http://projects.spring.io/spring-boot/) + 
[Vaadin4Spring](https://github.com/peholmst/vaadin4spring).

There is both standard Vaadin "form" that can be used to edit existing Person objects in database, including their "relations". As such this project works as a small CRUD example for the above stack.

There is also a bit hackish "visual editor" mode on the other tab that uses [AlloYUI Diagram Builder](http://alloyui.com/examples/diagram-builder/) to modify the model.

### TODO

 * Improve diagram-builder to support identifiers, now updates from there rebuild the whole DB content.
 * Change relations between Persons to contain properties to better illustrate Neo4J goodies
 * Add an other entity type and relations to it, e.g. WorkGroup or Company


