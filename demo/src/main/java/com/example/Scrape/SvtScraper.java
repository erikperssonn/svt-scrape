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

    boolean debugging = true;

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

        //String chromeDriverVersion = (String) ((HasCapabilities) driver).getCapabilities().getCapability("chrome").toString();
        //System.out.println("ChromeDriver Version: " + chromeDriverVersion);

    }

    public void run(){
        scrapeWebPage(INRIKES_URL);
    }
    
    private void declineCookies(){
        WebElement declineButton = Utils.tryToScrapeXPath("/html/body/div[3]/div/div[3]/button[2]",
                                                    2000L,
                                                    this.driver);
        declineButton.click();
    }

    private void scrapeWebPage (String url){
        this.driver.get(url);
        declineCookies();
        getTitleHrefMap(url);
    }

    private void getTitleHrefMap(String baseUrl){
        List<WebElement> articles = driver.findElements(By.cssSelector("#innehall > div > section > ul > li:nth-child(n) > article > a"));
        for(WebElement article : articles){
            String articleUrl = article.getDomAttribute("href");
            //sortera bort artiklar man redan har, samt länkar till externa hemsidor
            if(!hrefSet.contains(articleUrl) && !articleUrl.contains("https")){
                scrapeArticle(articleUrl);
                this.driver.get(baseUrl);
            }
        }
    }

    private void scrapeArticle(String articleUrl){
        Utils.debugg(articleUrl, "Scrape article", debugging);

        String completeArticleUrl = "https://www.svt.se/" + articleUrl;
        this.driver.get(completeArticleUrl);

        final WebElement titleElement = Utils.tryToScrapeCSS("#innehall > div > article > h1", 2000L, this.driver);
        final String title = titleElement.getText();

        final WebElement subtitlesElement = Utils.tryToScrapeCSS("#innehall > div > article > div.Lead__root___PJ6pA", 2000L, this.driver);
        final String subtitle = subtitlesElement.getText();

        final StringBuilder breadtextBuilder = new StringBuilder();
        final List<WebElement> breadtextElements = driver.findElements(By.cssSelector("#innehall > div > article > div.TextArticle__main___zH7wd > div.Stack__root___W5SPP.TextArticle__body___SZ6MK.Stack__gap-14___zt026.Stack__sm-gap-16___D6LK8 > div.InlineText__root___g8u-1"));

        for(WebElement breadtextPart : breadtextElements){
            breadtextBuilder.append(breadtextPart.getText());
        }

        SvtArticle svtArticle = new SvtArticle(title, subtitle, breadtextBuilder.toString());

        
        Utils.debugg(svtArticle.toString(), debugging);


    }

    /* private String getTitleFromArticle(WebElement article){
        WebElement titleElement = article.findElement(By.cssSelector())
        return "";
    }
 */
    
}
