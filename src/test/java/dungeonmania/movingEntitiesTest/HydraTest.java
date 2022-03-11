package dungeonmania.movingEntitiesTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.entities.Entity;
import dungeonmania.entities.moving.MovingEntity;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;

public class HydraTest {
    @Test
    public void test_hydra_movement_all() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("hydra", "Hard");
        
        EntityResponse hydra = null;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getPosition().equals(new Position(3, 3))) {
                hydra = e;
            }
        }
        
        controller.tick(null, Direction.LEFT);

        boolean validMove = false;
        Position north = new Position(3, 4);
        Position south = new Position(3, 2);
        Position east = new Position(4, 3);
        Position west = new Position(2, 3);

        // Check whether the hydra has moved one of the four cardinal directions (random movement)
        if (hydra.getPosition().equals(north) ||
        hydra.getPosition().equals(south) ||
        hydra.getPosition().equals(east) ||
        hydra.getPosition().equals(west)) {
            validMove = true;
        }
        assertTrue(validMove);
    }

    @Test
    public void test_hydra_movement_constricted() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("hydra2", "Hard");
        
        EntityResponse hydra = null;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getPosition().equals(new Position(3, 3))) {
                hydra = e;
            }
        }
        
        // Check whether the hydra has moved left or right as up and down are constricted
        boolean validMove = false;
        Position east = new Position(4, 3);
        Position west = new Position(2, 3);
        if (hydra.getPosition().equals(east) ||
        hydra.getPosition().equals(west)) {
            validMove = true;
        }
        assertTrue(validMove);
    }

    @Test
    public void test_hydra_regen() {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("hydra_combat", "Hard");

        Position hydraPos = new Position(2, 0);
        MovingEntity hydra = null;

        for (Entity e : controller.getDungeon().getMap().get(hydraPos)) {
            if (e.getEntityType().equals("hydra")) {
                hydra = (MovingEntity) e;
            }
        }

        // UP is the only valid direction for the hydra to move and moving player right will
        // have them on the same tile initiating combat.
        controller.tick(null, Direction.RIGHT);

        // For every tick in combat, check if the hydra's current health is greater than it was
        // last round. If it is, then the regen was a success, otherwise continue ticking.
        double hydraHP = hydra.getHealth();
        boolean regenSuccess = false;
        for (int i = 0; i < 20; i++) {
            if (hydraHP < hydra.getHealth()) {
                regenSuccess = true;
                break;
            }
            else {
                hydraHP = hydra.getHealth();
            }
        }

        assertTrue(regenSuccess);
    }
}
