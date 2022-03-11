package dungeonmania.movingEntitiesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.entities.Entity;
import dungeonmania.entities.moving.Mercenary;
import dungeonmania.entities.moving.MovingEntity;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;

public class AssassinTest {
    @Test
    public void test_assassin_movement() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("assassin-4-1", "Peaceful");

        EntityResponse assassin = null;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getPosition().equals(new Position(4, 1))) {
                assassin = e;
            }
        }

        // Assassin should be moving towards the player with every tick
        controller.tick(null, Direction.LEFT);
        assertEquals(new Position(3, 1), assassin.getPosition());

        controller.tick(null, Direction.LEFT);
        assertEquals(new Position(2, 1), assassin.getPosition());

        controller.tick(null, Direction.LEFT);
        assertEquals(new Position(1, 1), assassin.getPosition());
    }

    @Test
    public void test_assassin_damage() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("assassinMerc", "Peaceful");

        Position mercPos = new Position(3, 1);
        Position assassinPos = new Position(4, 1);

        MovingEntity assassin = null;
        MovingEntity merc = null;

        for (Entity e : controller.getDungeon().getMap().get(mercPos)) {
            if (e.getEntityType().equals("mercenary")) {
                merc = (MovingEntity) e;
            }
        }

        for (Entity e : controller.getDungeon().getMap().get(assassinPos)) {
            if (e.getEntityType().equals("assassin")) {
                assassin = (MovingEntity) e;
            }
        }

        // Assassin should have higher attack damage than the mercenary
        assertTrue(assassin.getAttackDamage() > merc.getAttackDamage());
    }

    @Test
    public void test_assassin_bribe_invalid_distance() {
        // Create controller and dungeon response entity
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("assassin-4-1", "Standard");

        EntityResponse assassin = null;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getPosition().equals(new Position(4, 1))) {
                assassin = e;
            }
        }

        EntityResponse tempAssassin = assassin;

        // dungeon has a assassin(4, 1) more than 2 cardinal tiles away from the
        // player(1, 1)
        // Player is out of range to bribe the assassin so invalidactionexception is
        // thrown
        Assertions.assertThrows(InvalidActionException.class, () -> controller.interact(tempAssassin.getId()));
    }

    @Test
    public void test_assassin_bribe_invalid_bribe() {
        // Create controller and dungeon response entity
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("assassin-4-1", "Standard");

        EntityResponse assassin = null;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getPosition().equals(new Position(4, 1))) {
                assassin = e;
            }
        }

        // Assassin spawns at (4, 1), tick once to let it move to (3, 1)
        controller.tick(null, Direction.NONE);

        EntityResponse tempAssassin = assassin;

        // Assassin in the dungeon within 2 cardinal tiles of the player(1, 1)
        // Player has no ring to bribe the assassin so invalidactionexception is thrown
        Assertions.assertThrows(InvalidActionException.class, () -> controller.interact(tempAssassin.getId()));
    }

    // TODO: make resource dungeon to add a bunch of money to inventory before
    // bribe?
    @Test
    public void test_assassin_bribe_valid() {
        // Create controller and dungeon response entity
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("ringBribe", "Standard");

        // Assassin assassin = null;
        // for (MovingEntity e : controller.getDungeon().getMap().get(mercPos)) {
        // if (e.getPosition().equals(new Position(4, 1))) {
        // assassin = (Assassin) e;
        // }
        // }

        // // Assassin spawns at (4, 1), tick once to let it move to (3, 1) and move
        // player right
        // // to pick up one_ring
        // controller.tick(null, Direction.RIGHT);

        // controller.interact(assassin.getEntityID());

        // // Player is in range to bribe the assassin so and has enough gold so bribe
        // is successful
        // assertFalse(assassin.isHostile());
    }

}
