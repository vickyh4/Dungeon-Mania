package dungeonmania.entities.misc;
import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class Wall extends Entity {

    /**
     * Constructor for a wall
     * @param dungeon
     * @param position
     * @param entityID
     * @param entityType
     */
    public Wall(Dungeon dungeon, Position position, int entityID, String entityType){
        super(dungeon, position, entityID, entityType);
    }
}
