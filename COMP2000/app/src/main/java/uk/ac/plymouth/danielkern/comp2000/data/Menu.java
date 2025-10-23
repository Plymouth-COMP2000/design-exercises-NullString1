package uk.ac.plymouth.danielkern.comp2000.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

public class Menu {
    private HashMap<String, ArrayList<MenuItem>> menu;
    public Menu() {
        menu = new HashMap<>();
    }

    public void addCategory(String category) {
        menu.put(category, new ArrayList<>());
    }

    public void addMenuItem(String category, MenuItem item) {
        if (menu.containsKey(category)) {
            menu.get(category).add(item);
        }
    }

    public HashMap<String, ArrayList<MenuItem>> getMenu() {
        return menu;
    }

    public ArrayList<MenuItem> getItemsByCategory(String category) {
        return menu.getOrDefault(category, new ArrayList<>());
    }

    public void addCategoryWithItems(String category, ArrayList<MenuItem> items) {
        menu.put(category, items);
    }

    public void addItemsToCategory(String category, ArrayList<MenuItem> items) {
        if (menu.containsKey(category)) {
            menu.get(category).addAll(items);
        }
    }

    public boolean isEmpty() {
        return menu.isEmpty();
    }

    public Set<String> getCategories() {
        return menu.keySet();
    }
}
