package dungeonmania.entities.moving;

public interface SubjectPlayer {
    /**
     * Adds an observer to the subject
     * @param ObserverMob
     */
    public void addObservers(ObserverMob o);
    /**
     * Removes an observer from subject
     * @param ObserverMob
     */
    public void removeObservers(ObserverMob o);
    /**
     * Notifies the observer that the player has moved
     */
    public void notifyObservers();
    /**
     * Gets the player object
     */
    public Player getSubjectPlayer();
}