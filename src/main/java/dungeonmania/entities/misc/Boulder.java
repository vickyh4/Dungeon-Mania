package dungeonmania.entities.misc;

import java.util.Iterator;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import static dungeonmania.DungeonControllerLoader.STATIC;

public class Boulder extends Entity{

    /**
     * Constructor for a boulder
     * @param dungeon
     * @param position
     * @param entityID
     * @param entityType
     */
    public Boulder(Dungeon dungeon, Position position, int entityID, String entityType){
        super(dungeon, position, entityID, entityType);
    }

    /**
     * Checks if a boulder is pushable by checking behind it
     * @param direction
     * @return
     */
    public boolean isPushable(Direction direction) {
        Position toPosition = getPosition().translateBy(direction);
        List<Entity> staticEntities = dungeon.getMap().get(toPosition.asLayer(STATIC));
        if (staticEntities != null) {
            Iterator<Entity> it = staticEntities.iterator();
            while (it.hasNext()) {
                Entity entity = it.next();
                if (!collisionCheck(entity, direction)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Pushes a boulder in the given direction
     * @param direction
     */
    public void push(Direction direction) {
        Position toPosition = getPosition().translateBy(direction);
        if (isSwitch() != null) {
            isSwitch().setTriggered(false);
        }

        dungeon.updatePosition(this, getPosition(), toPosition);
        this.setPosition(toPosition);

        if (isSwitch() != null) {
            isSwitch().setTriggered(true);
        }
    }

    /**
     * Checks whether the boulder is on a switch
     * @return switch if true, null if false
     */
    public FloorSwitch isSwitch() {
        List<Entity> staticEntities = dungeon.getMap().get(this.getPosition());
        if (staticEntities != null) {
            for (Entity e : staticEntities) {
                if (e.getEntityType().equals("switch")) {
                    FloorSwitch floorSwitch = (FloorSwitch) e;
                    return floorSwitch;
                }
            }
        }
        return null;
    }
    
    /**
     * Checks if the position the boulder is being pushed in is free
     * @param e
     * @param direction
     * @return true if free, false if not
     */
    public boolean collisionCheck(Entity e, Direction direction) {
        String type = e.getEntityType();
        switch (type) {
            case "door":
                Door door = (Door) e;
                if (door.isOpened()) {
                    return true;
                } 
                return false;
            
            case "wall":
                return false;

            case "boulder":
                return false;
            
            case "zombie_toast_spawner":
                return false;
            
            case "portal":
                return false;
            
            case "exit":
                return false;

            default:
                return true;
        }
    }
}
