package dungeonmania.entities.moving;

import java.util.List;
import java.util.Random;

import dungeonmania.Battle;
import dungeonmania.Dungeon;
import dungeonmania.entities.Entity;
import dungeonmania.entities.equippable.Anduril;
import dungeonmania.entities.misc.OneRing;
import dungeonmania.util.Position;

public class MovingEntity extends Entity implements Battle {
    private double attackDamage;
    private double health;

    public MovingEntity(Dungeon dungeon, double attackDamage, double health, Position position, int entityID,
            String entityType) {
        super(dungeon, position, entityID, entityType);
        this.attackDamage = attackDamage;
        this.health = health;
    }

    public double getAttackDamage() {
        return attackDamage;
    }

    public void setAttackDamage(double attackDamage) {
        this.attackDamage = attackDamage;
    }

    public double getHealth() {
        return health;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void moveEntity(MovingEntity entity) {
        String type = entity.getEntityType();

        if (type.equals("mercenary")) {
            Mercenary merc = (Mercenary) entity;
            merc.moveEntity();
        } else if (type.equals("spider")) {
            Spider spider = (Spider) entity;
            spider.moveEntity();
        } else if (type.equals("zombie_toast")) {
            ZombieToast zombie = (ZombieToast) entity;
            zombie.moveEntity();
        } else if (type.equals("hydra")) {
            Hydra hydra = (Hydra) entity;
            hydra.moveEntity();
        } else if (type.equals("assassin")) {
            Assassin assassin = (Assassin) entity;
            assassin.moveEntity();
        }
    }

    public void takeDamage(Double attack) {
        this.health = this.health - attack;
    }

    public void enemyAttack(Player player, Double attack) {
        player.takeDamage(attack);
    }

    @Override
    public void combat(Player player, MovingEntity enemy) {
        Double playerAttack = player.calculateAttack();

        // Check to see if anduril is in inventory and combatting a boss
        if (player.getInventory().containsKey("anduril")) {
            if (enemy.entityType.equals("assassin") || enemy.entityType.equals("hydra")) {
                playerAttack = playerAttack * 3; 
            }
        }

        Double enemyAttack = enemy.getHealth() * enemy.getAttackDamage() / 10;

        // Check to see if player is wearing an armour 
        if (player.getInventory().containsKey("armour")) {
            enemyAttack = enemyAttack / 2;
        }

        // Checks to see if player is wearing midnight armour 
        if (player.getInventory().containsKey("midnight_armour")) {
            enemyAttack = enemyAttack -15;
            playerAttack = playerAttack + 15;
        }

        player.attack(enemy, playerAttack);
        enemy.enemyAttack(player, enemyAttack);

        if (player.getHealth() <= 0) {
            List<Entity> rings = player.getInventory().get("one_ring");
            if (rings != null && rings.size() > 0) {
                player.removeFromInventory("one_ring", rings.get(0));
                player.setHealth(100);
            } else {
                player.setInCombat(false);
                dungeon.removeEntity(player);
                dungeon.removeEntityID(player);
            }
        }

        if (enemy.getHealth() <= 0) {
            dropArmour(player, enemy);
            if (dropRoll()) {

                Random rand = new Random();

                int chooseRingOrAnduril = rand.nextInt(2);

                if (chooseRingOrAnduril == 1) {
                    Integer ringId = dungeon.getNewEntityId();
                    OneRing oneRing = new OneRing(this.dungeon, null, ringId, "one_ring");
                    player.addToInventory(oneRing);
                } else {
                    Integer andurilID = dungeon.getNewEntityId();
                    Anduril anduril = new Anduril(this.dungeon,5, null, andurilID, "anduril");
                    player.addToInventory(anduril);
                }


            }
            dungeon.removeEntity(enemy);
            dungeon.removeEntityID(enemy);
            player.setInCombat(false);
        }

    }

    @Override
    public void dropArmour(Player player, MovingEntity enemy) {
        if (enemy instanceof Mercenary) {
            Mercenary mercenary = (Mercenary) enemy;
            if (mercenary.hasArmour()) {
                player.addToInventory(mercenary.getArmour());
            }
        } else if (enemy instanceof ZombieToast) {
            ZombieToast zombieToast = (ZombieToast) enemy;
            if (zombieToast.hasArmour()) {
                player.addToInventory(zombieToast.getArmour());
            }
        }
    }

    @Override
    public boolean dropRoll() {
        Random random = new Random(System.currentTimeMillis());
        int chance = random.nextInt(100);
        return chance < 5 ? true : false;
    }

}
