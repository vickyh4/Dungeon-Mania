package dungeonmania.entities.equippable;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;

public class Armour extends Equippable {
    /**
     * Constructor for armour
     * @param dungeon
     * @param durability
     * @param position
     * @param entityID
     * @param entityType
     */
    public Armour(Dungeon dungeon, int durability,Position position, int entityID, String entityType){
        super(dungeon, durability, position, entityID, entityType);
    }
}
