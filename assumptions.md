# Dungeon Mania - Assumptions - F11B CELERY

## Milestone 1

- `Collectible` and `Static` entities are never placed in the same position
- Battle is 1v1 only, cycle through enemies in the same cell
- Mercenary doesn't doesn't carry any particular weapon 
- When bribed, the mercenary stores the gold, and when killed, drops the gold
- The key for the door is never behind the door
- For every door spawned, there has to be at least one key on the map that corresponds to the door
- Spiders can spawn on anything but a boulder
- Items are walked over unless picked up 
- If the character already holds a key to a door, other instances of the key cannot be picked up

## Milestone 2

- When spiders encounter the edge of the map, they reverse direction
- Each round takes one tick
    - The tick function would check if the player is in the same cell as enemy, and commence a round
- Player exits portal on the same tile as the portal
- Assume bombs explode on the same tick that they are triggered

## Milestone 3 Task 1
- The sunstone can be used as a key or used as treasure to craft items and be retained but will be removed when given as a bribe.
- A sceptre when used has a 10 tick duration which turns all mercenaries and assassins into allies. Once the 10 ticks are completed, it will be removed.
- The Anduril will be dropped by battle's similar to the One Ring as it is also a rare collectable entity
- The sceptre cannot be crafted by a single sunstone taking the place of the key/treasure and itself as the materials. This will only be possible with two sun stones where one replaces the key/treasure and the other being itself.
- Midnight armour will have a durability which will function in the same way as regular armour
- Sceptre will have a durability in terms of number of total times the sceptre can be used.
- Once a mob drops an item when killed, it will have a 50/50 chance of dropping a one ring or anduril

