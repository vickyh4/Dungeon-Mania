package dungeonmania.entities.potions;

import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class Potion extends Entity{
    /**
     * Constructor for Potions class
     * @param dungeon
     * @param position
     * @param entityID
     * @param entityType
     */
    public Potion(Dungeon dungeon, Position position, int entityID, String entityType) {
        super(dungeon, position, entityID, entityType);
    }
}
