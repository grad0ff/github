package com.github.base;

import com.github.managers.AbstractWDManager;
import com.github.managers.BrowserstackMobileWDManager;
import com.github.managers.LocalMobileWDManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

public class MobileTestBase extends TestBase {

    private static final AbstractWDManager manager;

    static {
        String deviceHost = System.getProperty("deviceHost", "real");
        switch (deviceHost) {
            case "browserstack":
                manager = BrowserstackMobileWDManager.create();
                break;
            case "emulator":
                manager = LocalMobileWDManager.create(false);
                break;
            case "real":
                manager = LocalMobileWDManager.create(true);
                break;
            default:
                throw new RuntimeException("DeviceHost is not defined!");
        }
    }

    @BeforeAll
    public static void beforeAll() {
        manager.configureBeforeAll();
    }

    @BeforeEach
    public void beforeEach() {
        manager.configureBeforeEach();
    }

    @AfterEach
    public void afterEach() {
        manager.configureAfterEach();
    }

    @AfterAll
    public static void afterAll() {
        manager.configureAfterAll();
    }

}
