package com.fonfella.tak.service;

import com.fonfella.tak.model.ManageCardCommand;
import com.fonfella.tak.model.ProcessOptions;
import com.fonfella.tak.util.CreateCapability;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.TimeUnit;

@Component
public class ManageCardBotService extends AbstractService {

    static AppiumDriver<MobileElement> driver;

    @Autowired
    private ProcessExecutor processExecutor;

    String result;
    String getCause;
    String getMessage;
    String url;

    public JSONObject sendManageCard(@Valid ManageCardCommand manageCardCommand) throws InterruptedException, IOException {
        ProcessOptions options = new ProcessOptions();
        JSONObject obj = new JSONObject();
        System.setProperty("ADB_HOME", "/Users/testfactory/Desktop/takServer/platform-tools");


        // Leggi l'eseguibile adb dalle risorse
        InputStream adbInputStream = getClass().getResourceAsStream("/adb");

        // Crea un file temporaneo per l'eseguibile adb
        Path tempAdbPath = Files.createTempFile("adb", "");
        tempAdbPath.toFile().deleteOnExit();

        // Copia l'eseguibile adb dalle risorse nel file temporaneo
        Files.copy(adbInputStream, tempAdbPath, StandardCopyOption.REPLACE_EXISTING);

        // Rendi il file temporaneo eseguibile
        tempAdbPath.toFile().setExecutable(true);

        // Ora puoi accedere a questa variabile di ambiente all'interno del tuo programma Java
        String androidHome = System.getProperty("ANDROID_HOME");
        String adbHome = System.getProperty("ADB_HOME");
      //  System.out.println("Percorso ADB: " + adbHome+"& :"+androidHome);
      //  processExecutor.shellExec(options, "appium");
   //     ProcessBuilder pb = new ProcessBuilder("appium", "--port", "4724");
    //    Process process = pb.start();
        Thread.sleep(4000);
        String commandActivateNFc = " -s " + manageCardCommand.getUdid() + " shell service call nfc 7";
        String commandDisableNFc = " -s " + manageCardCommand.getUdid() + " shell service call nfc 6";

        try {
            openApp(manageCardCommand, commandActivateNFc, commandDisableNFc, tempAdbPath);
 //           process.destroy();
        } catch (Exception e) {
            log.info(e.getCause().toString());
            log.info(e.getMessage());
            e.printStackTrace();
            result = "Error!!!";
            getCause = e.getCause().toString();
            getMessage = e.getMessage();
     //       processExecutor.shellExec(options, "pkill -f appium -- -p 4724");
      //      process.destroy();
        }
        if (result == "Error!!!") {
            obj.put("Causa", getCause);
            obj.put("Messaggio", getMessage);
        } else {
            obj.put("Risultato", result);
        }
 //       processExecutor.shellExec(options, "pkill -f appium -- -p 4724");
  //      process.destroy();
 //       String command = "adb -s " + fingerBotCommand.getUdid() + " shell service call nfc 7";
        return obj;
    }


