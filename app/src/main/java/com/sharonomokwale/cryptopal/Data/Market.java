package com.sharonomokwale.cryptopal.Data;

public class Market {
    public String getVolume() {
        return Volume;
    }

    public void setVolume(String volume) {
        Volume = volume;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
    public String getCHANGEPCT24HOUR() {
        return CHANGEPCT24HOUR;
    }

    public String getFullName() {
        return FullName;
    }
    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getMKTCAP() {
        return MKTCAP;
    }

    public void setMKTCAP(String MKTCAP) {
        this.MKTCAP = MKTCAP;
    }

    public void setCHANGEPCT24HOUR(String CHANGEPCT24HOUR) {
        this.CHANGEPCT24HOUR = CHANGEPCT24HOUR;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    private  String Name;
    private  String FullName;
    private String ImageUrl;
    private String Price;
    private String MKTCAP;
    private String CHANGEPCT24HOUR;
    private String Volume;
    private String rating;


    public Market(String name, String fullName, String imgeurl, String price, String mktcap,String changepct24hour, String volume, String Rating) {
        this.Name = name;
        this.FullName= fullName;
        this.ImageUrl = imgeurl;
        this.Price = price;
        this.MKTCAP = mktcap;
        this.CHANGEPCT24HOUR= changepct24hour;
        this.Volume= volume;
        this.rating = Rating;

    }
    public Market(String name) {
        this.Name = name;


    }

   /* public News(String author, String title, String image_link, String logo,String publish_at,String description,String url) {
        this.title = title;
        this.author = author;
        this.image_link = image_link;
        this.logo = logo;
        this.publish_at=publish_at;
        this.description=description;
        this.url=url;
    }*/


}
