package g62755.dev3.util;

public interface Observer {

    /**
     * This method is called by the observable object when its state changes.
     * The observer should implement this method to update its state or perform
     * other actions based on the changes in the observable object.
     */
    void update();
}
