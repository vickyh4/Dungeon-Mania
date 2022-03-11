package dungeonmania.entities.spawners;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import dungeonmania.Dungeon;
import dungeonmania.util.Position;
import dungeonmania.entities.Entity;
import dungeonmania.entities.moving.Spider;

import static dungeonmania.DungeonControllerLoader.MOVING;
import static dungeonmania.DungeonControllerLoader.STATIC;

public class SpiderSpawner implements EnemySpawnerInterface {

    int spawnRate;
    int ticks;

    /**
     * Constructor for the spider spawner
     * 
     * @param spawnRate
     */
    public SpiderSpawner(int spawnRate) {
        this.spawnRate = spawnRate;
        this.ticks = spawnRate;
    }

    @Override
    public void spawn(Dungeon dungeon) {
        // Ticks the spawner down
        if (this.ticks != 0) {
            this.ticks--;
            return;
        }
        int tilesVisited = 0;

        // Loops through to find a random available position to spawn a spider
        while (true) {
            if (tilesVisited == dungeon.getDimensionX() * dungeon.getDimensionY()) {
                break;
            }
            int x = randInt(dungeon.getDimensionX());
            int y = randInt(dungeon.getDimensionX());
            Position spawnPosition = new Position(x, y, MOVING);
            if (checkPosition(spawnPosition, dungeon)) {
                Spider spider = new Spider(dungeon, 5, 10, new Position(x, y, MOVING), dungeon.getNewEntityId(),
                        "spider");
                this.ticks = this.spawnRate;
                dungeon.addEntity(spider);
                break;
            }
            tilesVisited++;
        }
    }

    /**
     * Helper function: Random int generator
     * 
     * @param max
     * @return randInt
     */
    private int randInt(int max) {
        Random rand = new Random();
        int x = rand.nextInt(max) + 1;
        return x;
    }

    /**
     * Checks if a position contains a boulder
     * 
     * @param position
     * @param dungeon
     * @return boolean
     */
    public boolean checkPosition(Position p, Dungeon dungeon) {
        List<Entity> entitiesAtNext = dungeon.getMap().get(p.asLayer(STATIC));

        if (entitiesAtNext == null) {
            return true;
        }

        List<String> types = entitiesAtNext.stream().map(Entity::getEntityType).collect(Collectors.toList());
        if (types.contains("boulder")) {
            return false;
        }

        return true;
    }

}