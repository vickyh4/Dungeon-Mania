package dungeonmania.entities.misc;
import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class Treasure extends Entity{
    
    /**
     * Constructor for treasure
     * @param dungeon
     * @param position
     * @param entityID
     * @param entityType
     */
    public Treasure(Dungeon dungeon, Position position, int entityID, String entityType){
        super(dungeon, position, entityID, entityType);
    }
}
