package dungeonmania.entities.equippable;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;

public class Sceptre extends Equippable {
    private int duration;
    /**
     * Constructor for sceptre
     * @param dungeon
     * @param durability
     * @param position
     * @param entityID
     * @param entityType
     */
    public Sceptre(Dungeon dungeon, int durability,Position position, int entityID, String entityType){
        super(dungeon, durability, position, entityID, entityType);
        this.duration = 10;
    }
    /**
     * Gets the current duration
     * @return
     */
    public int getDuration() {
        return duration;
    }
    /**
     * Set Duration of the sceptre
     * @param duration
     */
    public void setDuration(int duration) {
        this.duration = duration;
    }
}
