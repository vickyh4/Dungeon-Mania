package dungeonmania;

import dungeonmania.entities.Entity;
import dungeonmania.entities.misc.Portal;
import dungeonmania.entities.moving.Mercenary;
import dungeonmania.entities.moving.Player;
import dungeonmania.exceptions.InvalidActionException;

import dungeonmania.response.models.*;
import dungeonmania.util.*;
import com.google.gson.*;

import java.io.*;
import java.util.*;

public class DungeonManiaController {
    private Dungeon dungeon;

    /**
     * Constructor for the Dungeon Mania Controller
     */
    public DungeonManiaController() {
    }

    public String getSkin() {
        return "default";
    }

    public String getLocalisation() {
        return "en_US";
    }

    public List<String> getGameModes() {
        return Arrays.asList("standard", "peaceful", "hard");
    }

    /**
     * Lists the names of available dungeon maps
     */
    public static List<String> dungeons() {
        try {
            return FileLoader.listFileNamesInResourceDirectory("/dungeons");
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    /**
     * Creates a new game by reading a Json file and parsing it into a dungeon
     * response
     * 
     * @param dungeonName
     * @param gameMode
     * @return a dungeon response for the new game
     * @throws IllegalArgumentException
     */
    public DungeonResponse newGame(String dungeonName, String gameMode) throws IllegalArgumentException {
        // Check that the game mode and dungeon is valid
        if (!getGameModes().contains(gameMode)) {
            throw new IllegalArgumentException("Invalid game mode");
        } else if (!dungeons().contains(dungeonName)) {
            throw new IllegalArgumentException("Invalid dungeon");
        }

        DungeonControllerLoader loader = null;
        DungeonResponse newGame = null;

        try {
            loader = new DungeonControllerLoader(dungeonName);
            newGame = loader.loadNewGame(gameMode);
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
            e.printStackTrace();
        }

        this.dungeon = loader.getDungeon();

        return newGame;
    }

    /**
     * Saves a game by writing dungeon response to a JSON file in directory
     * saveFiles
     * 
     * @param name
     * @return a dungeon response
     * @throws IllegalArgumentException
     */
    public DungeonResponse saveGame(String name) throws IllegalArgumentException {
        DungeonResponse response = this.dungeon.getDungeonResponse();

        this.dungeon.getDungeonSave().setAll(response, this.dungeon.getAllEntities());
        this.dungeon.getDungeonSave().setGoals(this.dungeon.getGoalInterface());

        try {
            String filename = "src/main/java/saveFiles/" + name + ".json";
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(this.dungeon.getDungeonSave());
            File saveFile = new File(filename);
            saveFile.createNewFile();
            FileWriter fw = new FileWriter(filename, false);
            fw.write(json);
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response;
    }

    /**
     * Loads a game from saveFiles directory
     * 
     * @param name
     * @return a dungeon response of the loaded dungeon
     */
    public DungeonResponse loadGame(String name) throws IllegalArgumentException {
        try {
            List<String> saveFiles = FileLoader.listFileNamesInDirectoryOutsideOfResources("src/main/java/saveFiles");
            if (!saveFiles.contains(name)) {
                throw new IllegalArgumentException("Save file does not exist.");
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        DungeonControllerLoader loader = null;
        DungeonResponse loadGame = null;

        try {
            loader = new DungeonControllerLoader(name);
            loadGame = loader.loadSaveFile(name);
        } catch (JsonIOException | JsonSyntaxException | FileNotFoundException e) {
            e.printStackTrace();
        }

        return loadGame;
    }

    /**
     * Returns a string of all names of the saved games
     */
    public List<String> allGames() {
        List<String> saveFiles = null;
        try {
            saveFiles = FileLoader.listFileNamesInDirectoryOutsideOfResources("src/main/java/saveFiles");
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        return saveFiles;
    }

    /**
     * Ticks the game whenever the player moves or an item is used
     * 
     * @param itemUsed
     * @param movementDirection
     * @return
     * @throws IllegalArgumentException
     * @throws InvalidActionException
     */
    public DungeonResponse tick(String itemUsed, Direction movementDirection)
            throws IllegalArgumentException, InvalidActionException {

        this.dungeon.checkForCombat();
        if (!this.dungeon.playerIsAlive()) {
            return this.dungeon.getDungeonResponse();
        }
        // Check for items being used
        linkPortals();
        if (itemUsed != null) {
            dungeon.tickDungeonItem(itemUsed);
        }
        if (!movementDirection.equals(Direction.NONE)) {
            dungeon.tickDungeonMovement(movementDirection);
        }

        // Decrement duration of potions in use by player
        this.dungeon.getPlayer().tickDuration();
        // Spawn enemies
        this.dungeon.getGameMode().spawnEnemies();
        DungeonResponse response = this.dungeon.getDungeonResponse();

        return response;
    }

    /**
     * Interacts with mercenary or zombie toast spawner entity
     * 
     * @param entityId
     * @return dungeon response
     * @throws IllegalArgumentException
     * @throws InvalidActionException
     */
    public DungeonResponse interact(String entityId) throws IllegalArgumentException, InvalidActionException {
        Entity e = this.dungeon.getEntityIds().get(Integer.parseInt(entityId));
        Player player = this.dungeon.getPlayer();
        Position positionVec = Position.calculatePositionBetween(e.getPosition(), player.getPosition());

        if (e.getEntityType().equals("mercenary")) {
            Mercenary merc = (Mercenary) e;
            if (Math.abs(positionVec.getX()) <= 2 && Math.abs(positionVec.getY()) <= 2) {
                player.bribe(merc);
            } else {
                throw new InvalidActionException("Mercenary is too far away");
            }
        } else if (e.getEntityType().equals("zombie_toast_spawner")) {
            if (player.hasWeapon()) {
                if (Position.isAdjacent(player.getPosition(), e.getPosition())) {
                    this.dungeon.removeEntity(e);
                } else {
                    throw new InvalidActionException("Player does not have a weapon");
                }
            } else {
                throw new InvalidActionException("Player is too far");
            }

        } else {
            throw new IllegalArgumentException("Cannot interact with this entity");
        }

        DungeonResponse response = this.dungeon.getDungeonResponse();

        return response;
    }

    /**
     * Builds a buildable object given its type
     * 
     * @param buildable
     * @return dungeon response
     */
    public DungeonResponse build(String buildable) throws IllegalArgumentException, InvalidActionException {
        dungeon.getPlayer().buildItem(buildable);
        DungeonResponse response = this.dungeon.getDungeonResponse();
        return response;
    }

    /**
     * Helper function to get dungeon field
     */
    public Dungeon getDungeon() {
        return this.dungeon;
    }

    /**
     * Helper function to link same coloured portals together if they exist
     */
    public void linkPortals() {
        for (Entity ent : dungeon.getAllEntities()) {
            if (ent instanceof Portal) {
                Portal portal = (Portal) ent;
                portal.setPortalExit();
            }
        }
    }

    public static void main(String args[]) {
        DungeonManiaController controller = new DungeonManiaController();
        controller.newGame("advanced", "standard");
        controller.tick(null, Direction.RIGHT);
        controller.saveGame("save2");
    }

}
