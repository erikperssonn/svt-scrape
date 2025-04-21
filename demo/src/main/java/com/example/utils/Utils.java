package com.example.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

public interface Utils {

   static WebElement tryToScrape(String path, long millis, WebDriver driver){
        long t0 = System.currentTimeMillis();
        try{
            if(millis <= 0){
                throw new TimeoutException();
            }
            WebElement element = driver.findElement(By.cssSelector(path));
            return element;
        } catch(NoSuchElementException e){
            long t = System.currentTimeMillis() - t0;
            return tryToScrape(path, millis - t, driver);
        }
    }
    static void easySleep(int millisUpper, int millisLower){
    
        int millis = (int) (Math.random() * (millisUpper - millisLower) + millisLower);
        try{
            Thread.sleep(millis);
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
