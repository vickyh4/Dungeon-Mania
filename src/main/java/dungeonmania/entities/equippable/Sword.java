package dungeonmania.entities.equippable;
import dungeonmania.Dungeon;
import dungeonmania.util.Position;

public class Sword extends Equippable{
    private double damage;
    /**
     * Constructor for sword
     * @param dungeon
     * @param durability
     * @param position
     * @param entityID
     * @param entityType
     */
    public Sword(Dungeon dungeon, int durability, Position position, int entityID, String entityType){
        super(dungeon, durability, position, entityID, entityType);
        this.setDamage(20);
    }

    /**
     * Gets the sword damage
     * @return Double damage
     */
    public double getDamage() {
        return damage;
    }

    /**
     * Sets the swords damage
     * @param damage
     */
    public void setDamage(double damage) {
        this.damage = damage;
    }
}
