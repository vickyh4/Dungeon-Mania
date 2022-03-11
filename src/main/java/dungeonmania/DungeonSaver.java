package dungeonmania;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.goalsInterface.GoalInterface;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Position;

public class DungeonSaver {
    private String dungeonId;
    private String dungeonName;
    private List<Entity> entities;
    private List<ItemResponse> inventory;
    private List<String> buildables;
    private String gameMode;
    private GoalInterface goalConditions;
    private Position entrance;

    public DungeonSaver(String dungeonId, String dungeonName) {
        this.dungeonId = dungeonId;
        this.dungeonName = dungeonName;
    }

    public void setAll(DungeonResponse response, List<Entity> entities) {
        this.entities = entities;
        this.inventory = response.getInventory();
        this.buildables = response.getBuildables();
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public void setGoals(GoalInterface goals) {
        this.goalConditions = goals;
    }

    public void setEntrance(Position entrance) {
        this.entrance = entrance;
    }

}
