package dungeonmania.goalsInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import dungeonmania.response.models.EntityResponse;

public class GoalOr implements LogicInterface {

    private String goal = "OR";
    private List<GoalInterface> subgoals;
    
    /**
     * Constructor for Goals (OR)
     * @param goals
     */
    public GoalOr(List<GoalInterface> goals) {
        this.subgoals = goals;
    }

    @Override
    public String goalString() {
        return "OR";
    }

    @Override
    public List<GoalInterface> getGoals() {
        return subgoals;
    }

    @Override
    public List<String> getSubGoals() {
        return this.subgoals.stream().map(GoalInterface::goalString).collect(Collectors.toList());
    }

    @Override
    public String evaluate(List<EntityResponse> entities) {
        boolean complete = false;
        for (GoalInterface g : this.subgoals) {
            if (g.evaluate(entities).equals("")) {
                complete = true;
            } 
        }

        if (complete) {
            return "";
        } else {
            List<String> goalsRemaining = new ArrayList<>();
            for (GoalInterface g : this.subgoals) {
                goalsRemaining.add(g.goalString());
            }
            return String.join("/", goalsRemaining);
        }
        
    }
}
