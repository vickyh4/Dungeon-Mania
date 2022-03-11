package dungeonmania.movingEntitiesTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;

public class ZombieToastTest {
    @Test
    public void test_zombie_movement_all() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("zombie_toast", "Peaceful");
        
        EntityResponse zombie = null;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getPosition().equals(new Position(3, 3))) {
                zombie = e;
            }
        }
        
        controller.tick(null, Direction.LEFT);

        boolean validMove = false;
        Position north = new Position(3, 4);
        Position south = new Position(3, 2);
        Position east = new Position(4, 3);
        Position west = new Position(2, 3);

        // Check whether the zombie has moved one of the four cardinal directions (random movement)
        if (zombie.getPosition().equals(north) ||
        zombie.getPosition().equals(south) ||
        zombie.getPosition().equals(east) ||
        zombie.getPosition().equals(west)) {
            validMove = true;
        }
        assertTrue(validMove);
    }

    @Test
    public void test_zombie_movement_constricted() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("zombie_toast2", "Peaceful");
        
        EntityResponse zombie = null;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getPosition().equals(new Position(3, 3))) {
                zombie = e;
            }
        }
        
        // Check whether the zombie has moved left or right as up and down are constricted
        boolean validMove = false;
        Position east = new Position(4, 3);
        Position west = new Position(2, 3);
        if (zombie.getPosition().equals(east) ||
        zombie.getPosition().equals(west)) {
            validMove = true;
        }
        assertTrue(validMove);
    }
}
