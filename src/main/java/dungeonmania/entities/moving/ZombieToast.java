package dungeonmania.entities.moving;

import dungeonmania.Dungeon;
import dungeonmania.entities.equippable.Armour;
import dungeonmania.util.Position;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;
import dungeonmania.entities.Entity;

public class ZombieToast extends MovingEntity implements MovingInterface, ObserverMob {
    private Armour armour;
    private transient Player player;
    List<Position> movePos = new ArrayList<Position>();
    
    /**
     * Constructor for the Zombie Toast
     * @param dungeon The dungeon the zombie is in/ is a part of
     * @param attackDamage The zombie's attack damage
     * @param health The zombie's health points
     * @param position The zombie's current position
     * @param entityID The Unique ID of the zombie
     * @param entityType The type of the zombie i.e. "zombie"
     * @param armour The armour the zombie is or is not wearing
     */
    public ZombieToast(Dungeon dungeon, double attackDamage, double health, Position position, int entityID, String entityType, Armour armour) {
        super(dungeon, attackDamage, health, position, entityID, entityType);
        this.armour = armour;
    }

    @Override
    public void update(SubjectPlayer player) {
        this.player = player.getSubjectPlayer();
    }

    /**
     * Getter for the zombie's armour
     * @return
     */
    public Armour getArmour() {
        return armour;
    }

    /**
     * 
     * @return a boolean confirming whether the zombie does or does not have armour
     */
    public boolean hasArmour() {
        if (armour == null) {
            return false;
        } else {
            return true;
        }
    }
    /**
     * Checks if the player is invincible, and if they are we will move the zombie away from the person
     */
    @Override
    public void moveEntity() {
        if(player.isInvincible()) {
            moveDirection(MovingEntityHelper.moveAwayFromPlayer(dungeon, player.getPosition(), this.getPosition()));
        } else {
            moveNormal();
        }
    }

    /**
     * Moves the zombie into the given position
     */
    @Override
    public void moveDirection(Position position) {
        dungeon.updatePosition(this, this.getPosition(), position);
        this.setPosition(position);
    }
    
    /**
     * Helper function:
     * Outlines the behaviour for the zombie's movement.
     * Randomly chooses one of the available cardinal blocks adjacent to the zombie
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

        Random moveZombie = new Random();
        int index = moveZombie.nextInt(movePos.size());
        Position zomDirection = movePos.get(index);
        moveDirection(zomDirection);
    }

}
