package g62755.dev3.util;

public interface Observable {

    /**
     * Adds an observer to the list of observers for this observable object.
     * The observer will be notified when the observable object's state changes.
     * @param observer The observer to be added.
     */
    void addObserver(Observer observer);

    /**
     * Removes an observer from the list of observers for this observable object.
     * The observer will no longer be notified when the observable object's state changes.
     * @param observer The observer to be removed.
     */
    void removeObserver(Observer observer);

    /**
     * Notifies all registered observers that the observable object's state has changed.
     * Each observer will be updated accordingly based on the implementation of the update method in the Observer.
     */
    void notifyObservers();
}
