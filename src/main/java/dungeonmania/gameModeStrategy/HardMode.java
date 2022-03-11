package dungeonmania.gameModeStrategy;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.entities.equippable.Sceptre;
import dungeonmania.entities.misc.Bomb;
import dungeonmania.entities.moving.Assassin;
import dungeonmania.entities.moving.Mercenary;
import dungeonmania.entities.moving.MovingEntity;
import dungeonmania.entities.moving.Player;
import dungeonmania.entities.potions.*;
import dungeonmania.entities.spawners.*;
import dungeonmania.exceptions.InvalidActionException;

public class HardMode extends GameModeStrategy {

    MercenarySpawner mercSpawner;
    ZombieToastSpawner zombieSpawner;
    SpiderSpawner spiderSpawner;
    HydraSpawner hydraSpawner;

    public HardMode(Dungeon dungeon) {
        this.dungeon = dungeon;
        this.mercSpawner = new MercenarySpawner(25);
        this.spiderSpawner = new SpiderSpawner(15);
        this.hydraSpawner = new HydraSpawner(50);
        this.setZombieSpawnRates(dungeon);
    }

    @Override
    public String mode() {
        return "Hard";
    }

    @Override
    public void battle(Player player, MovingEntity enemy) {
        enemy.combat(player, enemy);
    }

    @Override
    public void spawnEnemies() {
        this.mercSpawner.spawn(this.dungeon);
        this.spiderSpawner.spawn(this.dungeon);
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
                player.setInvincible(false);
                this.dungeon.removeEntityID(item);
            } else if (item instanceof Bomb) {
                Bomb bomb = (Bomb) item;
                bomb.setPosition(player.getPosition().asLayer(2));
                dungeon.addEntity(bomb);
            } else if (item instanceof Sceptre) {
                Sceptre s = (Sceptre) item;
                if (s.getDuration() > 0) {
                    s.setDuration(s.getDuration() - 1);
                    List<Integer> bribableIDs = new ArrayList<>();
                    if (this.dungeon.getEntities().get("mercenary") != null) {
                        bribableIDs = this.dungeon.getEntities().get("mercenary");
                    } 
                    if (this.dungeon.getEntities().get("assassin") != null) {
                        bribableIDs.addAll(this.dungeon.getEntities().get("assassin"));
                    }
                    for (Integer i : bribableIDs) {
                        Entity e = dungeon.getEntityIds().get(i);
                        if (e instanceof Mercenary) {
                            Mercenary m = (Mercenary) e;
                            m.setHostile(false);
                        } else if (e instanceof Assassin) {
                            Assassin a = (Assassin) e;
                            a.setHostile(false);
                        }
                    }
                    if (s.getDuration() == 0) {
                        player.removeFromInventory("sceptre", item);
                        this.dungeon.removeEntity(item);
                    }
                }
            }
            else {
                throw new IllegalArgumentException("Not a usable item");
            }
        } else {
            throw new InvalidActionException("Player does not have this item");
        }
    }
}
