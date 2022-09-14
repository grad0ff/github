package com.github.base;

import com.github.config.BaseConfig;
import org.aeonbits.owner.ConfigFactory;

public class TestBase {

    protected static BaseConfig baseConfig = ConfigFactory.create(BaseConfig.class);
}
