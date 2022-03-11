package dungeonmania.entities.misc;

import dungeonmania.entities.Entity;
import dungeonmania.util.Position;
import dungeonmania.Dungeon;

import java.util.ArrayList;
import java.util.List;


public class FloorSwitch extends Entity {
    private Boolean triggered;
    
    /**
     * Constructor for a floor switch
     * @param dungeon
     * @param position
     * @param entityID
     * @param entityType
     */
    public FloorSwitch(Dungeon dungeon, Position position, Integer entityID, String entityType){
        super(dungeon, position, entityID, entityType);
    }

    /**
     * Detonates any adjacent bombs
     * @param dungeon
     */
    public void detonate(Dungeon dungeon) {
        List<Position> adjPos = this.getPosition().getAdjacentPositions();
        List<Bomb> bombs = new ArrayList<>();
        for (Position p : adjPos) {
            List<Entity> entityList = dungeon.getMap().get(p);
            if (entityList != null) {
                bombs = getBombs(entityList);
            }
        }
        for (Bomb bomb : bombs) {
            bomb.explode(dungeon);
        }
    }

    /**
     * Gets the state of the floor switch
     * @return
     */
    public Boolean getTriggered() {
        return triggered;
    }

    /**
     * Sets the state of the floor switch
     * @param triggered
     */
    public void setTriggered(Boolean triggered) {
        if (triggered == true) {
            detonate(dungeon);
            this.triggered = triggered;
        } else {
            this.triggered = triggered;
        }
    }

    /**
     * Gets a list of adjacent bombs placed
     * @return bombs
     */
    public List<Bomb> getBombs(List<Entity> entityList) {
        List<Bomb> bombs = new ArrayList<>();
        for (Entity e : entityList) {
            if (e.getEntityType().equals("bomb") 
                && Position.isAdjacent(this.getPosition(), e.getPosition())) {
                Bomb bomb = (Bomb) e;
                bombs.add(bomb);
            }
        }
        return bombs;
    }
}
