package dungeonmania.entities.moving;

import java.util.Iterator;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.equippable.*;
import dungeonmania.entities.misc.Boulder;
import dungeonmania.entities.misc.Door;
import dungeonmania.entities.misc.Key;
import dungeonmania.entities.misc.Portal;
import dungeonmania.exceptions.InvalidActionException;
import dungeonmania.response.models.ItemResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;
import dungeonmania.Dungeon;

import static dungeonmania.DungeonControllerLoader.STATIC;
import static dungeonmania.DungeonControllerLoader.COLLECTABLES;

public class Player extends MovingEntity implements SubjectPlayer {

    private boolean invisible;
    private boolean invincible;
    private int detectableDuration = 10;
    private int invincibleDuration = 5;
    private boolean inCombat;
    private HashMap<String, List<Entity>> inventory = new HashMap<>();
    // private Map<String, List<Equippable>> equipped = new HashMap<>();
    private List<ObserverMob> observers = new ArrayList<>();

    /**
     * Constructor for player
     * 
     * @param dungeon
     * @param health
     * @param position
     * @param entityID
     * @param entityType
     */
    public Player(Dungeon dungeon, double attackDamage, double health, Position position, int entityID,
            String entityType) {
        super(dungeon, attackDamage, health, position, entityID, entityType);
        this.invisible = false;
        this.invincible = false;
        this.inCombat = false;
    }

    /**
     * Getter for invincibility duration
     * 
     * @return duration of invincibility
     */
    public int getInvincibleDuration() {
        return invincibleDuration;
    }

    /**
     * Sets the invincible duration
     */
    public void setInvincibleDuration() {
        this.detectableDuration = 5;
    }

    /**
     * Gets the invincible duration
     * 
     * @return detectableDuration
     */
    public int getDetectableDuration() {
        return detectableDuration;
    }

    /**
     * Sets the detectable duration
     */
    public void setDetectableDuration() {
        this.detectableDuration = 10;
    }

    /**
     * Getter for invisibility
     * 
     * @return false if invisible, true otherise
     */
    public boolean isInvisible() {
        return invisible;
    }

    /**
     * Setter for invisibility
     * 
     * @param detectable
     */
    public void setInvisible(boolean detectable) {
        this.invisible = detectable;
    }

    /**
     * Getter for invincibility
     * 
     * @return true if invincible, false otherwise
     */
    public boolean isInvincible() {
        return invincible;
    }

