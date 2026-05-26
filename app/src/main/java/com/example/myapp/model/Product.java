package com.example.myapp.model;


import com.example.myapp.database.*;


import com.example.myapp.service.*;

public class Product {
    private String nameProduct; // имя продукта
    private double calory; //каллории
    private double protein; // белки
    private double fat; // жиры
    private double carb; // углеводы
    private int id;

    //при создании вручную
    public Product(String nameProduct,double calory,double protein,double fat,double carb){
        this.nameProduct = nameProduct;
        this.calory = calory;
        this.protein = protein;
        this.fat = fat;
        this.carb = carb;
        this.id = -1; // временный пока не сохраним в БД
    }

    // Конструктор для загрузки из БД (с id)
    public Product(int id, String nameProduct, double calory, double protein, double fat, double carb) {
        this.id = id;
        this.nameProduct = nameProduct;
        this.calory = calory;
        this.protein = protein;
        this.fat = fat;
        this.carb = carb;
    }



    public String getNameProduct() {
        return nameProduct;
    }

    public void setNameProduct(String nameProduct) {
        this.nameProduct = nameProduct;
    }



    public double getCalory() {
        return calory;
    }

    public void setCalory(double calory) {
        this.calory = calory;
    }

    public double getProtein() {
        return protein;
    }

    public void setProtein(double protein) {
        this.protein = protein;
    }

    public double getFat() {
        return fat;
    }

    public void setFat(double fat) {
        this.fat = fat;
    }

    public double getCarb() {
        return carb;
    }

    public void setCarb(double carb) {
        this.carb = carb;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return StringFormatter.formatProductForSpinner(nameProduct, calory, protein, fat, carb);
    }
}
