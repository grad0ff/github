package com.github.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/base_config.properties")
public interface BaseConfig extends Config {

    @Key("baseUrl")
    String baseUrl();

    @Key("baseApiUrl")
    String baseApiUrl();
}
