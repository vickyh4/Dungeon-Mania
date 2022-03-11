package dungeonmania;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

import com.google.gson.*;

import dungeonmania.util.*;
import dungeonmania.entities.Entity;
import dungeonmania.entities.equippable.*;
import dungeonmania.entities.misc.*;
import dungeonmania.entities.moving.*;
import dungeonmania.entities.potions.*;
import dungeonmania.entities.spawners.ZombieToastSpawner;
import dungeonmania.goalsInterface.*;
import dungeonmania.response.models.*;

public class DungeonControllerLoader {
    public static final int STATIC = 0;
    public static final int MOVING = 1;
    public static final int COLLECTABLES = 2;
    private Dungeon dungeon;
    private String dungeonName;
    private JsonObject json;
    private int id = 0;

    /**
     * Dungeon Controller Loader constructor
     * 
     * @param dungeonName
     */
    public DungeonControllerLoader(String dungeonName) {
        this.dungeonName = dungeonName;
    }

    /**
     * Creates a new game by loading a map in
     * 
     * @param gameMode
     * @return DungeonResponse
     * @throws JsonIOException
     * @throws JsonSyntaxException
     * @throws FileNotFoundException
     */
    public DungeonResponse loadNewGame(String gameMode)
            throws JsonIOException, JsonSyntaxException, FileNotFoundException {
        String filename = "src/main/resources/dungeons/" + dungeonName + ".json";
        this.json = JsonParser.parseReader(new FileReader(filename)).getAsJsonObject();

        String dungeonId = "dungeon-" + Integer.toString(Globals.dungeonIdentifier++);
        Dungeon loadDungeon = new Dungeon(dungeonId, dungeonName);
        this.dungeon = loadDungeon;

        JsonArray entitiesArray = json.get("entities").getAsJsonArray();
        JsonObject goalObject = json.get("goal-condition").getAsJsonObject();

        List<EntityResponse> entities = parseEntities(dungeon, entitiesArray);
        GoalInterface goals = parseGoals(goalObject, entities);
        this.dungeon.setGoals(goals);
        this.dungeon.setGameMode(gameMode);

        updatePlayerObservers(this.dungeon);
        fillMap(this.dungeon);

        DungeonResponse response = this.dungeon.getDungeonResponse();
        this.dungeon.setEntityCounter(this.id);
        return response;
    }

    /**
     * Load a dungeon and dungeon saver from dungeon
     * 
     * @param name
     * @return
     * @throws JsonIOException
     * @throws JsonSyntaxException
     * @throws FileNotFoundException
     */
    public DungeonResponse loadSaveFile(String name)
            throws JsonIOException, JsonSyntaxException, FileNotFoundException {
        String filename = "src/main/java/saveFiles/" + name + ".json";
        JsonObject reader = JsonParser.parseReader(new FileReader(filename)).getAsJsonObject();

        String dungeonId = reader.get("dungeonId").getAsString();
        String dungeonName = reader.get("dungeonName").getAsString();

        Dungeon loadDungeon = new Dungeon(dungeonId, dungeonName);
        DungeonSaver dungeonSave = new DungeonSaver(dungeonId, dungeonName);
        this.dungeon = loadDungeon;

        JsonArray entitiesArray = reader.get("entities").getAsJsonArray();
        JsonArray inventoryArray = reader.get("inventory").getAsJsonArray();
        JsonArray buildableArray = reader.get("buildables").getAsJsonArray();
        JsonObject goalObject = reader.get("goalConditions").getAsJsonObject();

        List<EntityResponse> entities = parseLoadedEntities(dungeon, entitiesArray);
        List<ItemResponse> inventory = parseInventory(inventoryArray);
        List<String> buildables = parseBuildables(buildableArray);

        String gameMode = reader.get("gameMode").getAsString();
        Position dungeonEntrance = getJsonEntrance(reader);
        GoalInterface goals = parseGoals(goalObject, entities);

        this.dungeon.setGoals(goals);
        this.dungeon.setGameMode(gameMode);
        dungeonSave.setGameMode(gameMode);
        this.dungeon.setDungeonEntrance(dungeonEntrance);
        this.dungeon.setDungeonSave(dungeonSave);

        updatePlayerObservers(this.dungeon);

        DungeonResponse game = new DungeonResponse(dungeonId, dungeonName, entities, inventory, buildables,
                goals.evaluate(entities));

        this.dungeon.setEntityCounter(this.id);
        return game;
    }

    /**
     * Gets the JSON entrance and returns it as a position
     * 
     * @return position
     */
    public Position getJsonEntrance(JsonObject reader) {
        JsonObject jsonEntrance = reader.get("entrance").getAsJsonObject();
        int x = jsonEntrance.get("x").getAsInt();
        int y = jsonEntrance.get("y").getAsInt();
        int layer = jsonEntrance.get("layer").getAsInt();
        return new Position(x, y, layer);
    }

