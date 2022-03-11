package dungeonmania.entities.potions;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;

public class InvincibilityPotion extends Potion{
    /**
     * Constructor for the Invincibility potion
     * @param dungeon
     * @param position
     * @param entityID
     * @param entityType
     */
    public InvincibilityPotion(Dungeon dungeon, Position position, int entityID, String entityType) {
        super(dungeon, position, entityID, entityType);
    }
}
