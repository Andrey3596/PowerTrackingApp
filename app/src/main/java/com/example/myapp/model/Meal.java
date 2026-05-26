package com.example.myapp.model;

import java.time.LocalDate;
import java.util.List;

import com.example.myapp.database.*;

import com.example.myapp.service.*;
public class Meal {
    // список продуктов
    private String typeMeal; //тип приема
    private List<ConsumedProduct> consumedProducts;
    LocalDate date;
    private int id;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Meal(List<ConsumedProduct> consumedProducts, String typeMeal, LocalDate date) {
        this.consumedProducts = consumedProducts;
        this.typeMeal = typeMeal;
        this.date = date;
    }


    public String getTypeMeal() {
        return typeMeal;
    }

    public void setTypeMeal(String typeMeal) {
        this.typeMeal = typeMeal;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }


    public List<ConsumedProduct> getConsumedProducts() {
        return consumedProducts;
    }

    public void setConsumedProducts(List<ConsumedProduct> consumedProducts) {
        this.consumedProducts = consumedProducts;
    }
}
