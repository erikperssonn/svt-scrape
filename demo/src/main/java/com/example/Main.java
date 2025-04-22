package com.example;

import com.example.Scrape.SvtScraper;

public class Main {
    public static void main(String[] args) {
        
        System.setProperty("file.encoding", "UTF-8");

        SvtScraper svtScraper = new SvtScraper();
        svtScraper.run();

    }
}

