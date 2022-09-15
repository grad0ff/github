package com.github.spec;

import com.github.config.BaseConfig;
import com.github.config.CredentialsConfig;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.Header;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.aeonbits.owner.ConfigFactory;

import static io.restassured.RestAssured.with;

public class BaseSpec {

    public static CredentialsConfig config = ConfigFactory.create(CredentialsConfig.class);
    public static BaseConfig baseConfig = ConfigFactory.create(BaseConfig.class);
    public static String accept = "application/vnd.github+json";
    public static Header header = new Header("Authorization", "Bearer " + config.apiToken());

    public static RequestSpecification reqSpec = with()
            .baseUri(baseConfig.baseApiUrl())
            .accept(accept)
            .header(header);

    public static ResponseSpecification resSpec = new ResponseSpecBuilder()
            .log(LogDetail.ALL)
            .build();
}
