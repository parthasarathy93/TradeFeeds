package com.sarathy.demo.repository;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
public class TradeRecord implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(nullable = false)
    private String buyer;

    @Column(nullable = false)
    private String seller;


    @Column(nullable = false)
    private String stock;

    @Column(nullable = false)
    private long price;

    @Column(nullable = false)
    private String tradedate;


    public TradeRecord()
    {
        super();
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

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

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((buyer == null) ? 0 : buyer.hashCode());
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final TradeRecord other = (TradeRecord) obj;
        if (buyer == null) {
            if (other.buyer != null)
                return false;
        } else if (!buyer.equals(other.buyer))
            return false;
        return true;
    }


    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("TradeRecord[buyer=").append(buyer).append("]").append("[seller=").append(seller).append("]").append("[stock").append(stock).append("]").append("[price").append(price).append("]").append("[tradedate").append(tradedate).append("]");
        ;
        return builder.toString();
    }

    public String getTradedate() {
        return tradedate;
    }

    public void setTradedate(String tradedate) {
        this.tradedate = tradedate;
    }
}
