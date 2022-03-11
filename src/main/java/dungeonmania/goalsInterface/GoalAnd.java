package dungeonmania.goalsInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.response.models.EntityResponse;

public class GoalAnd implements LogicInterface {

    private String goal = "AND";
    private List<GoalInterface> subgoals;
    /**
     * Constructor for goals (AND)
     * @param goals
     */
    public GoalAnd(List<GoalInterface> goals) {
        this.subgoals = goals;
    }

    @Override
    public String goalString() {
        return "AND";
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
        boolean complete = true;
        List<GoalInterface> remainingGoals = new ArrayList<>();
        for (GoalInterface g : this.subgoals) {
            if (!g.evaluate(entities).equals("")) {
                complete = false;
                remainingGoals.add(g);
            } 
        }

        if (complete) {
            return "";
        } else {
            List<String> goalsRemaining = new ArrayList<>();
            for (GoalInterface g : remainingGoals) {
                goalsRemaining.add(g.goalString());
            }
            return String.join(" AND ", goalsRemaining);
        }
        
    }
    
}
