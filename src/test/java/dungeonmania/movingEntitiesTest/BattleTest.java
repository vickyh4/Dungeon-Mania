package dungeonmania.movingEntitiesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;

public class BattleTest {
    @Test
    public void test_battle_standard() {
        // Create controller and dungeon response entity
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("merc-4-1", "Standard");

        // Get entity response of player from dungeon response
        EntityResponse player = null; 
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getType().equals("player")) {
                player = e;
            }
        }

        EntityResponse mercenary = null;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getPosition().equals(new Position(4, 1))) {
                mercenary = e;
            }
        }
        // Tick so that mercenary moves into position (3, 1)
        controller.tick(null, Direction.LEFT);

        // Move entities towards each other into same tile
        controller.tick(null, Direction.RIGHT);
        assertEquals(new Position(2, 1), player.getPosition());
        assertEquals(new Position(2, 1), mercenary.getPosition());

        // Tick for enough rounds such that battle should be complete
        for (int i = 0; i < 10; i++) {
            controller.tick(null, Direction.NONE);
        }

        // Check that mercenary no longer exists in the dungeon
        boolean mercenaryExists = false;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getType().equals("mercenary")) {
                mercenaryExists = true;
            }
        }
        assertFalse(mercenaryExists);
    }

    @Test
    public void test_battle_mercenary_movement() {
        // Create controller and dungeon response entity
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("battle_radius", "Standard");

        EntityResponse mercenary_2 = null;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getPosition().equals(new Position(4, 1))) {
                mercenary_2 = e;
            }
        }

        // battle_radius.json spawns a mercenary on the player so combat should commence immediately
        controller.tick(null, Direction.NONE);

        // Player in mercenary_2's (4, 1) battle radius. Check that mercenary_2 moves twice as fast
        assertEquals(new Position(1, 2), mercenary_2.getPosition());

        // Check that mercenary_2 does not move into wall, but enters same cell as player
        controller.tick(null, Direction.NONE);
        assertEquals(new Position(1, 1), mercenary_2.getPosition());
    }

    @Test
    public void test_battle_queue() {
        // Create controller and dungeon response entity
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("battle_radius", "Standard");

        EntityResponse mercenary_1 = null;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getPosition().equals(new Position(1, 1)) && e.getType().equals("mercenary")) {
                mercenary_1 = e;
            }
        }

        EntityResponse mercenary_2 = null;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getPosition().equals(new Position(4, 1))) {
                mercenary_2 = e;
            }
        }

        controller.tick(null, Direction.NONE);

        // Check that mercenary_2 moves twice as fast
        assertEquals(new Position(1, 2), mercenary_2.getPosition());

        // Check that mercenary_2 does not move into wall, but enters same cell as player
        controller.tick(null, Direction.NONE);
        assertEquals(new Position(1, 1), mercenary_2.getPosition());

        for (int i = 0; i < 15; i++) {
            controller.tick(null, Direction.NONE);
        }

        // Check that mercenary_1 and mercenary_2 have both been defeated by the player (no more entities of type "mercenary" exist)
        List<String> types = dungeon.getEntities().stream().map(EntityResponse::getType).collect(Collectors.toList());

        assertFalse(types.contains("mercenary"));
    }

}
