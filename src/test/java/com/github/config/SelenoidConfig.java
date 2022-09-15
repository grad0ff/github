package com.github.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/selenoid_config.properties")
public interface SelenoidConfig extends Config {

    @Key("serverUrl")
    String serverUrl();

    @Key("videoPath")
    String videoPath();
}
