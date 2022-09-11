package com.github.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/Selenoid.properties")
public interface SelenoidConfig extends Config {

    @Key("serverUrl")
    String getServerUrl();

    @Key("videoPath")
    String getVideoPath();
}
