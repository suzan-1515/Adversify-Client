package com.generic.appbase.domain.dto;

public enum Category {

    BARS("Bars"),
    COFFEE_SHOPS("Coffee Shops"),
    RESTAURANTS("Restaurants"),
    HOTELS("Hotels"),
    MOVIE_THEATERS("Movie Theaters"),
    PHARMACIES("Pharmacies"),
    ELECTRONIC_STORES("Electronic Stores"),
    CLOTHING_STORES("Clothing Stores"),
    BEAUTY_SALON("Beauty Salon"),
    BAKERY("Bakery"),
    SHOPPING_MALL("Shopping Mall"),
    DEPARTMENT_STORE("Department Store");

    private final String value;

    Category(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return getValue();
    }
}
