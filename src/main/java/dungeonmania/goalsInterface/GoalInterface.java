package dungeonmania.goalsInterface;

import java.util.List;

import dungeonmania.response.models.EntityResponse;

public interface GoalInterface {
    /**
     * Returns the goal string 
     * @return String
     */
    public String goalString();

    /**
     * Gets the sub goals within a complex goal
     * @return List<String>
     */
    public List<String> getSubGoals();

    /**
     * Returns a string of remaining goals to be completed 
     * @param entities
     * @return String
     */
    public String evaluate(List<EntityResponse> entities);
}
