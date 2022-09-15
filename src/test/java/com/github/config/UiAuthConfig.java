package com.github.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/ui_auth_config.properties")
public interface UiAuthConfig extends Config {

    @Key("cookie")
    String cookie();
}
