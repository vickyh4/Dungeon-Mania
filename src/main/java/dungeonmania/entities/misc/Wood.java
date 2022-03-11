package dungeonmania.entities.misc;
import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class Wood extends Entity {

    /**
     * Constructor for wood
     * @param dungeon
     * @param position
     * @param entityID
     * @param entityType
     */
    public Wood(Dungeon dungeon, Position position, int entityID, String entityType){
        super(dungeon, position, entityID, entityType);
    }
}