    /**
     * Setter for invincibility
     * 
     * @param invincible
     */
    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }

    /**
     * Getter for player inventory
     * 
     * @return map of type to list of ids
     */
    public Map<String, List<Entity>> getInventory() {
        return inventory;
    }

    /**
     * Checks if the player is in combat
     * 
     * @return true if in combat, false otherwise
     */
    public boolean isInCombat() {
        return inCombat;
    }

    /**
     * Setter for in combat
     * 
     * @param inCombat
     */
    public void setInCombat(boolean inCombat) {
        this.inCombat = inCombat;
    }

    /**
     * Decrements the duration of buffs
     */
    public void tickDuration() {
        if (this.invisible) {
            tickInvisibleDuration();
        } else if (this.invincible) {
            tickInvincibleDuration();
        }
    }

    /**
     * Remove an existing item from inventory Input: Entity
     */
    public void removeFromInventory(String type, Entity e){
        List<Entity> items = inventory.get(type);
        items.remove(e);
        inventory.put(type, items);
    }

    /**
     * Add an item to inventory with a check for key
     * 
     * @param item
     * @return boolean
     */
    public boolean addToInventory(Entity item) {
        String type = item.getEntityType();
        if (item instanceof Key) {
            if (inventory.containsKey("key")) {
                if (inventory.get("key").size() > 0) {
                    return false;
                }
            }
        }
        if (inventory.containsKey(type)) {
            List<Entity> keyIds = inventory.get(type);
            keyIds.add(item);
            inventory.replace(type, keyIds);
        } else {
            List<Entity> itemIds = new ArrayList<>();
            itemIds.add(item);
            inventory.put(type, itemIds);
        }
        dungeon.removeEntity(item);
        return true;
    }

    /**
     * Checks whether the player has a weapon
     * 
     * @return boolean
     */
    public boolean hasWeapon() {
        if (this.getInventory().containsKey("bow")) {
            if (this.getInventory().get("bow").size() > 0) {
                return true;
            }
        } else if (this.getInventory().containsKey("sword")) {
            if (this.getInventory().get("sword").size() > 0) {
                return true;
            }
        } else if (this.getInventory().containsKey("anduril")) {
            if (this.getInventory().get("anduril").size() > 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * Use an item from the player's inventory
     * 
     * @param item
     */
    public void useItem(Entity item) {
        dungeon.getGameMode().useItem(this, item);
        removeFromInventory(item.getEntityType(), item);
    }

    /**
     * Builds a buildable item and adds to player inventory
     * 
     * @param item
     */
    public void buildItem(String item) {
        if (item.equals("bow")) {
            if (buildBowValid()) {
                Bow bow = new Bow(dungeon, MovingEntityHelper.durabilityGenerator(), null, dungeon.getNewEntityId(),
                        "bow");
                addToInventory(bow);
                dungeon.addEntity(bow);
                removeFromInventory("wood", inventory.get("wood").get(0));
                for (int i = 0; i < 3; i++) {
                    removeFromInventory("arrow", inventory.get("arrow").get(0));
                }
            }
        } else if (item.equals("shield")) {
            if (buildShieldValid()) {
                Shield shield = new Shield(dungeon, MovingEntityHelper.durabilityGenerator(), null,
                        dungeon.getNewEntityId(), "shield");
                addToInventory(shield);
                // dungeon.addEntity(shield);
                for (int i = 0; i < 2; i++) {
                    removeFromInventory("wood", inventory.get("wood").get(0));
                }
                try {
                    removeFromInventory("treasure", inventory.get("treasure").get(0));
                } catch (NullPointerException e) {
                    removeFromInventory("key", inventory.get("key").get(0));
                }
            }
        } else if (item.equals("sceptre")) {
            if (buildSceptreValid()) {
                Sceptre sceptre = new Sceptre (dungeon, MovingEntityHelper.durabilityGenerator(), null, dungeon.getNewEntityId(), "sceptre");
                addToInventory(sceptre);
                dungeon.addEntity(sceptre);

                if (getInventory().get("arrow") != null) {
                    if (getInventory().get("arrow").size() > 2) {
                        for (int i = 0 ; i < 1 ; i++) {
                            removeFromInventory("arrow", inventory.get("arrow").get(0));
                        }
                    }
                } else {
                    removeFromInventory("wood", inventory.get("wood").get(0));

                }

                if (getInventory().get("key") != null) {
                    removeFromInventory("key", inventory.get("key").get(0));
                }
                else {
                    removeFromInventory("treasure", inventory.get("treasure").get(0));
                }

                // Unsure if should keep as Sun_stone isn't removable?
                removeFromInventory("sun_stone", inventory.get("sun_stone").get(0));

            }
        } else if (item.equals("midnight_armour")) {
            if (buildMidnightArmourValid()) {
                MidnightArmour midnightArmour = new MidnightArmour(dungeon, MovingEntityHelper.durabilityGenerator(), null, dungeon.getNewEntityId(), "midnight_armour");
                addToInventory(midnightArmour);
                dungeon.addEntity(midnightArmour);

                removeFromInventory("armour", inventory.get("armour").get(0));
                
                // Unsure if should keep as Sun_stone isn't removable?
                removeFromInventory("sun_stone", inventory.get("sun_stone").get(0));
            }
        }
    }

    /**
     * Bribes a mercenary if the player has enough gold or a sunstone
     * If a player has at least one gold, then difference between gold needed to bribe mercenary
     * Can be subsidized by sun stone
     * 
     * @param merc
     */
    public void bribe(Mercenary merc) throws InvalidActionException {
        List<Entity> golds = this.inventory.get("treasure");

        int numSunStone = 0;

        if (this.inventory.get("sun_stone") != null) {
            numSunStone = this.inventory.get("sun_stone").size();
        }

        if (golds != null && (golds.size() + numSunStone) >= merc.getGold() && assassinBribe(merc)) {
            merc.setHostile(false);
            for (int i = 0; i < ( merc.getGold() - numSunStone) ; i++) {
                removeFromInventory("treasure", golds.get(0));
            }
        } else {
            throw new InvalidActionException("Not enough to bribe");
        }
    }

    /**
     * Checks if an assasin is bribed
     * 
     * @param merc
     * @return boolean
     * @throws InvalidActionException
     */
    public boolean assassinBribe(Mercenary merc) throws InvalidActionException {
        if (merc.getEntityType() == "assassin") {
            List<Entity> rings = this.inventory.get("one_ring");
            if (rings != null && rings.size() > 0) {
                removeFromInventory("one_ring", rings.get(0));
                return true;
            } else {
                throw new InvalidActionException("Not enough to bribe");
            }
        } else {
            return true;
        }
    }

    /**
     * Checks if the player has an item in their inventory
     * 
     * @param item
     * @return
     */
    public boolean checkItemInventory(Entity item) {
        List<Entity> invent = this.inventory.get(item.getEntityType());
        if (invent != null) {
            return invent.contains(item);
        } else {
            return false;
        }
    }

    /**
     * Getter for buildables
     * 
     * @return A list of buildable objects from items in the player's inventory
     */
    public List<String> getBuildables() {
        List<String> buildables = new ArrayList<>();
        if (buildBowValid()) {
            buildables.add("bow");
        }
        if (buildShieldValid()) {
            buildables.add("shield");
        } if (buildSceptreValid()) {
            buildables.add("sceptre");
        } if (buildMidnightArmourValid()) {
            buildables.add("midnight_armour");
        }
        return buildables;
    }

    /**
     * Getter for item responses
     * 
     * @return A list of responses of items in the player's inventory
     */
    public List<ItemResponse> getInventoryResponses() {
        List<ItemResponse> inventory = new ArrayList<>();
        if (this.inventory.isEmpty()) {
            return inventory;
        } else {
            for (List<Entity> entities : this.inventory.values()) {
                for (Entity entity : entities) {
                    ItemResponse itemResponse = new ItemResponse(entity.getEntityID().toString(),
                            entity.getEntityType());
                    inventory.add(itemResponse);
                }
            }
        }
        return inventory;
    }

    /**
     * Moves the player in a certain direction
     * 
     * @param direction
     */
    public void moveDirection(Direction direction) {
        Position newPosition = getPosition().translateBy(direction);
        Position toPosition = newPosition;
        List<Entity> staticEntities = dungeon.getEntitiesAtLayer(newPosition, STATIC);

        for (Entity e : dungeon.getAllEntities()) {
            if (e instanceof Portal && e.getPosition().equals(toPosition)) {
                Portal portal = (Portal) e;
                toPosition = portal.teleport();
                break;
            }
        }

        if (staticEntities != null) {
            Iterator<Entity> it = staticEntities.iterator();
            while (it.hasNext()) {
                Entity entity = it.next();
                if (!isFree(entity, direction, toPosition)) {
                    toPosition = this.getPosition();
                }
            }
            interact(staticEntities, direction);
        }
        List<Entity> collectableEntities = new ArrayList<>();
        if (dungeon.getEntitiesAtLayer(newPosition, COLLECTABLES) != null) {
            collectableEntities = new ArrayList<>(dungeon.getEntitiesAtLayer(newPosition, COLLECTABLES));
        }
        if (collectableEntities != null) {
            for (Entity item : collectableEntities) {
                addToInventory(item);
            }
        }
        dungeon.updatePosition(this, getPosition(), toPosition);
        this.setPosition(toPosition);
        notifyObservers();
    }

    /**
     * Checks that the tile the player moves into is free
     * 
     * @param e
     * @param direction
     * @param toPosition
     * @return true if the tile is free, false otherwise
     */
    public boolean isFree(Entity e, Direction direction, Position toPosition) {
        if (e.getEntityType().equals("door")) {
            Door door = (Door) e;
            if (door.isOpened() || door.canBeOpened()) {
                return true;
            }
            return false;

        } else if (e.getEntityType().equals("boulder")) {
            Boulder boulder = (Boulder) e;
            if (boulder.isPushable(direction)) {
                return true;
            }
            return false;

        } else if (e.getEntityType().equals("portal")) {
            Portal portal = (Portal) e;
            toPosition = portal.teleport();
            return true;

        } else if (e.getEntityType().equals("switch")) {
            return true;

        } else if (e.getEntityType().equals("exit")) {
            return true;

        } else {
            return false;
        }
    }

    /**
     * If there is a boulder or door, push it or open it
     * 
     * @param staticEntities
     * @param direction
     */
    public void interact(List<Entity> staticEntities, Direction direction) {
        List<String> entityTypes = staticEntities.stream().map(Entity::getEntityType).collect(Collectors.toList());
        Entity interactable = null;
        for (int i = 0; i < entityTypes.size(); i++) {
            if (entityTypes.get(i).equals("boulder") || entityTypes.get(i).equals("door")) {
                interactable = staticEntities.get(i);
            }
        }

        if (interactable != null) {
            if (interactable.getEntityType().equals("boulder")) {
                Boulder boulder = (Boulder) interactable;
                boulder.push(direction);
            } else if (interactable.getEntityType().equals("door")) {
                Door door = (Door) interactable;
                door.open();
                if (door.isOpened()) {
                    if (!this.inventory.containsKey("sun_stone")) {
                        inventory.remove("key");
                    }
                }
            }
        }
    }

    /**
     * Makes the enemy take damage
     * 
     * @param enemy
     * @param attack
     */
    public void attack(MovingEntity enemy, Double attack) {
        enemy.takeDamage(attack);
    }

    /**
     * Calculates the attack damage of player
     * 
     * @return attack damage
     */
    public Double calculateAttack() {
        Double totalAttack = this.getAttackDamage();
        if (this.inventory.containsKey("sword") || this.inventory.containsKey("anduril")) {
            totalAttack += 20;
        } else if (this.inventory.containsKey("bow")) {
            totalAttack *= 2;
        }

        return (this.getHealth() * totalAttack) / 5;
    }

    /**
     * Helper function: Checks if a shield can be built
     * 
     * @return Boolean
     */
    public Boolean buildShieldValid() {
        if (getInventory() != null && getInventory().get("wood") != null) {
            int numberOfWood = getInventory().get("wood").size();
            int numberOfTreasure = 0;
            int numberOfKey = 0;
            if (getInventory().get("treasure") != null) {
                numberOfTreasure = getInventory().get("treasure").size();
            } else if (getInventory().get("key") != null) {
                numberOfKey = getInventory().get("key").size();
            }
            if ((numberOfTreasure >= 1 || numberOfKey >= 1) && numberOfWood >= 2) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper function: Checks if a bow can be built
     * 
     * @return Boolean
     */
    public Boolean buildBowValid(){
        if (getInventory() != null && getInventory().get("wood") != null && getInventory().get("arrow") != null) {
            int numberOfWood = getInventory().get("wood").size();
            int numberOfArrows = getInventory().get("arrow").size();
            if (numberOfWood >= 1 && numberOfArrows >= 3) {
                return true;
            }
        }
        return false;
    }

    /**
     * Helper function to check if the sceptre can be built
     * @return Boolean
     */
    public Boolean buildSceptreValid() {
        if (getInventory() != null && getInventory().get("sun_stone") != null) {
            if (getInventory().get("key") != null || getInventory().get("treasure") != null) {
                if (getInventory().get("wood") != null || getInventory().get("arrow") != null) {
                    if (getInventory().get("arrows") != null) {
                        int numberOfArrows = getInventory().get("arrow").size();
                        if (numberOfArrows >= 2) {
                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Helper function to check if the Midnight Armour can be built
     * @return Boolean
     */
    public Boolean buildMidnightArmourValid() {
        if (getInventory().get("armour") != null && getInventory().get("sun_stone") != null) {
            return true;
        } 
        return false;
    }


    /**
     * Helper function:
     * Decrements the duration of invisibility buff
     */
    private void tickInvisibleDuration() {
        int detectableDuration = getDetectableDuration();
        detectableDuration = detectableDuration - 1;
        if (detectableDuration <= 0) {
            setInvisible(false);
            setDetectableDuration();
        }
    }

    /**
     * Helper function: Decrements the duration of invincibility buff
     */
    private void tickInvincibleDuration() {
        int invincibleDuration = getInvincibleDuration();
        invincibleDuration = invincibleDuration - 1;
        if (invincibleDuration <= 0) {
            setInvincible(false);
            setInvincibleDuration();
        }
    }

    @Override
    public void addObservers(ObserverMob o) {
        this.observers.add(o);
    }

    @Override
    public void removeObservers(ObserverMob o) {
        this.observers.remove(o);
    }

    @Override
    public void notifyObservers() {
        for (ObserverMob o : observers) {
            o.update(this);
        }
    }

    @Override
    public Player getSubjectPlayer() {
        return this;
    }

}
