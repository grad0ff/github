package com.github.drivers;

import com.codeborne.selenide.WebDriverProvider;
import com.github.config.LocalMobileDriverConfig;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.remote.AutomationName;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class LocalMobileDriver implements WebDriverProvider {

    public static LocalMobileDriverConfig config;
    public static Boolean isRealDevice;

    private static URL getAppiumServerUrl() {
        try {
            return new URL(config.getServerUrl());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public WebDriver createDriver(Capabilities capabilities) {
        File app = new File(config.getApkPath());
        String device;
        String deviceOs;
        if (isRealDevice) {
            device = config.getRealDevice();
            deviceOs = config.getRealDeviceOs();
        } else {
            device = config.getEmulatorDevice();
            deviceOs = config.getEmulatorDeviceOs();
        }
        UiAutomator2Options options = new UiAutomator2Options();
        options.merge(capabilities);
        options.set
        options.setDeviceName(device);
        options.setPlatformVersion(deviceOs);
        options.setAutomationName(AutomationName.ANDROID_UIAUTOMATOR2);
        options.setPlatformName("Android");
        options.setApp(app.getAbsolutePath());
        options.setAppPackage(config.getAppPackage());
        options.setAppActivity(config.getAppActivity());
        return new AndroidDriver(getAppiumServerUrl(), options);
    }
}