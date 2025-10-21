package uk.ac.plymouth.danielkern.comp2000.data;

public class MenuItem {
    private final String name;
    private final String description;

    public MenuItem(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
