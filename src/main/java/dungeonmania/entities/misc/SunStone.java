package dungeonmania.entities.misc;
import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class SunStone extends Entity {

    /**
     * Constructor for the one ring
     * @param dungeon
     * @param position
     * @param entityID
     * @param entityType
     */
    public SunStone(Dungeon dungeon, Position position, int entityID, String entityType){
        super(dungeon, position, entityID, entityType);
    }
}
