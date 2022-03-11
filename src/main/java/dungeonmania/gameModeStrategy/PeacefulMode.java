package dungeonmania.gameModeStrategy;

import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.entities.moving.MovingEntity;
import dungeonmania.entities.moving.Player;
import dungeonmania.entities.potions.*;
import dungeonmania.entities.spawners.*;
import dungeonmania.entities.misc.Bomb;

import dungeonmania.exceptions.InvalidActionException;

public class PeacefulMode extends GameModeStrategy {
    
    MercenarySpawner mercSpawner;
    ZombieToastSpawner zombieSpawner;
    SpiderSpawner spiderSpawner;

    public PeacefulMode(Dungeon dungeon) {
        this.dungeon = dungeon;
        this.dungeon = dungeon;
        this.mercSpawner = new MercenarySpawner(30);
        this.spiderSpawner = new SpiderSpawner(14);
        this.setZombieSpawnRates(dungeon);
    }

    @Override
    public String mode() {
        return "Peaceful";
    }

    @Override
    public void battle(Player player, MovingEntity enemy) {
        // We're all friends here.
    }

    @Override
    public void useItem(Player player, Entity item) {
        if (player.checkItemInventory(item)) {
            if (item instanceof HealthPotion) {
                player.setHealth(100);
                this.dungeon.removeEntityID(item);
            } else if (item instanceof InvisibilityPotion) {
                player.setInvisible(true);
                this.dungeon.removeEntityID(item);
            } else if (item instanceof InvincibilityPotion) {
                player.setInvincible(true);
                this.dungeon.removeEntityID(item);
            } else if (item instanceof Bomb) {
                Bomb bomb = (Bomb) item;
                bomb.setPosition(player.getPosition().asLayer(2));
                dungeon.addEntity(bomb);
            } 
            else {
                throw new IllegalArgumentException("Not a usable item");
            }
        } else {
            throw new InvalidActionException("Player does not have this item");
        }
    }

    @Override
    public void spawnEnemies() {
        this.mercSpawner.spawn(this.dungeon);
        this.spiderSpawner.spawn(this.dungeon);
    }
}
