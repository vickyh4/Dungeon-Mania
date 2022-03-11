package dungeonmania.gameModeStrategy;

import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.entities.moving.MovingEntity;
import dungeonmania.entities.moving.Player;
import dungeonmania.entities.spawners.ZombieToastSpawner;

public abstract class GameModeStrategy {
    public Dungeon dungeon;

    public abstract String mode();
    public abstract void battle(Player entityOne, MovingEntity entityTwo);
    public abstract void useItem(Player player, Entity item);
    public abstract void spawnEnemies();
    
    public void setZombieSpawnRates(Dungeon dungeon) {
        if (dungeon.getEntities().containsKey("zombie_toast_spawner")) {
            for (Integer id : dungeon.getEntities().get("zombie_toast_spawner")) {
                Entity e = dungeon.getEntityIds().get(id);
                ZombieToastSpawner z = (ZombieToastSpawner) e; 
                z.setSpawnRate(15);
            }
        }
    }
}
