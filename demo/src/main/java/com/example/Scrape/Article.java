package com.example.Scrape;

import com.example.utils.ArticleType;
import com.example.utils.Country;

public class Article {
    
    private String title;
    private String subtitle;
    private String breadtext;
    private String url;
    private Country country;
    private ArticleType articleType;

    public Article  (String title, String subtitle, String breadtext, String url, Country country, ArticleType articleType){
        this.title = title;
        this.subtitle = subtitle;
        this.breadtext = breadtext;
        this.url = url;
        this.country = country;
        this.articleType = articleType;
    }

    @Override
    public String toString(){
        return "title: " + this.title + "\n" + 
                "subtitle: " + this.subtitle + "\n" +
                "breadtext: " + this.breadtext + "\n" +
                "url: " + this.url + "\n" +
                "country: " + this.country.toString() + "\n" + 
                "articletype: "  + articleType.toString();

    }
}
