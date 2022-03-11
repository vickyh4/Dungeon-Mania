package dungeonmania.movingEntitiesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;

public class MercenaryTest {
    @Test
    public void test_mercenary_movement() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("merc-4-1", "Peaceful");
        
        EntityResponse mercenary = null;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getPosition().equals(new Position(4, 1))) {
                mercenary = e;
            }
        }
        
        // Mercenary should be moving towards the player with every tick
        controller.tick(null, Direction.LEFT);
        assertEquals(new Position(3, 1), mercenary.getPosition());

        controller.tick(null, Direction.LEFT);
        assertEquals(new Position(2, 1), mercenary.getPosition());

        controller.tick(null, Direction.LEFT);
        assertEquals(new Position(1, 1), mercenary.getPosition());
    }

    @Test
    public void test_mercenary_bribe_invalid_distance() {
        // Create controller and dungeon response entity
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("merc-4-1", "Standard");

        EntityResponse mercenary = null;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getPosition().equals(new Position(4, 1))) {
                mercenary = e;
            }
        }

        EntityResponse tempMerc = mercenary;

        // dungeon has a mercenary(4, 1) more than 2 cardinal tiles away from the player(1, 1)
        // Player is out of range to bribe the mercenary so invalidactionexception is thrown
        Assertions.assertThrows(InvalidActionException.class, () -> controller.interact(tempMerc.getId()));
    }

    @Test
    public void test_mercenary_bribe_invalid_gold() {
        // Create controller and dungeon response entity
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("merc-4-1", "Standard");

        EntityResponse mercenary = null;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getPosition().equals(new Position(4, 1))) {
                mercenary = e;
            }
        }

        // Mercenary spawns at (4, 1), tick once to let it move to (3, 1)
        controller.tick(null, Direction.NONE);

        EntityResponse tempMerc = mercenary;

        // Mercenary in the dungeon within 2 cardinal tiles of the player(1, 1)
        // Player has no gold to bribe the mercenary so invalidactionexception is thrown
        Assertions.assertThrows(InvalidActionException.class, () -> controller.interact(tempMerc.getId()));
    }

    // TODO: make resource dungeon to add a bunch of money to inventory before bribe?
    @Test
    public void test_mercenary_bribe_valid() {
        // Create controller and dungeon response entity
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("merc-4-1", "Standard");

        EntityResponse mercenary = null;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getPosition().equals(new Position(4, 1))) {
                mercenary = e;
            }
        }

        // Mercenary spawns at (4, 1), tick once to let it move to (3, 1)
        controller.tick(null, Direction.NONE);

        // Player is in range to bribe the mercenary so and has enough gold so bribe is successful
    //     assertFalse(mercenary.getHostility());
    }
    

}
