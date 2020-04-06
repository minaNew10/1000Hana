package com.example.alfhana.data.model;

import androidx.annotation.StringDef;

import com.google.firebase.database.Exclude;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Map;


public class Meal {
    private String name;

    @Category private String category;
    private String description;
    private int calories;
    private int price;
    private String ImageUri;

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUri() {
        return ImageUri;
    }

    @Exclude
    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Meal() {
    }

    public Meal(String name,@Category String category, String description, int calories, int price) {
        this.name = name;
        this.category = category;
        this.description = description;
        this.calories = calories;
        this.price = price;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }




    @StringDef
    @Retention(RetentionPolicy.SOURCE)
    public @interface Category{
         String VEGETARIAN = "Vegetarian";
         String POULTRY  = "Poultry";
         String MEAT = "Meat";
         String FISH = "Fish";
         String DESSERT = "Dessert";
         String PASTA = "Pasta";
    }

}
