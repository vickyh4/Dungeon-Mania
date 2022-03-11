package dungeonmania.entities.moving;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.entities.equippable.Armour;
import dungeonmania.entities.misc.Portal;
import dungeonmania.entities.misc.SwampTile;
import dungeonmania.util.Position;

public class Mercenary extends MovingEntity implements MovingInterface, ObserverMob {
    private int gold;
    private Armour armour;
    private boolean hostile = true;
    private int battleRadius;
    private transient Player player;

    /**
     * Constructor for mercenary
     * 
     * @param dungeon
     * @param attackDamage
     * @param health
     * @param position
     * @param entityID
     * @param entityType
     * @param armour
     */
    public Mercenary(Dungeon dungeon, double attackDamage, double health, Position position, int entityID,
            String entityType, Armour armour) {
        super(dungeon, attackDamage, health, position, entityID, entityType);
        this.armour = armour;

        this.gold = goldGenerator();
        this.battleRadius = 5;
    }

    /**
     * Sets the player field for this mercenary
     * 
     * @param player
     */
    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Part of the observer method that updates the player for the mercenary
     * 
     * @param player
     */
    public void update(SubjectPlayer player) {
        this.player = player.getSubjectPlayer();
    }

    /**
     * Getter for gold
     * 
     * @return how much the mercenary needs to be bribed
     */
    public int getGold() {
        return gold;
    }

    /**
     * Getter for hostility
     * 
     * @return whether the mercenary is hostile
     */
    public boolean isHostile() {
        return hostile;
    }

    /**
     * Setter for hostility
     * 
     * @param hostile
     */
    public void setHostile(boolean hostile) {
        this.hostile = hostile;
    }

    /**
     * Getter for battle radius
     * 
     * @return mercenary's battle radius
     */
    public int getBattleRadius() {
        return battleRadius;
    }

    /**
     * Getter for the player's position
     * 
     * @return player's position
     */
    public Position getPlayerPosition() {
        return player.getPosition();
    }

    /**
     * Checks whether the mercenary has armour
     * 
     * @return
     */
    public boolean hasArmour() {
        return (armour == null) ? false : true;
    }

    /**
     * Getter for the mercenary's armour
     * 
     * @return armour
     */
    public Armour getArmour() {
        return armour;
    }

    @Override
    public void moveEntity() {
        try {
            if (player.isInvincible() && this.isHostile()) {
                // If the player is invincible, move away from the player
                moveDirection(MovingEntityHelper.moveAwayFromPlayer(dungeon, this.getPlayerPosition(), this.position));
            } else if (player.isInvisible()) {
                moveDirection(this.position);
            } else if (player.isInCombat() && withinRadius()) {
                Map<Position, Position> map = dijkstras(dungeon.getMap(), this.position);
                Position next = nextPosition(map, this.player.getPosition());
                next = nextPosition(map, this.player.getPosition());
                moveDirection(next);
            } else {
                Map<Position, Position> map = dijkstras(dungeon.getMap(), this.position);
                Position next = nextPosition(map, this.player.getPosition());
                moveDirection(next);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void moveDirection(Position position) {
        for (Entity e : dungeon.getAllEntities()) {
            if (e instanceof Portal && e.getPosition().equals(position)) {
                Portal portal = (Portal) e;
                position = portal.teleport();
                break;
            }
        }
        dungeon.updatePosition(this, this.position, position);
        this.setPosition(position);
    }

    /**
     * Helper function: Generates a random number between 1 - 3
     * 
     * @return the amount the mercenary needs to be bribed
     */
    private int goldGenerator() {
        int max = 3;
        int min = 1;
        int gold = (int) Math.floor(Math.random() * (max - min + 1) + min);
        return gold;
    }

    /**
     * Helper function: Dijkstra's algorithm to calculate shortest path to player
     * 
     * @param map
     * @param mercPosition
     * @return map of path
     */
    private Map<Position, Position> dijkstras(Map<Position, List<Entity>> map, Position mercPosition) {
        Map<Position, Double> dist = new HashMap<>();
        Map<Position, Position> prev = new HashMap<>();
        Map<Position, Boolean> visited = new HashMap<>();

        Map<Position, List<Entity>> available = MovingEntityHelper.getAvailablePositions(map, dungeon);

        for (Position p : available.keySet()) {
            dist.put(p, Double.POSITIVE_INFINITY);
            prev.put(p, null);
            visited.put(p, false);
        }

        dist.put(mercPosition, 0.0);
        visited.put(mercPosition, true);

        Queue<Position> q = new LinkedList<Position>();
        q.addAll(available.keySet());
        while (!q.isEmpty()) {
            Position u = getNextPosition(dist, q);
            List<Position> cardinals = MovingEntityHelper.getCardinals(u);
            for (Position v : cardinals) {
                Double distance = dist.get(u) + cost(u, v);
                if (available.containsKey(v) && !visited.get(v)) {
                    if (distance < dist.get(v)) {
                        dist.put(v, distance);
                        prev.put(v, u);
                    }
                }
            }
            q.remove(u);
        }
        return prev;
    }

    /**
     * Helper function: Checks for portals in the mercenary's path
     * 
     * @param cardinals
     */
    private void checkForPortals(List<Position> cardinals) {
        for (int i = 0; i < cardinals.size(); i++) {
            List<Entity> entities = this.dungeon.getEntitiesAtLayer(cardinals.get(i), 0);
            if (entities != null) {
                for (Entity e : entities) {
                    if (e.getEntityType().equals("portal")) {
                        Portal portal = (Portal) e;
                        cardinals.add(portal.teleport());
                    }
                }
            }
        }
    }

    /**
     * Calculates how many ticks it takes to move across a tile
     * 
     * @param u
     * @param v
     * @return amount of ticks
     */
    private Double cost(Position u, Position v) {
        List<Entity> entities = dungeon.getMap().get(v);
        if (entities == null || entities.isEmpty()) {
            return 1.0;
        }

        Iterator<Entity> it = entities.iterator();
        Double cost = 1.0;
        while (it.hasNext()) {
            Entity e = it.next();
            if (e.getEntityType().equals("swamp_tile")) {
                SwampTile tile = (SwampTile) e;
                cost = tile.getMovementFactor();
            }
        }
        return cost;
    }

    /**
     * Gets the next position in the queue with the least distance
     * 
     * @param dist
     * @param q
     * @return position with least distance
     */
    public Position getNextPosition(Map<Position, Double> dist, Queue<Position> q) {
        Position position = null;
        Double distance = Double.POSITIVE_INFINITY;
        Iterator<Position> it = q.iterator();

        while (it.hasNext()) {
            Position next = it.next();
            if (dist.get(next) <= distance) {
                position = next;
                distance = dist.get(next);
            }
        }
        return position;
    }

    /**
     * Gets the next position that the mercenary should move to
     * 
     * @param map
     * @param target
     * @return
     */
    public Position nextPosition(Map<Position, Position> map, Position target) {
        if (MovingEntityHelper.getCardinals(target).contains(this.getPosition())) {
            return target;
        }
        target = map.get(target);
        return nextPosition(map, target);
    }

    /**
     * Check if the player is within battle radius
     * 
     * @return boolean
     */
    public boolean withinRadius() {
        Position posCheck = Position.calculatePositionBetween(player.getPosition(), this.position);
        if (Math.abs(posCheck.getX()) <= this.battleRadius && Math.abs(posCheck.getY()) <= this.battleRadius) {
            return true;
        } else {
            return false;
        }
    }
}
