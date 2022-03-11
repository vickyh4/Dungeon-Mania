package dungeonmania.entities.moving;

import dungeonmania.util.Position;

public interface MovingInterface {
    /**
     * Calls move entity to follow their movement pattern and detect any changes in environment
     */
    public void moveEntity();

    /**
     * Move the entity to the new position
     * @param position
     */
    public void moveDirection(Position position);

}