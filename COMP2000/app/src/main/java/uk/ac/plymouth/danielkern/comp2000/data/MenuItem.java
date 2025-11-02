package uk.ac.plymouth.danielkern.comp2000.data;

import android.graphics.drawable.Drawable;

public class MenuItem {
    private final String name;
    private final String description;
    private final float price;

    private final Drawable image;


    public MenuItem(String name, String description, float price, Drawable image) {
        this.price = price;
        this.name = name;
        this.description = description;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public Drawable getImage() {
        return image;
    }
}
