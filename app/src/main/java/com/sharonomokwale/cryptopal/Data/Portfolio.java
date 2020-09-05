package com.sharonomokwale.cryptopal.Data;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity (tableName = "portfolio")
public class Portfolio {
    public Portfolio() {
    }

    public Portfolio(String coinname, String amount, String imageurl, String Quantity) {
        this.coinname = coinname;
        this.amount = amount;
        this.imageurl=imageurl;
        this.quantity=Quantity;
    }

    @PrimaryKey(autoGenerate = true)
    public int pid;



    @ColumnInfo(name = "coinname")
    public String coinname;

    @ColumnInfo(name = "amount")
    public String amount;
    @ColumnInfo(name = "imageurl")
    public String imageurl;


    @ColumnInfo(name = "quantity")
    public String quantity;
    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getCoinname() {
        return coinname;
    }

    public void setCoinname(String coinname) {
        this.coinname = coinname;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
    public String getImageurl() { return imageurl; }

    public void setImageurl(String imageurl) { this.imageurl = imageurl; }

    public String getQuantity() { return quantity; }

    public void setQuantity(String quantity) { this.quantity = quantity; }


}

