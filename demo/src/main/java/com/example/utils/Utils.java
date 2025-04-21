package com.example.utils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

public interface Utils {

   static WebElement tryToScrapeCSS(String path, long millis, WebDriver driver){
        long t0 = System.currentTimeMillis();
        try{
            if(millis <= 0){
                throw new TimeoutException();
            }
            WebElement element = driver.findElement(By.cssSelector(path));
            return element;
        } catch(NoSuchElementException e){
            long t = System.currentTimeMillis() - t0;
            return tryToScrapeCSS(path, millis - t, driver);
        }
        catch (TimeoutException e){
            System.err.println("ERROR, PATH: " + path);
            return null;
        }
    }


    static WebElement tryToScrapeXPath(String path, long millis, WebDriver driver){
        long t0 = System.currentTimeMillis();
        try{
            if(millis <= 0){
                throw new TimeoutException();
            }
            WebElement element = driver.findElement(By.xpath(path));
            return element;
        } catch(NoSuchElementException e){
            long t = System.currentTimeMillis() - t0;
            return tryToScrapeXPath(path, millis - t, driver);
        }
        catch (TimeoutException e){
            System.err.println("ERROR, PATH: " + path);
            return null;
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

    static void debugg(String content, boolean debugg){
        if(debugg){
            System.out.println("DEBUGGING - " + content);
        }
    }

    static void debugg(String content, String subcontent, boolean debugg){
        if(debugg){
            System.out.println("DEBUGGING - " + content + " " + subcontent);
        }
    }


}
