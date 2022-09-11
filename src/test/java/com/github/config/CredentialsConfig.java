package com.github.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/CredentialsConfig.properties")
public interface CredentialsConfig extends Config {

    @Key("login")
    String getLogin();

    @Key("password")
    String getPassword();

    @Key("email")
    String getEmail();

    @Key("apiToken")
    String getApiToken();
}
