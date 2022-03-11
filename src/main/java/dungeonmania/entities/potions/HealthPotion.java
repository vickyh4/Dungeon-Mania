package dungeonmania.entities.potions;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;

public class HealthPotion extends Potion{
    /**
     * Constructor for the health potion
     * @param dungeon
     * @param position
     * @param entityID
     * @param entityType
     */
    public HealthPotion(Dungeon dungeon, Position position, int entityID, String entityType) {
        super(dungeon, position, entityID, entityType);
    }

}
