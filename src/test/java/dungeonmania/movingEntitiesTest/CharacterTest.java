package dungeonmania.movingEntitiesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;

public class CharacterTest {
    @Test
    public void test_character_movement() {
        // Create controller and dungeon response entity
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("maze", "Peaceful");

        // Check that the player starts at the starting position
        Position startingPosition = new Position(1, 1);
        EntityResponse player = null;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getType().equals("Player")) {
                player = e;
            }
        }
        assertEquals(startingPosition, player.getPosition());

        // Character should not be able to move up, left or right as there is a wall there
        controller.tick(null, Direction.LEFT);
        assertEquals(startingPosition, player.getPosition());

        controller.tick(null, Direction.UP);
        assertEquals(startingPosition, player.getPosition());

        controller.tick(null, Direction.RIGHT);
        assertEquals(startingPosition, player.getPosition());
        
        // Character should be able to move down
        controller.tick(null, Direction.DOWN);
        assertEquals(new Position(1, 2), player.getPosition());
    }

}
