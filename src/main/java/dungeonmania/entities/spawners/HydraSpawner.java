package dungeonmania.entities.spawners;

import dungeonmania.Dungeon;
import dungeonmania.entities.moving.Hydra;
import dungeonmania.util.Position;

public class HydraSpawner implements EnemySpawnerInterface {

    int spawnRate;
    int ticks;

    /**
     * Constructor for the HydraSpawner
     * @param spawnRate
     */
    public HydraSpawner(int spawnRate) {
        this.spawnRate = spawnRate;
        this.ticks = spawnRate;
    }

    /**
     * Spawns a Hydra at the dungeon entrance after 50 ticks if in hard mode
     * @param dungeon
     */
    @Override
    public void spawn(Dungeon dungeon) {
        if (this.ticks != 0) {
            this.ticks--;
            return;
        }
        Position spawnPosition = dungeon.getDungeonEntrance();
        int hydraID = dungeon.getNewEntityId();
        Hydra hydra = new Hydra(dungeon, 20, 40, spawnPosition, hydraID, "hydra");
        dungeon.addEntityId(hydraID, hydra);
        dungeon.addEntity(hydra);
        this.ticks = this.spawnRate;
    }

}
