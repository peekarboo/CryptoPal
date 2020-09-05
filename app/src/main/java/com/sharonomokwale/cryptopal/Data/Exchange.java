package com.sharonomokwale.cryptopal.Data;

public class Exchange {
    private  String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getTrustscore() {
        return trustscore;
    }

    public void setTrustscore(String trustscore) {
        this.trustscore = trustscore;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    private  String imageUrl;
    private String Url;
    private String trustscore;
    private String rank;
    public Exchange(String name, String imageurl, String url, String trustscore, String rank){
        this.name= name;
        this.imageUrl=imageurl;
        this.Url=url;
        this.trustscore=trustscore;
        this.rank= rank;

    }

}
