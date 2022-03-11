package dungeonmania.entities.misc;

import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;
import static dungeonmania.DungeonControllerLoader.MOVING;

import java.util.List;

public class Bomb extends Entity {

    /**
     * Constructor for a bomb
     * @param position
     * @param entityID
     * @param entityType
     */
    public Bomb(Dungeon dungeon, Position position, int entityID, String entityType){
        super(dungeon, position, entityID, entityType);
    }

    /**
     * Explodes enemies within one tile of the bomb
     * @param dungeon
     */
    public void explode(Dungeon dungeon) {
        List<Position> adjPos = this.getPosition().getAdjacentPositions();
        for (Position p : adjPos) {
            p.asLayer(MOVING);
        }
        adjPos.add(this.getPosition().asLayer(MOVING));
        
        for (Position p : dungeon.getMap().keySet()) {
            if (adjPos.contains(p)) {
                List<Entity> entitiesAtP = dungeon.getMap().get(p);
                removeEnemies(dungeon, entitiesAtP);
            }
        }
        dungeon.removeEntity(this);
        dungeon.removeEntityID(this);
    }

    /**
     * Removes the enemies in the bombs radius
     * @param dungeon
     * @param entitiesAtP
     */
    private void removeEnemies(Dungeon dungeon, List<Entity> entitiesAtP) {
        for (Entity e : entitiesAtP) {
            if (!e.getEntityType().equals("player")) {
                dungeon.removeEntity(e);
                dungeon.removeEntityID(e);
            }
        }
    }
}
