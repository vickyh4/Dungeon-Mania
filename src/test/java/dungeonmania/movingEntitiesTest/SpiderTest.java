package dungeonmania.movingEntitiesTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;
import dungeonmania.util.Direction;

public class SpiderTest {
    @Test
    public void test_spider_movement() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("spider1", "Peaceful");
        
        EntityResponse spider = null;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getPosition().equals(new Position(4, 4))) {
                spider = e;
            }
        }
        
        // Spider moves up from its spawn point then continues in a circle around it, unimpeded by walls
        // Character tick direction irrelevant, spider should move every tick
        controller.tick(null, Direction.NONE);
        assertEquals(new Position(4, 3), spider.getPosition());

        controller.tick(null, Direction.NONE);
        assertEquals(new Position(5, 3), spider.getPosition());

        controller.tick(null, Direction.NONE);
        assertEquals(new Position(5, 4), spider.getPosition());
        
        controller.tick(null, Direction.NONE);
        assertEquals(new Position(5, 5), spider.getPosition());

        controller.tick(null, Direction.NONE);
        assertEquals(new Position(4, 5), spider.getPosition());
    
        controller.tick(null, Direction.NONE);
        assertEquals(new Position(3, 5), spider.getPosition());

        controller.tick(null, Direction.NONE);
        assertEquals(new Position(3, 4), spider.getPosition());

        controller.tick(null, Direction.NONE);
        assertEquals(new Position(3, 3), spider.getPosition());

        controller.tick(null, Direction.NONE);
        assertEquals(new Position(4, 3), spider.getPosition());
    }

    @Test
    public void test_spider_boulder() {
        DungeonManiaController controller = new DungeonManiaController();
        DungeonResponse dungeon = controller.newGame("spider2", "Peaceful");
        
        EntityResponse spider = null;
        for (EntityResponse e : dungeon.getEntities()) {
            if (e.getPosition().equals(new Position(4, 4))) {
                spider = e;
            }
        }

        // Spider moves up from its spawn point then continues in a circle around it, unimpeded by walls
        // Character tick direction irrelevant, spider should move every tick
        controller.tick(null, Direction.UP);
        assertEquals(new Position(4, 3), spider.getPosition());

        // Spider spends one tick bumping into the boulder before reversing direction
        controller.tick(null, Direction.UP);
        assertEquals(new Position(4, 3), spider.getPosition());

        controller.tick(null, Direction.UP);
        assertEquals(new Position(3, 3), spider.getPosition());
        
        // After 5 ticks, spider should be positioned on the other side of boulder and reverse direction again
        for (int i = 0; i < 5; i++) {
            controller.tick(null, Direction.UP);
        }
        assertEquals(new Position(5, 4), spider.getPosition());

        controller.tick(null, Direction.UP);
        assertEquals(new Position(5, 4), spider.getPosition());

        controller.tick(null, Direction.UP);
        assertEquals(new Position(5, 5), spider.getPosition());

    }


}
