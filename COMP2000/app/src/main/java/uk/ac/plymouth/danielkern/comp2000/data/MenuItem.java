package uk.ac.plymouth.danielkern.comp2000.data;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class MenuItem {
    int id; String name; String description; float price; Drawable image; String categoryName;
    public MenuItem(String name, String description, float price, Drawable image, String categoryName) {
        this.id = -1;
        this.name = name;
        this.description = description;
        this.price = price;
        this.image = image;
        this.categoryName = categoryName;
    }

    public MenuItem(int id, String name, String description, float price, BitmapDrawable image, String categoryName) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryName = categoryName;
        this.image = image;
    }

    public String getCategoryName() {
        return this.categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public float getPrice() {
        return this.price;
    }

    public Drawable getImage() {
        return this.image;
    }

    public void setName(String itemName) {
        this.name = itemName;
    }

    public void setDescription(String itemDescription) {
        this.description = itemDescription;
    }
}
