package com.company;

public class TradeFeed  implements  java.io.Serializable{

    private String partyName;
    private String tradeType;
    private String tradePrice;
    private String company;


    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getTradeType() {
        return tradeType;
    }

    public void setTradeType(String tradeType) {
        this.tradeType = tradeType;
    }

    public String getTradePrice() {
        return tradePrice;
    }

    public void setTradePrice(String tradePrice) {
        this.tradePrice = tradePrice;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    @Override
    public String toString()
    {
        return getPartyName()+","+getCompany()+","+getTradePrice();

    }
}


