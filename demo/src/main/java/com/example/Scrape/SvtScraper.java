package com.example.Scrape;

import java.util.Set;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import java.text.Normalizer;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import org.openqa.selenium.*;


import com.example.utils.Variables;
import com.example.utils.ArticleType;
import com.example.utils.Country;
import com.example.utils.Utils;

public class SvtScraper {

    boolean debugging = true;

    private final WebDriver driver;
    private final JavascriptExecutor jsEx;

    private final Country country = Country.SWEDEN;
    
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
        this.jsEx = (JavascriptExecutor) driver;

        //String chromeDriverVersion = (String) ((HasCapabilities) driver).getCapabilities().getCapability("chrome").toString();
        //System.out.println("ChromeDriver Version: " + chromeDriverVersion);

    }

    public void run(){
        //För att först decline cookies, gör inget på bassidan
        this.driver.get("https://www.svt.se/nyheter/");
        declineCookies();
        scrapeWebPage(INRIKES_URL, ArticleType.INRIKES);
        scrapeWebPage(UTRIKES_URL, ArticleType.UTRIKES);
    }
    
    private void declineCookies(){
        WebElement declineButton = Utils.tryToScrapeXPath("/html/body/div[3]/div/div[3]/button[2]",
                                                    2000L,
                                                    this.driver);
        declineButton.click();
    }

    private void scrapeWebPage(String url, ArticleType articleType){
        this.driver.get(url);
        List<WebElement> articles = driver.findElements(By.cssSelector("#innehall > div > section > ul > li:nth-child(n) > article > a"));
        List<String> articleUrls = new ArrayList<>();

        //Detta funkar, kan tyckas ful lösning, har med href att göra
        for(WebElement article : articles){
            String articleHref = article.getDomAttribute("href");
            Utils.debugg("COMPLETEURL", articleHref, debugging);
            articleUrls.add(articleHref);
        }
        
        for(String completeUrl : articleUrls){
            //sortera bort artiklar man redan har
            if(!hrefSet.contains(completeUrl)){
                scrapeArticle(completeUrl, articleType);
            }
        }
    }

    private void scrapeArticle(String articleUrl, ArticleType articleType){
        Utils.debugg(articleUrl, "Scrape article", debugging);

        String completeArticleUrl = "https://www.svt.se/" + articleUrl;
        this.driver.get(completeArticleUrl);

        final WebElement titleElement = Utils.tryToScrapeCSS("#innehall > div > article > h1", 2000L, this.driver);
        final String title = getUTF8Text(titleElement);

        final WebElement subtitlesElement = Utils.tryToScrapeCSS("#innehall > div > article > div.Lead__root___PJ6pA", 2000L, this.driver);
        
        //Ibland finns ingen subtitle
        final String subtitle;
        if(subtitlesElement != null){
            subtitle = getUTF8Text(subtitlesElement);
        } else{
            subtitle = "";
        }
        

        final StringBuilder breadtextBuilder = new StringBuilder();
        final List<WebElement> breadtextElements = driver.findElements(By.cssSelector("#innehall > div > article > div.TextArticle__main___zH7wd > div.Stack__root___W5SPP.TextArticle__body___SZ6MK.Stack__gap-14___zt026.Stack__sm-gap-16___D6LK8 > div.InlineText__root___g8u-1"));

        for(WebElement breadtextPart : breadtextElements){
            breadtextBuilder.append(getUTF8Text(breadtextPart));
        }

        Article svtArticle = new Article(title, subtitle, breadtextBuilder.toString(), completeArticleUrl, this.country, articleType);

        
        Utils.debugg(svtArticle.toString(), debugging);


    }

    //Blir knas med äåö osv, detta löser det
    private String getUTF8Text(WebElement element){
        String text = (String) jsEx.executeScript("return arguments[0].textContent;", element);
        return Normalizer.normalize(text, Normalizer.Form.NFKC);
    }

    
}
