package dungeonmania.goalsInterface;

import java.util.ArrayList;
import java.util.List;
import dungeonmania.response.models.EntityResponse;

public class GoalEnemies implements GoalInterface {

    private String goals = "enemies";
    private transient String name;

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
        boolean completed = true;

        for (EntityResponse e : entities) {
            if (e.getType().equals("spider") || e.getType().equals("mercenary") || e.getType().equals("zombie_toast")) {
                this.name = ":" + e.getType();
                completed = false;
            } 
        }

        if (completed) {
            return "";
        } else {
            return this.name;
        }
    }
    
}
