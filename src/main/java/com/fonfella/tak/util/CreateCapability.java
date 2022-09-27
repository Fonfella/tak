package com.fonfella.tak.util;

import org.openqa.selenium.remote.DesiredCapabilities;

public class CreateCapability {

    public DesiredCapabilities getCapabilities(String deviceName, String platformVersion, String udid) {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("deviceName", deviceName);
        capabilities.setCapability("platformName", "Android");
        capabilities.setCapability("platformVersion", platformVersion);
        capabilities.setCapability("udid", udid);
        capabilities.setCapability("noReset", "true");
        capabilities.setCapability("fullReset", "false");
        capabilities.setCapability("appPackage", "com.release.adaprox.controller2");
        capabilities.setCapability("appActivity", "com.release.adaprox.controller2.V3UI.V3MainStream.V3MainActivity");
        return capabilities;
    }


}