    /**
     * Turns a Json array from a new game file into a list of entity responses
     * 
     * @param entitiesArray
     * @return a list of entity responses
     */
    public List<EntityResponse> parseEntities(Dungeon dungeon, JsonArray entitiesArray) {
        List<EntityResponse> entities = new ArrayList<>();

        int i = 0;
        while (i < entitiesArray.size()) {
            JsonObject entity = entitiesArray.get(i).getAsJsonObject();
            int x = entity.get("x").getAsInt();
            int y = entity.get("y").getAsInt();
            Position position = new Position(x, y);
            String type = entity.get("type").getAsString();

            createEntity(dungeon, id, type, position, entities, entity, false);

            i++;
            this.id += 1;
        }

        return entities;
    }

    /**
     * Turns a JsonArray with pre-loaded layers and ids into a list of entity
     * response
     * 
     * @param entitiesArray
     * @return a list of entity responses
     */
    public List<EntityResponse> parseLoadedEntities(Dungeon dungeon, JsonArray entitiesArray) {
        List<EntityResponse> entities = new ArrayList<>();

        int i = 0;
        while (i < entitiesArray.size()) {
            JsonObject entity = entitiesArray.get(i).getAsJsonObject();
            String type = entity.get("entityType").getAsString();

            JsonObject p = entity.get("position").getAsJsonObject();
            int x = p.get("x").getAsInt();
            int y = p.get("y").getAsInt();
            Position position = new Position(x, y);

            createEntity(dungeon, id, type, position, entities, entity, true);

            i++;
            this.id += 1;
        }

        return entities;
    }

    /**
     * Creates a new entity
     * 
     * @param dungeon
     * @param id
     * @param type
     * @param position
     * @param entities
     * @param entity
     * @param load
     */
    public void createEntity(Dungeon dungeon, int id, String type, Position position, List<EntityResponse> entities,
            JsonObject entity, boolean load) {
        if (getStaticTypes().contains(type)) {
            entities.add(createStaticEntity(dungeon, id, type, position.asLayer(STATIC), entity));

        } else if (getMovingTypes().contains(type)) {
            entities.add(createMovingEntity(dungeon, id, type, position.asLayer(MOVING), entity, load));

        } else if (getCollectableTypes().contains(type)) {
            entities.add(createCollectableEntity(dungeon, id, type, position.asLayer(COLLECTABLES), entity, load));
        }
    }

    /**
     * Turns a Json object into a goal interface
     * 
     * @param goalObject
     * @param entities
     * @return a goal interface
     */
    public GoalInterface parseGoals(JsonObject goalObject, List<EntityResponse> entities) {
        String goalLogic = goalObject.get("goal").getAsString();
        JsonArray subgoalArray = null;
        List<GoalInterface> goals = null;

        switch (goalLogic) {
        case "OR":
            subgoalArray = goalObject.get("subgoals").getAsJsonArray();
            goals = getSubGoals(subgoalArray, entities);
            return new GoalOr(goals);

        case "AND":
            subgoalArray = goalObject.get("subgoals").getAsJsonArray();
            goals = getSubGoals(subgoalArray, entities);
            return new GoalAnd(goals);

        case "exit":
            return new GoalExit();

        case "boulders":
            return new GoalBoulders();

        case "treasure":
            return new GoalTreasure();

        case "enemies":
            return new GoalEnemies();

        default:
            return null;
        }

    }

    /**
     * Turns an Json array of subgoals into a list of goal interfaces
     * 
     * @param subgoalArray
     * @param entities
     * @return a list of goal interfaces
     */
    public List<GoalInterface> getSubGoals(JsonArray subgoalArray, List<EntityResponse> entities) {
        List<GoalInterface> goals = new ArrayList<>();
        for (JsonElement e : subgoalArray) {
            JsonObject subGoal = e.getAsJsonObject();
            goals.add(parseGoals(subGoal, entities));
        }
        return goals;
    }

    /**
     * Turns a JsonArray into a list of item responses
     * 
     * @param inventoryArray
     * @return a list of item responses
     */
    public List<ItemResponse> parseInventory(JsonArray inventoryArray) {
        List<ItemResponse> inventory = new ArrayList<>();

        int i = 0;
        while (i < inventoryArray.size()) {
            JsonObject entity = inventoryArray.get(i).getAsJsonObject();
            String type = entity.get("type").getAsString();

            inventory.add(new ItemResponse(String.valueOf(i), type));

            i++;
            this.id += 1;
        }

        return inventory;
    }

