package dungeonmania.entities.misc;
import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class Exit extends Entity{

    /**
     * Constructor for an exit
     * @param dungeon
     * @param position
     * @param entityID
     * @param entityType
     */
    public Exit(Dungeon dungeon, Position position, int entityID, String entityType){
        super(dungeon, position, entityID, entityType);
    }
}
