package uk.ac.plymouth.danielkern.comp2000.data;

public interface DBObserver {

    enum Operation {
        INSERT_RESERVATION,
        UPDATE_RESERVATION,
        DELETE_RESERVATION,
        INSERT_MENU_ITEM,
        UPDATE_MENU_ITEM,
        DELETE_MENU_ITEM,
    }
    void onDatabaseChanged(String dbName, Operation operation);
}
