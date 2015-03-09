/**
 * Created by xnivel on 09/03/2015.
 */
public class drugInCart {
    public String name;
    public String count;
    public String price;
    public String priceWithReduction;
    public String priceMBCount;

    public drugInCart(String name, String count, String price, String priceWithReduction, String priceMBCount) {
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceWithReduction() {
        return priceWithReduction;
    }

    public void setPriceWithReduction(String priceWithReduction) {
        this.priceWithReduction = priceWithReduction;
    }

    public String getPriceMBCount() {
        return priceMBCount;
    }

    public void setPriceMBCount(String priceMBCount) {
        this.priceMBCount = priceMBCount;
    }
}
