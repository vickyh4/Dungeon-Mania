package dungeonmania;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Iterator;

import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.entities.Entity;
import dungeonmania.entities.equippable.Equippable;
import dungeonmania.entities.moving.MovingEntity;
import dungeonmania.entities.moving.Player;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.gameModeStrategy.GameModeStrategy;
import dungeonmania.gameModeStrategy.HardMode;
import dungeonmania.gameModeStrategy.PeacefulMode;
import dungeonmania.gameModeStrategy.StandardMode;
import dungeonmania.goalsInterface.GoalInterface;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import static dungeonmania.DungeonControllerLoader.MOVING;

public class Dungeon {
    private String dungeonId;
    private String dungeonName;
    private Map<Position, List<Entity>> map;
    private Map<Integer, Entity> entityIds;
    private Map<String, List<Integer>> entities;
    private Player player;
    private Position dungeonEntrance;
    private GameModeStrategy gameStrategy;
    private GoalInterface goals;
    private DungeonSaver dungeonSave;
    private Integer entityCounter;
    
    /**
     * Dungeon Constructor
     * @param dungeonId
     * @param dungeonName
     */
    public Dungeon(String dungeonId, String dungeonName) {
        this.dungeonId = dungeonId;
        this.dungeonName = dungeonName;
        this.map = new HashMap<>();
        this.entityIds = new HashMap<>();
        this.entities = new HashMap<>();
        this.dungeonSave = new DungeonSaver(dungeonId, dungeonName);
    }

    /**
     * Set entity counter 
     * @param entityCounter
     */
    public void setEntityCounter(int entityCounter) {
        this.entityCounter = entityCounter;
    }

    /**
     * Add an entity to the dungeon
     * 
     * @param entity
     */
    public void addEntity(Entity entity) {
        String type = entity.getEntityType();
        Position position = entity.getPosition();

        if (entities.containsKey(type)) {
            List<Integer> ids = entities.get(type);
            ids.add(entity.getEntityID());
            entities.put(type, ids);
        } else {
            List<Integer> ids = new ArrayList<>();
            ids.add(entity.getEntityID());
            entities.put(type, ids);
        }
        if (!(entity instanceof Equippable)) {
            if (map.containsKey(position)) {
                List<Entity> listOfEntities = map.get(position);
                if (listOfEntities == null) {
                    listOfEntities = new ArrayList<>();
                }
                listOfEntities.add(entity);
                map.put(position, listOfEntities);
            } else {
                List<Entity> listOfEntities = new ArrayList<>();
                listOfEntities.add(entity);
                map.put(position, listOfEntities);
            }
        }

        addEntityId(entity.getEntityID(), entity);
    }

    /**
     * Removes an entity from the dungeon map
     * @param entity
     */
    public void removeEntity(Entity e) {
        List<Entity> entList = new ArrayList<>();
        Position current = e.getPosition();
        if (current != null) {
            entList = map.get(current);
            entList.remove(e);
            map.put(current, entList);
            // if (entList.isEmpty()) {
            //     map.put(current, null);
            // } else {
            //     map.put(current, entList);
            // }
        }

    }

    /**
     * Removes an entity's ID, effectively deleting it completely
     * 
     * @param e The entity being removed
     */
    public void removeEntityID(Entity e) {
        entityIds.remove(e.getEntityID());
    }

    /**
     * Helper function to add entity id to entity id map
     * 
     * @param id     the unique identifier of the entity
     * @param entity the entity being added
     */
    public void addEntityId(int id, Entity entity) {
        entityIds.put(id, entity);
    }

    public Integer getNewEntityId() {
        return entityCounter += 1;
    }

    /**
     * 
     * @param entity the entity being updated
     * @param pos    the entity's previous position
     */
    public void updatePosition(Entity entity, Position oldPos, Position newPos) {
        // Get the list at that position, remove the entity and replace it
        List<Entity> entlist1 = map.get(oldPos);
        entlist1.remove(entity);
        if (entlist1.isEmpty()) {
            map.put(oldPos, null);
        } else {
            map.put(oldPos, entlist1);
        }

        // Get the list at new position, add entity and replace it
        List<Entity> entlist2 = map.get(newPos);
        if (entlist2 == null || entlist2.isEmpty()) {
            entlist2 = new ArrayList<Entity>();
            entlist2.add(entity);
        } else {
            entlist2.add(entity);
        }

        map.put(newPos, entlist2);
    }

    /**
     * Checks if combat is occuring
     * @return boolean
     */
    public void checkForCombat() {
        List<Entity> enemies = map.get(this.player.getPosition());
        Iterator<Entity> it = enemies.iterator();
        MovingEntity enemy = null;
        while (it.hasNext()) {
            Entity e = it.next();
            if (!e.equals(this.player)) {
                enemy = (MovingEntity) e;
            }
        }

        if (enemy != null) {
            this.player.setInCombat(true);
            while (this.player.isInCombat()) {
                this.player.combat(this.player, enemy);
            }
        }
    }

    public boolean playerIsAlive() {
        return this.player.getHealth() > 0 ? true : false;
    }

    /**
     * Getter for entities map
     * 
     * @return the entities map
     */
    public Map<String, List<Integer>> getEntities() {
        return entities;
    }

    /**
     * Getter for entityIds map
     */
    public Map<Integer, Entity> getEntityIds() {
        return entityIds;
    }

