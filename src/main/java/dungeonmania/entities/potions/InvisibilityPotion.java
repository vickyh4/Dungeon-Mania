package dungeonmania.entities.potions;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;

public class InvisibilityPotion extends Potion{
    /**
     * Constructor for the invisibility potion
     * @param dungeon
     * @param position
     * @param entityID
     * @param entityType
     */
    public InvisibilityPotion(Dungeon dungeon, Position position, int entityID, String entityType) {
        super(dungeon, position, entityID, entityType);
    }
}
