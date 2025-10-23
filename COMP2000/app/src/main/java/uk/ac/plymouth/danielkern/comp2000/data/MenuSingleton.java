package uk.ac.plymouth.danielkern.comp2000.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;

public class MenuSingleton {
    private final HashMap<String, ArrayList<MenuItem>> menu;

    private static MenuSingleton instance;
    public MenuSingleton() {
        menu = new HashMap<>();
    }

    public static synchronized MenuSingleton getInstance() {
        if (instance == null) {
            instance = new MenuSingleton();
        }
        return instance;
    }
    public void addCategory(String category) {
        menu.put(category, new ArrayList<>());
    }

    public ArrayList<MenuItem> getItemsByCategory(String category) {
        return menu.getOrDefault(category, new ArrayList<>());
    }

    public void addItemsToCategory(String category, ArrayList<MenuItem> items) {
        if (menu.containsKey(category)) {
            Objects.requireNonNull(menu.get(category)).addAll(items);
        }
    }

    public boolean isEmpty() {
        return menu.isEmpty();
    }

    public Set<String> getCategories() {
        return menu.keySet();
    }
}
