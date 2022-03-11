package dungeonmania.goalsInterface;

import java.util.ArrayList;
import java.util.List;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Position;

public class GoalBoulders implements GoalInterface {

    private String goal = "boulders";
    private transient String name = ":boulder";

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
        List<Position> switchPositions = new ArrayList<>();
        List<Position> boulderPositions = new ArrayList<>();

        for (EntityResponse e : entities) {
            if (e.getType().equals("switch")) {
                switchPositions.add(e.getPosition());
            } else if (e.getType().equals("boulder")) {
                boulderPositions.add(e.getPosition());
            }
        }

        if (boulderPositions.containsAll(switchPositions)) {
            return "";
        } else {
            return this.name;
        }
    }
    
}
