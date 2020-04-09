package com.example.alfhana.data.model;

import android.widget.ImageView;

import androidx.annotation.StringDef;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.Exclude;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


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

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public void setPrice(int price) {
        this.price = price;
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


    @BindingAdapter({"imageUrl"})
    public static void loadImage(ImageView view, String imageUrl) {
        Glide.with(view.getContext())
                .load(imageUrl).apply(new RequestOptions())
                .into(view);
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
