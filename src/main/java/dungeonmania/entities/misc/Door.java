package dungeonmania.entities.misc;

import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class Door extends Entity{
    private int key;
    private boolean opened = false;

    /**
     * Constructor for a door
     * @param dungeon
     * @param position
     * @param entityID
     * @param entityType
     * @param key
     */
    public Door(Dungeon dungeon, Position position, Integer entityID, String entityType, int key){
        super(dungeon, position, entityID, entityType);
        this.key = key;
    }

    /** 
     * Checks if the player carries the key for the door
     */
    public boolean canBeOpened() {

        if (this.dungeon.getPlayer().getInventory().get("key") != null) {
            for (Entity e : this.dungeon.getPlayer().getInventory().get("key")) {
                Key k = (Key) e;
                if (k.getKeyID() == this.key) {
                    return true;
                }
            }
        } else if (this.dungeon.getPlayer().getInventory().get("sun_stone") != null) {
            return true;
        }

        return false;
    }

    /**
     * Opens the door using the key
     */
    public void open() {
        if (this.canBeOpened()) {
            this.opened = true;
        }
    }

    /**
     * Gets the state of the door
     * @return
     */
    public boolean isOpened() {
        return opened;
    }
}
