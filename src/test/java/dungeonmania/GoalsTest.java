package dungeonmania;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Test;

import dungeonmania.response.models.DungeonResponse;

import dungeonmania.util.Direction;


public class GoalsTest {
    /**
     * J Unit tests for Treasure Objective in Dungeon game
     *
     *Testing that after all treasure has been picked up that there are no more goals to be completed. 
     */
    @Test
    public void treasureObjective() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("treasureGoalTest.json", "Standard");
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.LEFT);
        assertEquals("", dungeon.getGoals());
    }
    /**
     * J Unit tests for Enemy Objective in Dungeon game
     *
     * When there are multiple types of mobs and a mob spawner present, the goal is only complete when the player has defeated all enemies and spawners.
     *
     */
    @Test
    public void enemyTest() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("enemyGoalTest.json", "Standard");
        // Collect items and use invincibility potion
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.LEFT);
        controller.tick("4", null);
        // Whilst there are other entities on the map continue trying to move left until all enemys and spawners are defeated.
        while(dungeon.getEntities().size() > 1) {
            controller.tick(null, Direction.LEFT);
        }
        // If the character is the only entity left on the map, therefore all spawners and mobs have been defeated
        assertEquals("", dungeon.getGoals());
    }
    /**
     * J Unit tests for Switch Objective in Dungeon game
     *
     * Testing cases:
     * - When all switches are covered by boulders that the goals for that dungeon are completed
     * - When only a portion of switches are covered, the goal is not completed.
     *
     */
    @Test
    public void switchTest() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("switchGoalTest.json", "peaceful");
        // First switch covered
        controller.tick(null, Direction.LEFT);
        assertEquals("switch", dungeon.getGoals());
        // Second switch covered
        controller.tick(null, Direction.RIGHT);
        controller.tick(null, Direction.RIGHT);
        assertEquals("", dungeon.getGoals());
    }
    /**
     * J Unit tests for Exit Objective in Dungeon game
     *
     *Testing that only once the character passes through an exit then they have completed their goal.
     */
    @Test
    public void exitTest() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("exitGoalTest.json", "peaceful");
        controller.tick(null, Direction.LEFT);
        assertEquals("exit", dungeon.getGoals());
        controller.tick(null, Direction.LEFT);
        assertEquals("", dungeon.getGoals());
    }
    /**
     * J Unit tests for Multiple Objectives in Dungeon game
     *
     * Testing an AND goal situation where two goals must be completed
     */
    @Test
    public void andTest() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("goalAndTest.json", "peaceful");
        controller.tick(null, Direction.RIGHT);
        // Check that after one goal is complete that the goals are not complete
        assertFalse(dungeon.getGoals() == "");
        controller.tick(null, Direction.LEFT);
        controller.tick(null, Direction.LEFT);
        // Assert after both goals are completed that the goals is an empty string
        assertEquals("", dungeon.getGoals());
    }

    /**
     * J Unit tests for Multiple Objectives in Dungeon game
     * 
     * Testing a OR goal situation where one of multiple goals can be completed
     */
    @Test
    public void orTest() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("goalOrTest.json", "peaceful");
        controller.tick(null, Direction.RIGHT);
        // Assert that once one of the goals is complete that the goals string is empty
        assertEquals("", dungeon.getGoals());
    }
}
