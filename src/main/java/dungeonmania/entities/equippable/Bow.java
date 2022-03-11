package dungeonmania.entities.equippable;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;

public class Bow extends Equippable {
    /**
     * Constructor for bow
     * @param dungeon
     * @param durability
     * @param position
     * @param entityID
     * @param entityType
     */
    public Bow(Dungeon dungeon, int durability, Position position, int entityID, String entityType){
        super(dungeon, durability, position, entityID, entityType);
    }
}