    /**
     * Turns a JsonArray of buildable items into a list of String
     * 
     * @param buildableArray
     * @return a list of string
     */
    public List<String> parseBuildables(JsonArray buildableArray) {
        List<String> buildables = new ArrayList<>();

        for (JsonElement i : buildableArray) {
            JsonObject entity = i.getAsJsonObject();
            String type = entity.get("type").getAsString();

            buildables.add(type);
        }

        return buildables;
    }

    /**
     * Updates the player observers
     * 
     * @param dungeon
     */
    public void updatePlayerObservers(Dungeon dungeon) {
        Player player = dungeon.getPlayer();
        for (Position p : dungeon.getMap().keySet()) {
            if (p.getLayer() == MOVING) {
                List<Entity> entities = dungeon.getMap().get(p);
                for (Entity e : entities) {
                    addObserverMob(player, e);
                }
            }
        }
        player.notifyObservers();
    }

    /**
     * Adds an observer to the player
     * 
     * @param player
     * @param entity
     */
    public void addObserverMob(Player player, Entity e) {
        String type = e.getEntityType();
        if (type.equals("mercenary")) {
            Mercenary merc = (Mercenary) e;
            player.addObservers(merc);
        } else if (type.equals("spider")) {
            Spider spider = (Spider) e;
            player.addObservers(spider);
        } else if (type.equals("zombie_toast")) {
            ZombieToast zombie = (ZombieToast) e;
            player.addObservers(zombie);
        }
    }

    /**
     * Fills the dungeon map with empty position keys if there are no entities on
     * that positions
     * 
     * @param dungeon
     */
    public void fillMap(Dungeon dungeon) {
        for (int x = 0; x < dungeon.getDimensionX(); x++) {
            for (int y = 0; y < dungeon.getDimensionY(); y++) {
                if (!dungeon.getMap().containsKey(new Position(x, y).asLayer(MOVING))) {
                    dungeon.getMap().put(new Position(x, y).asLayer(MOVING), null);
                }
            }
        }
    }

    /**
     * Helper function: Creates a static entity
     * 
     * @param dungeon
     * @param id
     * @param type
     * @param position
     * @param entity
     * @return EntityResponse
     */
    public EntityResponse createStaticEntity(Dungeon dungeon, int id, String type, Position position,
            JsonObject entity) {

        String stringId = String.valueOf(id);

        switch (type) {
        case "zombie_toast_spawner":
            dungeon.addEntity(new ZombieToastSpawner(dungeon, position, id, type));
            return new EntityResponse(stringId, type, position, true);

        case "wall":
            dungeon.addEntity(new Wall(dungeon, position, id, type));
            return new EntityResponse(stringId, type, position, false);

        case "boulder":
            dungeon.addEntity(new Boulder(dungeon, position, id, type));
            return new EntityResponse(stringId, type, position, false);

        case "exit":
            dungeon.addEntity(new Exit(dungeon, position, id, type));
            return new EntityResponse(stringId, type, position, false);

        case "door":
            Integer keyId = entity.get("key").getAsInt();
            dungeon.addEntity(new Door(dungeon, position, id, type, keyId));
            return new EntityResponse(stringId, type, position, false);

        case "switch":
            dungeon.addEntity(new FloorSwitch(dungeon, position, id, type));
            return new EntityResponse(stringId, type, position, false);

        case "portal":
            String colour = entity.get("colour").getAsString();
            dungeon.addEntity(new Portal(dungeon, colour, position, id, type));
            return new EntityResponse(stringId, type, position, false);

        case "swamp_tile":
            Double movementFactor = entity.get("movement_factor").getAsDouble();
            dungeon.addEntity(new SwampTile(dungeon, position, id, type, movementFactor));
            return new EntityResponse(stringId, type, position, false);

        default:
            return null;
        }
    }

