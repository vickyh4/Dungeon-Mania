package dungeonmania.entities.misc;
import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class Arrows extends Entity{
    
    /**
     * Constructor for an arrow
     * @param dungeon
     * @param position
     * @param entityID
     * @param entityType
     */
    public Arrows(Dungeon dungeon, Position position, int entityID, String entityType){
        super(dungeon, position, entityID, entityType);
    }
}
