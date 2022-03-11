package dungeonmania.goalsInterface;

import java.util.List;
import java.util.ArrayList;
import dungeonmania.response.models.EntityResponse;

public class GoalTreasure implements GoalInterface {

    private String goal = "treasure";
    private transient String name = ":treasure";

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
        if (entities.stream().map(EntityResponse::getType).anyMatch(e -> e.equals("treasure"))) {
            return this.name;
        } else {
            return "";
        }
    }
    
}
