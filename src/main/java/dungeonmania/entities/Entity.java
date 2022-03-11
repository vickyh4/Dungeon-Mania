package dungeonmania.entities;

import dungeonmania.Dungeon;
import dungeonmania.util.*;

public class Entity {
    protected Position position;
    protected Integer entityID;
    protected String entityType;
    protected transient Dungeon dungeon;
    
    /**
     * Constructor for an Entity
     * @param dungeon
     * @param position
     * @param entityID
     * @param entityType
     */
    public Entity(Dungeon dungeon, Position position, Integer entityID, String entityType){
        this.position = position;
        this.entityID = entityID;
        this.entityType = entityType;
        this.setDungeon(dungeon);
    }

    /**
     * Sets the dungeon this entity exists in
     */
    public void setDungeon(Dungeon dungeon) {
        this.dungeon = dungeon;
    }

    /**
     * Gets position of entity
     * @return Position position
     */
    public Position getPosition() {
        return position;
    }
    /**
     * @param position
     * Sets the position of entity
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Gets the entity's ID
     * @return entityID
     */
    public Integer getEntityID() {
        return entityID;
    }

    /**
     * Gets the entity type
     * @return entityType
     */
    public String getEntityType() {
        return entityType;
    }

}
