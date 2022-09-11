package com.github.base;

import com.github.config.BaseConfig;
import com.github.config.CredentialsConfig;
import org.aeonbits.owner.ConfigFactory;

public class TestBase {

    protected static CredentialsConfig credentialsConfig = ConfigFactory.create(CredentialsConfig.class);
    protected static BaseConfig baseConfig = ConfigFactory.create(BaseConfig.class);
}
