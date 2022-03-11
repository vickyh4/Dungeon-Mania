package dungeonmania.entities.misc;

import java.util.List;
import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.util.Position;

public class Portal extends Entity {
    private Position portalExit;
    private String colour;

    /**
     * Constructor for a portal
     * 
     * @param dungeon
     * @param colour
     * @param position
     * @param entityID
     * @param entityType
     */
    public Portal(Dungeon dungeon, String colour, Position position, int entityID, String entityType) {
        super(dungeon, position, entityID, entityType);
        this.colour = colour;
    }

    /**
     * Sets the exit of the portal
     */
    public void setPortalExit() {
        for (Entity ent : dungeon.getAllEntities()) {
            if (isCorresponding(ent) && ent.getEntityID() != this.entityID) {
                this.portalExit = ent.getPosition().asLayer(1);
            }
        }

    }

    /**
     * Checks if an entity is the corresponding portal
     * 
     * @param e
     * @return true if corresponding, false otherwise
     */
    public boolean isCorresponding(Entity e) {
        if (e.getEntityType().equals("portal")) {
            Portal portal = (Portal) e;
            return portal.getColour().equals(this.colour);
        }

        return false;
    }

    /**
     * Gets the colour of the portal
     * 
     * @return portal colour
     */
    public String getColour() {
        return this.colour;
    }

    /**
     * Gets the teleported position
     * @return portalExit
     */
    public Position teleport() {
        return portalExit;
    }
}
