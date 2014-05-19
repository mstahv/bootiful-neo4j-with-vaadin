package org.vaadin.domain;

import org.springframework.data.neo4j.annotation.GraphId;

/**
 * Common super class for entities in this demo app.
 * 
 * Contains just GraphID and x/y position that are used/modified in the "visual
 * editor".
 * 
 */
public abstract class AbstractPositionableEntity {

    @GraphId
    Long id;

    public AbstractPositionableEntity() {
    }

    public AbstractPositionableEntity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    private int x;
    private int y;

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

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (id != null && (obj instanceof AbstractPositionableEntity)) {
            AbstractPositionableEntity other = (AbstractPositionableEntity) obj;
            return id.equals(other.id);
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
        return super.hashCode();
    }

}
