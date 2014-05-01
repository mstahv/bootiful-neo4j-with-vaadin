package org.vaadin.domain;

import java.util.HashSet;
import java.util.Set;

import org.neo4j.graphdb.Direction;
import org.springframework.data.neo4j.annotation.Fetch;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;

@NodeEntity
public class Person {

    @GraphId
    Long id;
    private String name;
    private int x;
    private int y;

    public Person() {
    }

    public Person(String name, int x, int y) {
        this.name = name;
        this.x = x;
        this.y = y;
    }

    @RelatedTo(type = "TEAMMATE", direction = Direction.OUTGOING)
    public @Fetch Set<Person> teammates;

    public void worksWith(Person person) {
        if (teammates == null) {
            teammates = new HashSet<>();
        }
        teammates.add(person);
    }

    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Set<Person> getTeammates() {
        return teammates;
    }

    public void setTeammates(Set<Person> teammates) {
        this.teammates = teammates;
    }

    @Override
    public boolean equals(Object obj) {
        if (id != null && (obj instanceof Person)) {
            Person other = (Person) obj;
            return id.equals(other.id);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        if(id != null) {
            return id.hashCode();
        }
        return super.hashCode();
    }

    @Override
    public String toString() {
        String results = name + "'s teammates include\n";
        if (teammates != null) {
            for (Person person : teammates) {
                results += "\t- " + person.name + "\n";
            }
        }
        return results;
    }

}
