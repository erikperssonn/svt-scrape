package com.example.Scrape;

import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.*;


import com.example.utils.Variables;
import com.example.utils.Utils;

public class SvtScraper {

    private final WebDriver driver;
    
    private final String UTRIKES_URL = "https://www.svt.se/nyheter/utrikes/";
    private final String INRIKES_URL = "https://www.svt.se/nyheter/inrikes/";

    private final HashMap<String, String> titleHrefMap = new HashMap<>();

    private final Set<String> hrefSet = new HashSet<>();

    public SvtScraper  (){
        System.setProperty("webdriver.chrome.driver", Variables.CHROME_DRIVER_PATH);

        ChromeOptions options = new ChromeOptions();
        options.setPageLoadStrategy(PageLoadStrategy.NORMAL);
        //options.addArguments("--headless");

        this.driver = new ChromeDriver(options);

    }

    public void run(){
        declineCookies();
        scrapeWebPage(INRIKES_URL);
    }

    private void declineCookies(){
        WebElement declineButton = Utils.tryToScrape("body > div.CookieConsent__root___G7vry > div > div.CookieConsent__footer___o4D6 > button:nth-child(2)",
                                                    2000L,
                                                    this.driver);
        declineButton.click();
    }

    private void scrapeWebPage (String url){
        this.driver.get(url);
        getTitleHrefMap();
    }

    private void getTitleHrefMap(){
        List<WebElement> articles = driver.findElements(By.cssSelector("#innehall > div > section > ul > li:nth-child(n) > article > a"));
        for(WebElement article : articles){
            String articleUrl = article.getAttribute("href");
            if(!hrefSet.contains(articleUrl)){
                scrapeArticle(articleUrl);
            }
        }
    }

    private void scrapeArticle(String articleUrl){
        this.driver.get(articleUrl);

        final WebElement titleElement = driver.findElement(By.cssSelector("#innehall > div > article > h1"));
        final String title = titleElement.getText();

        final WebElement subtitlesElement = driver.findElement(By.cssSelector("#innehall > div > article > div.Lead__root___PJ6pA"));
        final String subtitle = subtitlesElement.getText();

        final StringBuilder breadtextBuilder = new StringBuilder();
        final List<WebElement> breadtextElements = driver.findElements(By.cssSelector("#innehall > div > article > div.TextArticle__main___zH7wd > div.Stack__root___W5SPP.TextArticle__body___SZ6MK.Stack__gap-14___zt026.Stack__sm-gap-16___D6LK8 > div.InlineText_root_g8u-1"));

        for(WebElement breadtextPart : breadtextElements){
            breadtextBuilder.append(breadtextPart.getText());
        }

        SvtArticle svtArticle = new SvtArticle(title, subtitle, breadtextBuilder.toString());

        svtArticle.toString();


    }

    /* private String getTitleFromArticle(WebElement article){
        WebElement titleElement = article.findElement(By.cssSelector())
        return "";
    }
 */
    
}