    /**
     * Getter for map
     * 
     * @return
     */
    public Map<Position, List<Entity>> getMap() {
        return map;
    }
    /**
     * Gets the game mode
     * @return gameStrategy
     */
    public GameModeStrategy getGameMode() {
        return gameStrategy;
    }
    
    /**
     * Sets the dungeon game mode
     * @param gameMode
     */
    public void setGameMode(String gameMode) {
        this.dungeonSave.setGameMode(gameMode);

        switch (gameMode) {
        case "peaceful":
            this.gameStrategy = new PeacefulMode(this);
            break;
        case "standard":
            this.gameStrategy = new StandardMode(this);
            break;
        case "hard":
            this.gameStrategy = new HardMode(this);
            break;
        }
    }

    /**
     * Sets the dungeon goals
     */
    public void setGoals(GoalInterface goals) {
        this.goals = goals;
        this.dungeonSave.setGoals(goals);
    }

    /**
     * Gets the dungeon player
     * @return player
     */
    public Player getPlayer() {
        return this.player;
    }

    /**
     * Gets the dungeon id
     * @return dungeonId
     */
    public String getDungeonId() {
        return this.dungeonId;
    }

    /**
     * Gets the dungeon name
     * @return dungeonName
     */
    public String getDungeonName() {
        return dungeonName;
    }

    /**
     * Gets the dungeon entrance
     * @return Position dungeonEntrance 
     */
    public Position getDungeonEntrance() {
        return this.dungeonEntrance;
    }

    /**
     * Sets the dungon entrance 
     * @param Position
     */
    public void setDungeonEntrance(Position dungeonEntrance) {
        this.dungeonEntrance = dungeonEntrance;
        this.dungeonSave.setEntrance(dungeonEntrance);
    }

    /**
     * Gets the dungeon saver
     * @return dungeon saver
     */
    public DungeonSaver getDungeonSave() {
        return dungeonSave;
    }

    /**
     * Sets the dungeon saver
     * @param dungeonSave
     */
    public void setDungeonSave(DungeonSaver dungeonSave) {
        this.dungeonSave = dungeonSave;
    }

    /**
     * Sets the player
     * @param player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    public List<EntityResponse> getEntityResponses() {
        List<Entity> entityList = getAllEntities();

        List<EntityResponse> listResponse = new ArrayList<>();
        for (Entity e : entityList) {
            if (isInteractable(e)) {
                listResponse
                        .add(new EntityResponse(e.getEntityID().toString(), e.getEntityType(), e.getPosition(), true));
            } else {
                listResponse
                        .add(new EntityResponse(e.getEntityID().toString(), e.getEntityType(), e.getPosition(), false));
            }
        }
        return listResponse;
    }

    /**
     * Checks if an entity is interactable
     * @param entity
     * @return boolean
     */
    public boolean isInteractable(Entity e) {
        return e.getEntityType().equals("mercenary") || e.getEntityType().equals("zombie_toast_spawner");
    }

    /**
     * Gets the goal interface
     * @return goals
     */
    public GoalInterface getGoalInterface() {
        return goals;
    }

    /**
     * Gets the x dimensions of the dungeon
     * @return int
     */
    public int getDimensionX() {
        int x = 0;
        for (Position p : this.map.keySet()) {
            if (p != null && p.getX() > x) {
                x = p.getX();
            }
        }
        return x;
    }

    /**
     * Gets the y dimensions of the dungeon
     * @return int
     */
    public int getDimensionY() {
        int y = 0;
        for (Position p : this.map.keySet()) {
            if (p != null && p.getY() > y) {
                y = p.getY();
            }
        }
        return y;
    }

    /**
     * Gets one dungeon response
     * @return DungeonResponse
     */
    public DungeonResponse getDungeonResponse() {
        String goals = this.goals.evaluate(getEntityResponses());
        return new DungeonResponse(this.dungeonId, this.dungeonName + ".json", getEntityResponses(),
            this.player.getInventoryResponses(), this.player.getBuildables(), goals);
    }

    /**
     * Ticks a dungeon item
     * @param itemUsed
     */
    public void tickDungeonItem(String itemUsed) {
        Entity item = this.entityIds.get(Integer.parseInt(itemUsed));
        try {
            this.player.useItem(item);
        } catch (IllegalArgumentException | InvalidActionException e) {
            System.err.println(e);
        }
    }

    /**
     * Ticks movement in the dungeon
     * @param movementDirection
     */
    public void tickDungeonMovement(Direction movementDirection) {
        // Move player
        this.player.moveDirection(movementDirection);

        // Move enemies
        for (Entity e : this.entityIds.values()) {
            if (e.getPosition() != null) {
                if (e.getPosition().getLayer() == MOVING && !e.getEntityType().equals("player")) {
                    MovingEntity entity = (MovingEntity) e;
                    entity.moveEntity(entity);
                }
            }
        }
    }

    /**
     * Gets all entities at a position and layer
     * @param position
     * @param layer
     * @return List<Entity> 
     */
    public List<Entity> getEntitiesAtLayer(Position position, int layer) {
        return this.map.get(position.asLayer(layer));
    }

    /**
     * Gets all the entities in the dungeon
     * @return List<Entity>
     */
    public List<Entity> getAllEntities() {
        List<Entity> all = new ArrayList<>();
        for (List<Entity> entities : map.values()) {
            if (entities != null) {
                all.addAll(entities);
            }
        }
        return all;
    }
}
