package dungeonmania.entities.misc;

import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class SwampTile extends Entity {
    private Double movementFactor;
    
    /**
     * Constructor for swamp tile
     * @param dungeon
     * @param position
     * @param entityID
     * @param entityType
     * @param movementFactor
     */
    public SwampTile(Dungeon dungeon, Position position, int entityID, String entityType, Double movementFactor) {
        super(dungeon, position, entityID, entityType);
        this.movementFactor = movementFactor;
    }

    /**
     * Getter for movement factor
     * @return movement factor
     */
    public Double getMovementFactor() {
        return movementFactor;
    }
}
