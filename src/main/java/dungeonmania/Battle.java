package dungeonmania;

import dungeonmania.entities.moving.MovingEntity;
import dungeonmania.entities.moving.Player;

public interface Battle {
    /**
     * Engages a player and enemy in battle
     * @param player
     * @param enemy
     */
    public void combat(Player player, MovingEntity enemy);
    /**
     * If an enemy is killed it has a possibility of dropping armour
     * @param player
     * @param enemy
     */
    public void dropArmour(Player player, MovingEntity enemy);
    /**
     * Generates a chance of getting armour 
     * @return boolean
     */
    public boolean dropRoll();
}
