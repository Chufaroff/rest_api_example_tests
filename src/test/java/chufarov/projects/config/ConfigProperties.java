package chufarov.projects.config;

import org.aeonbits.owner.Config;

@Config.Sources({
        "classpath:config/app.properties"
})
public interface ConfigProperties extends Config {

    @Key("userName")
    String getUserName();

    @Key("password")
    String getPassword();

    @Key("browser")
    @DefaultValue("chrome")
    String getBrowser();

    @Key("browser.size")
    @DefaultValue("1920x1080")
    String getBrowserSize();

    @Key("baseUrl")
    String getBaseUrl();

    @Key("remoteUrl")
    String getRemoteUrl();
}
