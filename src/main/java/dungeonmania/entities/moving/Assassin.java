package dungeonmania.entities.moving;

import dungeonmania.Dungeon;
import dungeonmania.entities.equippable.Armour;
import dungeonmania.util.Position;

public class Assassin extends Mercenary {
    /**
     * Assassin constructor
     * 
     * @param dungeon
     * @param attackDamage
     * @param health
     * @param position
     * @param entityID
     * @param entityType
     * @param armour
     */
    public Assassin(Dungeon dungeon, double attackDamage, double health, Position position, int entityID,
            String entityType, Armour armour) {
        super(dungeon, attackDamage, health, position, entityID, entityType, armour);
    }
}
