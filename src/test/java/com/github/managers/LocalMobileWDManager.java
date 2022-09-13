package com.github.managers;

import com.codeborne.selenide.Configuration;
import com.github.config.LocalMobileDriverConfig;
import com.github.drivers.LocalMobileDriver;
import org.aeonbits.owner.ConfigFactory;

public class LocalMobileWDManager extends AbstractWDManager {

    private static final LocalMobileDriverConfig config = ConfigFactory.create(LocalMobileDriverConfig.class);

    public static LocalMobileWDManager create(Boolean isDevice) {
        LocalMobileDriver.config = config;
        LocalMobileDriver.isRealDevice = isDevice;
        return new LocalMobileWDManager();
    }

    @Override
    public void configureAfterEach() {
        super.configureAfterEach();
    }

    @Override
    public void configureBeforeAll() {
        Configuration.browser = LocalMobileDriver.class.getName();
        Configuration.browserSize = null;
    }
}
