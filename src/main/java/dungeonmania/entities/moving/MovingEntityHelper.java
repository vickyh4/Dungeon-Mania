package dungeonmania.entities.moving;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.entities.misc.*;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class MovingEntityHelper {
    /**
     * Gets all available positions in the dungeon map
     * 
     * @param map
     * @param dungeon
     * @return Map<Position, List<Entity>> Available Positions
     */
    public static Map<Position, List<Entity>> getAvailablePositions(Map<Position, List<Entity>> map, Dungeon dungeon) {
        Map<Position, List<Entity>> available = new HashMap<>();
        for (int x = 0; x < dungeon.getDimensionX(); x++) {
            for (int y = 0; y < dungeon.getDimensionY(); y++) {
                Position currentPositionLayer0 = new Position(x, y, 0);
                Position currentPosition = new Position(x, y, 1);
                Position currentPositionLayer2 = new Position(x, y, 2);
                List<Entity> entitiesAtCurrent = map.get(currentPosition);
                List<Entity> entitiesAtLayer0 = map.get(currentPositionLayer0);
                List<Entity> entitiesAtLayer2 = map.get(currentPositionLayer2);
                if (isAvailable(entitiesAtCurrent) && isAvailable(entitiesAtLayer0) && isAvailable(entitiesAtLayer2)) {
                    available.put(currentPosition, entitiesAtCurrent);
                }
            }
        }
        return available;
    }

    /**
     * Checks if a list of entities contains a static entity which cannot be walked
     * on
     * 
     * @param entities
     * @return boolean
     */
    public static boolean isAvailable(List<Entity> entities) {
        if (entities == null) {
            return true;
        }

        for (Entity ent : entities) {
            if (ent.getEntityType().equals("boulder")) {
                return false;
            } else if (ent.getEntityType().equals("wall")) {
                return false;
            } else if (ent.getEntityType().equals("door")) {
                Door door = (Door) ent;
                boolean open = door.isOpened();
                return open ? true : false;
            }
        }
        return true;
    }

    /**
     * Gets the cardinal positions of a given position
     * 
     * @param position
     * @return a list of cardinal positions
     */
    public static List<Position> getCardinals(Position position) {
        List<Position> cardinals = new ArrayList<>();
        cardinals.add(position.translateBy(Direction.UP));
        cardinals.add(position.translateBy(Direction.DOWN));
        cardinals.add(position.translateBy(Direction.LEFT));
        cardinals.add(position.translateBy(Direction.RIGHT));

        return cardinals;
    }

    /**
     * Generates an armour durability between 5 and 10
     * 
     * @return durability
     */
    public static int durabilityGenerator() {
        int max = 10;
        int min = 5;
        int durability = (int) Math.floor(Math.random() * (max - min + 1) + min);
        return durability;
    }

    /**
     * Helper function to decide the next location of the mob if they are running
     * away from the player
     * 
     * @return position of enemy to move into
     */
    public static Position moveAwayFromPlayer(Dungeon dungeon, Position player, Position enemy) {
        List<Position> availablePositions = new ArrayList<>(getAvailablePositions(dungeon.getMap(), dungeon).keySet());
        List<Position> cardinals = getCardinals(enemy);
        // Set the cardinal positions to variables
        Position up = cardinals.get(0);
        Position down = cardinals.get(1);
        Position left = cardinals.get(2);
        Position right = cardinals.get(3);

        // Compare the new positions to enemy positions
        if (player.getX() > enemy.getX() && availablePositions.contains(left)) {
            // player is right of enemy
            return left;
        } else if (player.getX() < enemy.getX() && availablePositions.contains(right)) {
            // player is left of enemy
            return right;
        } else if (player.getY() > enemy.getY() && availablePositions.contains(up)) {
            // player is under enemy
            return up;
        } else if (player.getY() < enemy.getY() && availablePositions.contains(down)) {
            // player is above enemy
            return down;
        }
        return enemy;
    }
}
