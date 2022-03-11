package dungeonmania.entities.moving;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class Hydra extends MovingEntity implements MovingInterface, ObserverMob {

    private boolean regenerating;
    private Player player;
    List<Position> movePos = new ArrayList<Position>();

    /**
     * Constructor for the Hydra
     * @param dungeon
     * @param attackDamage
     * @param health
     * @param position
     * @param entityID
     * @param entityType
     */
    public Hydra(Dungeon dungeon, double attackDamage, double health, Position position, int entityID,
    String entityType) {
        super(dungeon, attackDamage, health, position, entityID, entityType);
        this.regenerating = false;
    }

    /**
     * Setter for hydra's regeneration
     * @param regen
     */
    public void setRegenerating(boolean regen) {
        this.regenerating = regen;
    }

    /**
     * Getter for hydra's regeneration
     * @return
     */
    public boolean getRegenerating() {
        return this.regenerating;
    }

    /**
     * Part of the observer method that updates the player for the Hydra
     * @param player
     */
    @Override
    public void update(SubjectPlayer o) {
        this.player = player.getSubjectPlayer();
    }

    /**
     * A getter for the player's position
     * @return
     */
    public Position getPlayerPosition() {
        return player.getPosition();
    }

    /**
     * Outlines the behaviour for the Hydra's movement.
     * Randomly chooses one of the available cardinal blocks adjacent to the Hydra
     * and moves them there.
     */
    public void moveNormal() {
        List<Position> adjPos = this.getPosition().getAdjacentPositions();
        for (int i = 1; i < adjPos.size(); i += 2) {
            movePos.add(adjPos.get(i));
        }

        for (int i = 0; i < movePos.size(); i++) {
            List<Entity> entities = dungeon.getMap().get(movePos.get(i));
            for (Entity e : entities) {
                if (e.getEntityType() == "wall" || e.getEntityType() == "boulder" || e.getEntityType() == "door" 
                                    || e.getEntityType() == "portal" || e.getEntityType() == "zombie_toast_spawner") {
                    movePos.remove(i);
                }
            }
        }

        Random moveHydra = new Random();
        int index = moveHydra.nextInt(movePos.size());
        Position hydraDirection = movePos.get(index);
        moveDirection(hydraDirection);
    }

    @Override
    public void moveEntity() {
        if(player.isInvincible()) {
            moveDirection(MovingEntityHelper.moveAwayFromPlayer(dungeon, player.getPosition(), this.getPosition()));
        } else {
            moveNormal();
        }
    }

    @Override
    public void moveDirection(Position position) {
        dungeon.updatePosition(this, this.getPosition(), position);
        this.setPosition(position);
    }
}
