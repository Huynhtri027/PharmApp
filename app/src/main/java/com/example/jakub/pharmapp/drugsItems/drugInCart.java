package com.example.jakub.pharmapp.drugsItems;
/**
 * Created by xnivel on 09/03/2015.
 */
public class drugInCart {
    public String name;
    public int count;
    public float price;
    public float priceWithReduction;
    public float priceMBCount;

    public drugInCart(String name, int count, float price, float priceWithReduction, float priceMBCount) {
        this.name = name;
        this.count = count;
        this.price = price;
        this.priceWithReduction = priceWithReduction;
        this.priceMBCount = priceMBCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPriceWithReduction() {
        return priceWithReduction;
    }

    public void setPriceWithReduction(float priceWithReduction) {
        this.priceWithReduction = priceWithReduction;
    }

    public float getPriceMBCount() {
        return priceMBCount;
    }

    public void setPriceMBCount(float priceMBCount) {
        this.priceMBCount = priceMBCount;
    }
}
