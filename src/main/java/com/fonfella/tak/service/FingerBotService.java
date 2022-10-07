package com.fonfella.tak.service;

import com.fonfella.tak.model.ExecResult;
import com.fonfella.tak.model.FingerBotCommand;
import com.fonfella.tak.model.ProcessOptions;
import com.fonfella.tak.util.CreateCapability;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

@Component
public class FingerBotService extends AbstractService {

    static AppiumDriver<MobileElement> driver;

    @Autowired
    private ProcessExecutor processExecutor;

    String result;
    String getCause;
    String getMessage;

    public JSONObject sendFingerBot(FingerBotCommand fingerBotCommand) throws InterruptedException {
        ProcessOptions options = new ProcessOptions();
        JSONObject obj = new JSONObject();
    //    processExecutor.shellExec(options, "appium");

        try {
            openApp(fingerBotCommand);
        } catch (Exception e) {
            log.info(e.getCause().toString());
            log.info(e.getMessage());
            e.printStackTrace();
            result = "Error!!!";
            getCause = e.getCause().toString();
            getMessage = e.getMessage();

        }
        if (result == "Error!!!") {
            obj.put("Causa", getCause);
            obj.put("Messaggio", getMessage);
        } else {
            obj.put("Risultato", result);
        }
        processExecutor.shellExec(options, "taskkill /F /IM appium.exe");
        return obj;
    }


    public String openApp(FingerBotCommand fingerBotCommand) throws MalformedURLException, InterruptedException {
        CreateCapability cc = new CreateCapability();
        URL url;
        if (fingerBotCommand.getUrl() != null) {
            String AppiumUrl = "http://"+ fingerBotCommand.getUrl()+":4723/wd/hub";
            url = new URL(AppiumUrl);
        } else {
            url = new URL("http://127.0.0.1:4723/wd/hub");
        }

        driver = new AppiumDriver<>(url, cc.getCapabilities(fingerBotCommand.getDeviceName(),
                                                            fingerBotCommand.getPlatformVersion(),
                                                            fingerBotCommand.getUdid()));

        driver.manage().timeouts().implicitlyWait(6, TimeUnit.SECONDS);
        WebDriverWait fingerReady = new WebDriverWait(driver, 10);
        fingerReady.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text = 'Idling']")));
        driver.findElement(By.id("com.release.adaprox.controller2:id/v2_general_device_card_device_status")).click();
        driver.closeApp();
        log.info("Azione Eseguita");
        result="Azione Eseguita";
        return result;
    }
}
