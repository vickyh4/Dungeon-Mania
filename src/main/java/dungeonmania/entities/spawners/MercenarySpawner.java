package dungeonmania.entities.spawners;

import java.util.Random;

import dungeonmania.Dungeon;
import dungeonmania.entities.equippable.Armour;
import dungeonmania.entities.moving.Assassin;
import dungeonmania.entities.moving.Mercenary;
import dungeonmania.util.Position;

public class MercenarySpawner implements EnemySpawnerInterface {

    int spawnRate;
    int assassinChance = 2;
    int ticks;

    /**
     * Constructor for mercenary/assasin spawner
     * 
     * @param spawnRate
     */
    public MercenarySpawner(int spawnRate) {
        this.spawnRate = spawnRate;
        this.ticks = spawnRate;
    }

    /**
     * Spawns a mercenary or assassin with or without armour depending on chance at
     * entrance after a certain number of ticks
     * 
     * @param dungeon
     */
    @Override
    public void spawn(Dungeon dungeon) {
        if (this.ticks != 0) {
            this.ticks--;
            return;
        }
        // Position spawnPosition = dungeon.getDungeonEntrance();
        Position spawnPosition = new Position(1, 1, 1);
        int mercId = dungeon.getNewEntityId();
        Armour mercArmour = ArmourGeneratorHelper.generateArmour(4, dungeon);
        Random AssassinCheck = new Random();

        // Chooses between spawning an assasin or a mercenary
        if (AssassinCheck.nextInt(10) < assassinChance) {
            Assassin assassin = new Assassin(dungeon, 40, 40, spawnPosition, mercId, "assassin", mercArmour);
            assassin.setPlayer(dungeon.getPlayer());
            dungeon.addEntityId(mercId, assassin);
            dungeon.addEntity(assassin);
        } else {
            Mercenary merc = new Mercenary(dungeon, 20, 40, spawnPosition, mercId, "mercenary", mercArmour);
            merc.setPlayer(dungeon.getPlayer());
            dungeon.addEntityId(mercId, merc);
            dungeon.addEntity(merc);
        }
        this.ticks = this.spawnRate;
    }
}