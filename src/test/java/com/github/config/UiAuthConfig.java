package com.github.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/UiAuthConfig.properties")
public interface UiAuthConfig extends Config {

    @Key("cookie")
    String getCookie();
}