    public String openApp(ManageCardCommand manageCardCommand, String commandActivateNFc, String commandDisableNFc, Path tempAdbPath) throws IOException, InterruptedException {
        CreateCapability cc = new CreateCapability();
        URL url;
        if (manageCardCommand.getUrl() != null) {
            String AppiumUrl = "http://"+ manageCardCommand.getUrl()+":4724";
            url = new URL(AppiumUrl);
        } else {
            url = new URL("http://127.0.0.1:4724");
        }

      //  driver = new AndroidDriver<>(url, cc.getCapabilities(fingerBotCommand.getDeviceName(),
//                                                            fingerBotCommand.getPlatformVersion(),
//                                                            fingerBotCommand.getBundleId(),
//                                                            fingerBotCommand.getUdid()));

        driver = new AndroidDriver(new URL(url.toString()), cc.getCapabilities(manageCardCommand.getDeviceName(),
                manageCardCommand.getPlatformVersion(),
                manageCardCommand.getUdid()));

        int time = manageCardCommand.getTimeWait();

        //ProcessOptions options = new ProcessOptions();
        Process process = Runtime.getRuntime().exec(tempAdbPath.toString() + commandActivateNFc);

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
       // driver.findElement(By.id("com.release.adaprox.controller2:id/v2_general_device_card_device_name"));
        String circuito = null;
        if (manageCardCommand.getCircuito().equals("mastercard")) {
            circuito = "com.mastercard.mcsa.internal";
            driver.activateApp(circuito);
            log.info("NFC abilitato sul device udid: "+manageCardCommand.getUdid());
            WebDriverWait fingerReady = new WebDriverWait(driver, 5);
            try {
                if (manageCardCommand.getAfterVisa().equals("true")) {
                    driver.findElement(By.xpath("//*[@text='CANCEL']")).click();
                    process = Runtime.getRuntime().exec(tempAdbPath.toString() + commandActivateNFc);
                    driver.activateApp(circuito);
                }
            } catch (Exception e) {

            }
            try {
                WebElement elementoSostituisci = fingerReady.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='Sostituisci' or @text='SOSTITUISCI']")));
                elementoSostituisci.click();
            } catch (Exception ei) {
               // System.out.println("L'elemento 'Sostituisci' non è presente.");
            }
            fingerReady.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//android.widget.TextView[@text='Mastercard']")));
            driver.findElement(By.xpath("//android.widget.TextView[@text='Mastercard']")).click();
            driver.findElement(By.xpath("//android.widget.TextView[@text='MCD01']")).click();
            Thread.sleep(time);

            try {
                driver.terminateApp(circuito);
            } catch (Exception e) {
                e.getMessage();
                Process process2 = Runtime.getRuntime().exec(tempAdbPath.toString() + commandDisableNFc);
                log.info("NFC disabilitato sul device udid: "+manageCardCommand.getUdid());
                driver.terminateApp(circuito);
            }

        } else if (manageCardCommand.getCircuito().equals("visa")) {
            circuito = "com.visa.app.cdet" ;
            driver.activateApp(circuito);
            WebDriverWait fingerReady = new WebDriverWait(driver, 10);
            try {
                WebElement elementoSostituisci = fingerReady.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@text='Sostituisci' or @text='SOSTITUISCI']")));
                elementoSostituisci.click();
            } catch (Exception ei) {
             //   System.out.println("L'elemento 'Sostituisci' non è presente.");
            }
            try {

            // Esegui uno swipe verso il basso
      //      swipeDown((AndroidDriver) driver);
            driver.findElement(By.xpath("//android.widget.TextView[@text='CDET v2.3 - Revision B – Card 01']")).click();
            Thread.sleep(time);
                driver.terminateApp(circuito);
            } catch (Exception e) {
                e.getMessage();
                Runtime.getRuntime().exec(tempAdbPath.toString() + commandDisableNFc);
                driver.close();
                log.info("NFC disabilitato sul device udid: "+manageCardCommand.getUdid());
            }
        }

        Runtime.getRuntime().exec(tempAdbPath.toString() + commandDisableNFc);
        log.info("NFC disabilitato sul device udid: "+manageCardCommand.getUdid());
//verificare inserimento riattivazione nfc con pausa
        log.info("Azione Eseguita");
        result="Azione Eseguita";
        return result;
    }


    // Metodo per eseguire uno swipe verso il basso
    public static void swipeDown(AndroidDriver driver) {
        // Ottieni le dimensioni dello schermo
        Dimension size = driver.manage().window().getSize();

        // Calcola i punti di inizio e fine dello swipe
        int startX = size.width / 2;
        int startY = (int) (size.height * 0.8); // 80% dall'alto dello schermo
        int endY = (int) (size.height * 0.2);   // 20% dall'alto dello schermo

        // Esegui lo swipe utilizzando le coordinate calcolate
        TouchAction<?> touchAction = new TouchAction<>(driver);
        touchAction.press(PointOption.point(startX, startY)).perform();
        touchAction.moveTo(PointOption.point(startX, endY)).release().perform();
    }
}
