package com.example.alfhana.data.model;

import android.graphics.Color;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import androidx.databinding.BindingAdapter;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

@Entity(tableName = "order")
public class Order {
//    public static final String TABLE_NAME = "order";
//    public static final String COLUMN_ID = "OrderId";
    @PrimaryKey(autoGenerate = true)
    public long orderId;
    public String productID;
    public String productName;
    public String category;
    public String quantity;
    public String price;


    public Order(long orderId, String productID, String productName, String category, String quantity, String price) {
        this.orderId = orderId;
        this.productID = productID;
        this.productName = productName;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }
    @Ignore
    public Order(String productID, String productName, String category, String quantity, String price) {
        this.productID = productID;
        this.productName = productName;
        this.category = category;
        this.quantity = quantity;
        this.price = price;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @BindingAdapter({"Quantity"})
    public static void loadImage(ImageView view, String Quantity) {
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(Quantity, Color.RED);
        view.setImageDrawable(drawable);
    }

}
