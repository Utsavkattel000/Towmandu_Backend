package com.tow.mandu.config.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class FCMInitializer {
    private final FirebaseProperties firebaseProperties;
    @PostConstruct
    public void firebaseInit() throws IOException {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(firebaseProperties.initializeInputStream(firebaseProperties)))
                .setProjectId(firebaseProperties.getProjectId()) //project id must be set explicitly
                .build();
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options);
        }
        boolean isFirebaseActive = true;
    }
}
