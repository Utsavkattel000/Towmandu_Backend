package com.tow.mandu.config.firebase;


import com.google.gson.Gson;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "firebase")
public class FirebaseProperties {
    private String type;

    private String projectId;

    private String private_key_id;

    private String private_key;

    private String client_email;

    private String client_id;

    private String auth_uri;

    private String token_uri;

    private String auth_provider_x509_cert_url;

    private String client_x509_cert_url;

    private String universe_domain;

    public InputStream initializeInputStream(FirebaseProperties firebaseProperties) {
        String json = new Gson().toJson(firebaseProperties);
        json = json.replace("\\\\n", "\\n");
        return new ByteArrayInputStream(json.getBytes());
    }
}

