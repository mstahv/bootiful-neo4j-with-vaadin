package org.vaadin.neo4j;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.vaadin.domain.Person;
import javax.annotation.PostConstruct;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.kernel.impl.util.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;

@Configuration
@EnableNeo4jRepositories("org.vaadin.neo4j")
public class Neo4JConf extends Neo4jConfiguration {

    private static final String DBNAME = "bootiful-neo4j-with-vaadin.db";

    public Neo4JConf() {
        setBasePackage("org.vaadin.domain");
        try {
            // reset neo4jdb, this is a demo app...
            FileUtils.deleteRecursively(new File(DBNAME));
        } catch (IOException ex) {
            Logger.getLogger(Neo4JConf.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }

    @Bean
    GraphDatabaseService graphDatabaseService() {
        return new GraphDatabaseFactory().newEmbeddedDatabase(DBNAME);
    }

    @Autowired
    PersonRepository personRepository;

    @Autowired
    GraphDatabaseService graphDatabase;

    /**
     * Adds some demo data to DB
     */
    @PostConstruct
    public void initData() {
        Person greg = new Person("Greg", 50, 50);
        Person roy = new Person("Roy", 250, 250);
        Person craig = new Person("Craig", 50, 250);

        System.out.println("Before linking up with Neo4j...");
        for (Person person : new Person[]{greg, roy, craig}) {
            System.out.println(person);
        }

        try (Transaction tx = graphDatabase.beginTx()) {
            personRepository.save(greg);
            personRepository.save(roy);
            personRepository.save(craig);
            
            greg = personRepository.findByName(greg.getName());
            greg.worksWith(roy);
            greg.worksWith(craig);
            personRepository.save(greg);

            roy = personRepository.findByName(roy.getName());
            roy.worksWith(craig);
            // We already know that roy works with greg
            personRepository.save(roy);

            tx.success();
        }
        
    }
}
