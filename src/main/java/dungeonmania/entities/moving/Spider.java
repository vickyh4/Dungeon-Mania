package dungeonmania.entities.moving;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import dungeonmania.Dungeon;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.entities.Entity;
import dungeonmania.entities.misc.Boulder;

public class Spider extends MovingEntity implements MovingInterface, ObserverMob {

    private Position spawnPoint;
    private List<Direction> path;
    private Player player;

    /**
     * Constructor for Spider
     * 
     * @param dungeon
     * @param attackDamage
     * @param health
     * @param position
     * @param entityID
     * @param entityType
     */
    public Spider(Dungeon dungeon, double attackDamage, double health, Position position, int entityID,
            String entityType) {
        super(dungeon, attackDamage, health, position, entityID, entityType);
        this.path = (new ArrayList<>(Arrays.asList(Direction.UP, Direction.RIGHT, Direction.RIGHT, Direction.DOWN,
                Direction.DOWN, Direction.LEFT, Direction.LEFT, Direction.UP)));
    }

    @Override
    public void update(SubjectPlayer player) {
        this.player = player.getSubjectPlayer();
    }

    public void setPath(List<Direction> path) {
        this.path = path;
    }

    /**
     * Getter for spawn point
     * 
     * @return spider spawn point
     */
    public Position getSpawnPoint() {
        return this.spawnPoint;
    }

    /**
     * Setter for spider spawn point
     * 
     * @param spawnPoint
     */
    public void setSpawnPoint(Position spawnPoint) {
        this.spawnPoint = spawnPoint;
    }

    /**
     * Gets the path of the spider
     * 
     * @return spider path
     */
    public List<Direction> getPath() {
        return path;
    }

    /**
     * Checks if the player is invincible, and if they are we will move the spider
     * away from the person
     */
    @Override
    public void moveEntity() {
        if (dungeon.getPlayer().isInvincible()) {
            moveDirection(MovingEntityHelper.moveAwayFromPlayer(dungeon,
                    dungeon.getPlayer().getPosition(), this.getPosition()));
        } else {
            moveNormal();
        }
    }

    @Override
    public void moveDirection(Position position) {
        dungeon.updatePosition(this, this.getPosition(), position);
        this.setPosition(position);
    }

    /**
     * Helper function:
     * Moves the spider into the next direction in their set position
     */
    public void moveNormal() {
        // If there is a boulder in the next step then reverse direction
        if (checkBoulderNext()) {
            reverseDirection();
        }
        // Move the spider in the next position
        Position nextStep = getPosition().translateBy(path.get(0));
        nextStep = getPosition().translateBy(path.get(0));
        moveDirection(nextStep);

        // Remove the direction from the beginning of the path list to the end
        Direction moveToEnd = path.get(0);
        path.remove(0);
        path.add(moveToEnd);
    }

    /**
     * Helper function:
     * Checks whether the next position has a boulder in the way
     * @return boolean
     */
    public boolean checkBoulderNext() {
        Map<Position, List<Entity>> map = dungeon.getMap();
        Position nextStep = getPosition().translateBy(path.get(0)).asLayer(0);
        List<Entity> entities = map.get(nextStep);

        if (entities != null) {
            for (Entity entity : entities) {
                if (entity instanceof Boulder) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Reverses the list of directions the spider is moving in 
     */
    private void reverseDirection() {
        List<Direction> reversedList = path;
        // Reverse the path
        Collections.reverse(reversedList);
        List<Direction> newList = new ArrayList<>();

        // Flip the reversed path's directions
        for (Direction direction : reversedList) {
            if (direction == Direction.UP) {
                newList.add(Direction.DOWN);
            } else if (direction == Direction.DOWN) {
                newList.add(Direction.UP);
            } else if (direction == Direction.RIGHT) {
                newList.add(Direction.LEFT);
            } else if (direction == Direction.LEFT) {
                newList.add(Direction.RIGHT);
            }
        }
        // Set the path to new path
        this.path = newList;
    }

}