package dungeonmania.entities.spawners;

import java.util.Random;

import dungeonmania.Dungeon;
import dungeonmania.entities.equippable.Armour;
import dungeonmania.entities.moving.MovingEntityHelper;

public class ArmourGeneratorHelper {
    /**
     * Armour generator helpfer function which generates a new armour entity with a random durability between 5 and 10
     * @param armourSpawnRate
     * @param dungeon
     * @return armour
     */
    public static Armour generateArmour(int armourSpawnRate, Dungeon dungeon) {
        Armour armour = null;
        Random withArmour = new Random();
        if (armourSpawnRate > withArmour.nextInt(10)) {
            int armourId = dungeon.getNewEntityId();
            int durability = MovingEntityHelper.durabilityGenerator();
            armour = new Armour(dungeon, durability, null, armourId, "armour");
            dungeon.addEntityId(armourId, armour);
        }
        return armour;
    }
}
