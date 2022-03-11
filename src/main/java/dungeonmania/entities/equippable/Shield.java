package dungeonmania.entities.equippable;
import dungeonmania.Dungeon;
import dungeonmania.util.Position;

public class Shield extends Equippable{
    
    /**
     * Constructor for shield
     * @param dungeon
     * @param durability
     * @param position
     * @param entityID
     * @param entityType
     */
    public Shield(Dungeon dungeon, int durability, Position position, int entityID, String entityType){
        super(dungeon, durability, position, entityID, entityType);
    }
}
