package com.example.myapp.service;

import com.example.myapp.database.*;

import com.example.myapp.model.*;


public class CalorieCalculator {
    private double BMR;
    private double aimProtein; // белки
    private double aimFat; // жиры
    private double aimCarb;


    public CalorieCalculator(User user){

        setArg(user);
    }

    public void setArg(User user) {
        if (user.getGender().equals("м")) {
            this.BMR = ((10 * user.getWeight()) + (6.25 * user.getHeight()) - (5 * user.getAge()) + 5) * user.getActive();
        } else {
            this.BMR = ((10 * user.getWeight()) + (6.25 * user.getHeight()) - (5 * user.getAge()) - 161) * user.getActive();
        }
        this.aimProtein = this.BMR * 0.3 / 4;
        this.aimFat = this.BMR * 0.3 / 9;
        this.aimCarb = this.BMR * 0.4 / 4;
    }




    public double getBMR() {
        return BMR;
    }
    public void setBMR(double BMR) {
        this.BMR = BMR;
    }
    public double getAimProtein() {
        return aimProtein;
    }
    public void setAimProtein(double aimProtein) {
        this.aimProtein = aimProtein;
    }
    public double getAimFat() {
        return aimFat;
    }
    public void setAimFat(double aimFat) {
        this.aimFat = aimFat;
    }
    public double getAimCarb() {
        return aimCarb;
    }
    public void setAimCarb(double aimCarb) {
        this.aimCarb = aimCarb;
    }
}
