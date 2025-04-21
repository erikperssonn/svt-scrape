package com.example.Scrape;

public class SvtArticle {
    
    private String title;
    private String subtitle;
    private String breadtext;

    public SvtArticle  (String title, String subtitle, String breadtext){
        this.title = title;
        this.subtitle = subtitle;
        this.breadtext = breadtext;
    }

    @Override
    public String toString(){
        return "title: " + title + "\n" + 
                "subtitle: " + subtitle + "\n" +
                "breadtext: " + breadtext;
    }
}
