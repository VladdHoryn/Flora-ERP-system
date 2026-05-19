package org.example.application;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "services")
public class ServicesProperties {

    private ServiceEntry sales = new ServiceEntry();

    private ServiceEntry inventory = new ServiceEntry();

    private ServiceEntry production = new ServiceEntry();

    @Getter
    @Setter
    public static class ServiceEntry {
        private String baseUrl;
    }
}