    /**
     * Helper function: Creates a moving entity
     * 
     * @param dungeon
     * @param id
     * @param type
     * @param position
     * @param entity
     * @param load
     * @return EntityResponse
     */
    public EntityResponse createMovingEntity(Dungeon dungeon, Integer id, String type, Position position,
            JsonObject entity, boolean load) {

        Gson gson = new Gson();
        String stringId = String.valueOf(id);

        switch (type) {
        case "mercenary":
            if (!load) {
                dungeon.addEntity(new Mercenary(dungeon, 20, 40, position, id, type, null));
            } else {
                Mercenary merc = gson.fromJson(entity, Mercenary.class);
                dungeon.addEntity(merc);
            }
            return new EntityResponse(stringId, type, position, true);

        case "spider":
            if (!load) {
                dungeon.addEntity(new Spider(dungeon, 5, 10, position, id, type));
            } else {
                Spider spider = gson.fromJson(entity, Spider.class);
                dungeon.addEntity(spider);
            }
            return new EntityResponse(stringId, type, position, false);

        case "zombie_toast":
            if (!load) {
                dungeon.addEntity(new ZombieToast(dungeon, 15, 30, position, id, type, null));
            } else {
                ZombieToast zombie = gson.fromJson(entity, ZombieToast.class);
                dungeon.addEntity(zombie);
            }
            return new EntityResponse(stringId, type, position, false);

        case "assassin":
            if (!load) {
                dungeon.addEntity(new Assassin(dungeon, 30, 50, position, id, type, null));
            } else {
                Assassin assassin = gson.fromJson(entity, Assassin.class);
                dungeon.addEntity(assassin);
            }

        case "hydra":
            if (!load) {
                dungeon.addEntity(new Hydra(dungeon, 20, 40, position, id, type));
            } else {
                Hydra hydra = gson.fromJson(entity, Hydra.class);
                dungeon.addEntity(hydra);
            }

        case "player":
            if (!load) {
                Player player = new Player(dungeon, 20, 100, position, id, type);
                dungeon.addEntity(player);
                dungeon.setPlayer(player);
                dungeon.setDungeonEntrance(position);
            } else {
                Player player = gson.fromJson(entity, Player.class);
                dungeon.addEntity(player);
                dungeon.setPlayer(player);
            }
            return new EntityResponse(stringId, type, position, false);

        default:
            return null;
        }
    }

    /**
     * Helper function: Creates a collectable entity
     * 
     * @param dungeon
     * @param id
     * @param type
     * @param position
     * @param entity
     * @param load
     * @return EntityResponse
     */
    public EntityResponse createCollectableEntity(Dungeon dungeon, Integer id, String type, Position position,
            JsonObject entity, boolean load) {

        Gson gson = new Gson();
        String stringId = String.valueOf(id);

        switch (type) {
        case "treasure":
            dungeon.addEntity(new Treasure(dungeon, position, id, type));
            break;

        case "key":
            Integer keyId = entity.get("key").getAsInt();
            dungeon.addEntity(new Key(dungeon, keyId, position, id, type));
            break;

        case "health_potion":
            dungeon.addEntity(new HealthPotion(dungeon, position, id, type));
            break;

        case "invincibility_potion":
            dungeon.addEntity(new InvincibilityPotion(dungeon, position, id, type));
            break;

        case "invisibility_potion":
            dungeon.addEntity(new InvisibilityPotion(dungeon, position, id, type));
            break;

        case "wood":
            dungeon.addEntity(new Wood(dungeon, position, id, type));
            break;

        case "arrow":
            dungeon.addEntity(new Arrows(dungeon, position, id, type));
            break;

        case "bomb":
            dungeon.addEntity(new Bomb(dungeon, position, id, type));
            break;

        case "sword":
            if (!load) {
                dungeon.addEntity(new Sword(dungeon, MovingEntityHelper.durabilityGenerator(), position, id, type));
            } else {
                Sword sword = gson.fromJson(entity, Sword.class);
                dungeon.addEntity(sword);
            }
            break;
        
        case "anduril":
            if (!load) {
                dungeon.addEntity(new Anduril(dungeon, MovingEntityHelper.durabilityGenerator(), position, id, type));
            } else {
                Anduril anduril = gson.fromJson(entity, Anduril.class);
                dungeon.addEntity(anduril);
            }
            break;

        case "armour":
            if (!load) {
                dungeon.addEntity(new Armour(dungeon, MovingEntityHelper.durabilityGenerator(), position, id, type));
            } else {
                Armour armour = gson.fromJson(entity, Armour.class);
                dungeon.addEntity(armour);
            }
            break;

        case "one_ring":
            dungeon.addEntity(new OneRing(dungeon, position, id, type));
            break;
            
        case "sun_stone":
            dungeon.addEntity(new SunStone(dungeon, position, id, type));
            break;

        }


        return new EntityResponse(stringId, type, position, false);
    }

    /**
     * Gets the static types
     * 
     * @return List<String>
     */
    public List<String> getStaticTypes() {
        return Arrays.asList("wall", "exit", "boulder", "door", "switch", "portal", "zombie_toast_spawner");
    }

    /**
     * Gets the moving types
     * 
     * @return List<String>
     */
    public List<String> getMovingTypes() {
        return Arrays.asList("spider", "zombie_toast", "mercenary", "player", "assasin", "hydra");
    }

    /**
     * Gets the collectable types
     * 
     * @return List<String>
     */
    public List<String> getCollectableTypes() {
        return Arrays.asList("treasure", "key", "health_potion", "invincibility_potion", "invisibility_potion", "wood",
                "arrow", "bomb", "sword", "armour", "one_ring", "anduril", "sun_stone");
    }

    /**
     * Gets the dungeon
     * 
     * @return dungeon
     */
    public Dungeon getDungeon() {
        return dungeon;
    }
}