package dungeonmania.entities.spawners;

import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.entities.moving.ZombieToast;
import dungeonmania.util.Position;
import dungeonmania.entities.equippable.Armour;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class ZombieToastSpawner extends Entity implements EnemySpawnerInterface {

    int spawnRate;
    int ticks;
    List<Position> spawnPos = new ArrayList<Position>();

    /**
     * Constructor for the ZombieToastSpawner
     * 
     * @param dungeon The dungeon the spawner is a part of
     * @param position The current position of the spawner in the dungeon
     * @param entityId the unique ID of the ZombieToastSpawner
     * @param type The type of the ZombieToastSpawner i.e. "zombie_toast_spawner"
     */
    public ZombieToastSpawner(Dungeon dungeon, Position position, Integer entityId, String type) {
        super(dungeon, position, entityId, type);
    }

    /**
     * Sets the spawn rate of zombie toast spawner
     * @param spawnRate
     */
    public void setSpawnRate(int spawnRate) {
        this.spawnRate = spawnRate;
        this.ticks = spawnRate;
    }

    /**
     * Checks if the north, east, south and west positions are free and randomly spawns a zombie in any free location
     * Checks the chance of spawning an armoured zombie and gives them armour dependent on that chance
     * @param dungeon
     */
    @Override
    public void spawn(Dungeon dungeon) {
        // Ticks the spawner down
        if (this.ticks != 0) {
            this.ticks--;
            return;
        }
        
        // Gets adjacent positions
        List<Position> adjPos = this.getPosition().getAdjacentPositions();
        for (int i = 1; i < adjPos.size(); i += 2) {
            spawnPos.add(adjPos.get(i));
        }

        // Identifies the available spawn positions from the adjacent positions
        for (int i = 0; i < spawnPos.size(); i++) {
            List<Entity> entities = dungeon.getMap().get(spawnPos.get(i));
            for (Entity e : entities) {
                if (e.getEntityType() == "wall" || e.getEntityType() == "boulder" || e.getEntityType() == "door"
                    || e.getEntityType() == "zombie_toast_spawner" || e.getEntityType() == "portal") {
                    spawnPos.remove(i);
                }
            }
        }

        // Sets a random spawn position from available positions
        Random genZombie = new Random();
        int index = genZombie.nextInt(spawnPos.size());
        Position spawnPosition = spawnPos.get(index);

        // Generates a new zombie with a possibility of having an armour
        Armour zombieArmour = ArmourGeneratorHelper.generateArmour(2,dungeon);
        int zombieId = dungeon.getNewEntityId();
        ZombieToast spawned = new ZombieToast(dungeon, 15, 30, spawnPosition, zombieId, this.getEntityType(), zombieArmour);
        this.ticks = spawnRate;

        dungeon.addEntity(spawned);
    }
}