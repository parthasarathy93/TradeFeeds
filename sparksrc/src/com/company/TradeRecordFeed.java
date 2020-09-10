package com.company;

public class TradeRecordFeed  implements  java.io.Serializable {

    private String buyer;
    private String seller;
    private String stock;
    private Float price;
    private String tradedate;


    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getTradedate() {
        return tradedate;
    }

    public void setTradedate(String tradedate) {
        this.tradedate = tradedate;
    }


}
