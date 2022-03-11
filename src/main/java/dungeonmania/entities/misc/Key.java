package dungeonmania.entities.misc;
import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class Key extends Entity{
    private int keyID;
    
    /**
     * Constructor for key
     * @param dungeon
     * @param keyID
     * @param position
     * @param entityID
     * @param entityType
     */
    public Key(Dungeon dungeon, int keyID, Position position, int entityID, String entityType){
        super(dungeon, position, entityID, entityType);
        this.keyID = keyID;
    }

    /**
     * Get the key id
     * @return keyID
     */
    public int getKeyID() {
        return keyID;
    }
}
