package dungeonmania.goalsInterface;

import java.util.List;
import java.util.ArrayList;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

import static dungeonmania.DungeonControllerLoader.MOVING;

public class GoalExit implements GoalInterface {

    private String goal = "exit";
    private transient String name = ":exit";

    @Override
    public String goalString() {
        return name;
    }

    @Override
    public List<String> getSubGoals() {
        return new ArrayList<>();
    }

    @Override
    public String evaluate(List<EntityResponse> entities) {
        Position exitPosition = null;
        Position playerPosition = null;

        for (EntityResponse e : entities) {
            if (e.getType().equals("exit")) {
                exitPosition = e.getPosition();
            } else if (e.getType().equals("player")) {
                playerPosition = e.getPosition();
            }
        }

        if (playerPosition.equals(exitPosition.asLayer(MOVING))) {
            return "";
        } else {
            return this.name;
        }
    }
    
}