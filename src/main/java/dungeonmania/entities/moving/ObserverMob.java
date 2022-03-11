package dungeonmania.entities.moving;

public interface ObserverMob {
    /**
     * Updates the player subject inside the observers
     * @param SubjectPlayer
     */
    public void update(SubjectPlayer o);
}