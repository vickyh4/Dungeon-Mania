package dungeonmania.entities.equippable;

import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class Equippable extends Entity{
    private int durability;
    /**
     * Constructor for equippable entities
     * @param dungeon
     * @param durability
     * @param position
     * @param entityID
     * @param entityType
     */
    public Equippable(Dungeon dungeon, int durability,Position position, int entityID, String entityType){
        super(dungeon, position, entityID, entityType);
        this.durability = durability;
    }

    /**
     * Gets the durability
     * @return durability
     */
    public int getDurability() {
        return durability;
    }

    /**
     * Sets the durability 
     * @param durability
     */
    public void setDurability(int durability) {
        this.durability = durability;
    }
}
