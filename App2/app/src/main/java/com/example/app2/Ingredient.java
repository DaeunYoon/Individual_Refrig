package com.example.app2;

public class Ingredient {
    private String name;
    private String unit;
    private String barcode;
    private int amount;

    Ingredient() {
        this.name = "None";
        this.barcode = "None";
        this.unit = "n";
        this.amount = 0;
    }

    Ingredient(String name, String unit, String barcode, int amount){
        this.name = name;
        this.unit = unit;
        this.barcode = barcode;
        this.amount = amount;
    }

    Ingredient(String name, String barcode) {
        this.name = name;
        this.barcode = barcode;
        this.unit = "n";
        this.amount = 0;
    }

    public String getName() {
        return this.name;
    }

    public String getUnit() {
        return this.unit;
    }

    public String getBarcode() {
        return this.barcode;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
