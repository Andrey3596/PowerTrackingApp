package com.example.myapp.model;

import com.example.myapp.database.*;


import com.example.myapp.service.*;
public class ConsumedProduct {
    private Product product;
    private double weightGrams;

    public ConsumedProduct(Product product, double weightGrams){
        this.product = product;
        this.weightGrams = weightGrams;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public double getWeightGrams() {
        return weightGrams;
    }

    public void setWeightGrams(double weightGrams) {
        this.weightGrams = weightGrams;
    }

    public double getCalories() {
        return product.getCalory() * weightGrams / 100;
    }
    public double getProtein() {
        return product.getProtein() * weightGrams / 100;
    }
    public double getFat() {
        return product.getFat() * weightGrams / 100;
    }
    public double getCarb() {
        return product.getCarb() * weightGrams / 100;
    }


}
