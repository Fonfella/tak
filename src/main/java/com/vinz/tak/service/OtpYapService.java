package com.vinz.tak.service;

import com.vinz.tak.controller.OtpYapFinder;
import org.json.simple.JSONObject;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.springframework.stereotype.Component;


@Component
public class OtpYapService<i> extends AbstractService {

    String p1 = "webdriver.chrome.driver";
    String p2 = "C:\\Users\\donzella.TXTGROUP\\IdeaProjects\\tak\\src\\main\\resources\\chromedriver.exe";
    String startUrl = "https://stgapi.nexi.it/mfa/getlastotp?user=%2B393486896752";

    public JSONObject sendOtpYapFinder(OtpYapFinder otpYapFinder) {
        JSONObject obj = new JSONObject();
        //importante cambiare il path del driver di chrome
        System.setProperty(p1, p2);
        ChromeOptions co = new ChromeOptions();
        co.addArguments("--no-sandbox");
        co.addArguments("--headless");
        co.addArguments("disable-gpu");
        co.addArguments("window-size=1400,2100");
        WebDriver driver = new ChromeDriver(co);


        driver.get(startUrl);
        String el = ((ChromeDriver) driver)
                .findElementByXPath("//*['/html/body/text()']")
                .getText();
        String[] result = el.split("39");
        log.info(result[1]);

        driver.close();
        obj.put("infoNumber", result[1]);
        return obj;
    }


}








