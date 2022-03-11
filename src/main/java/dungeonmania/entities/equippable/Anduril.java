package dungeonmania.entities.equippable;
import dungeonmania.Dungeon;
import dungeonmania.util.Position;

public class Anduril extends Sword{

    private double damage;

    public Anduril(Dungeon dungeon, int durability, Position position, int entityID, String entityType){
        super(dungeon, durability, position, entityID, entityType);
        this.setDamage(25);
    }
    
    public double getDamage() {
        return damage;
    }
    public void setDamage(double damage) {
        this.damage = damage;
    }

}

