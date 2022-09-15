package com.github.config;

import org.aeonbits.owner.Config;

@Config.Sources("classpath:config/credentials_config.properties")
public interface CredentialsConfig extends Config {

    @Key("login")
    String login();

    @Key("password")
    String password();

    @Key("email")
    String email();

    @Key("apiToken")
    String apiToken();
}
