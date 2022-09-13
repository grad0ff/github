package com.github.spec;

import com.github.config.CredentialsConfig;
import com.github.utils.CustomApiListener;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.aeonbits.owner.ConfigFactory;

public class Spec {

    public static CredentialsConfig config = ConfigFactory.create(CredentialsConfig.class);

    public static RequestSpecification reqSpec = BaseSpec.reqSpec
            .filter(CustomApiListener.withCustomTemplates())
            .log().all();

    public static ResponseSpecification resSpec = BaseSpec.resSpec;
}
