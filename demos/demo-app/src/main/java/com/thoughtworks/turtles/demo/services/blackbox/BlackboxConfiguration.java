package com.thoughtworks.turtles.demo.services.blackbox;

import com.thoughtworks.turtles.demo.services.PropertiesFileSecretService;
import com.thoughtworks.turtles.demo.services.PropertiesFileSecretServiceConfiguration;
import com.thoughtworks.turtles.demo.services.SecretCleaner;
import com.thoughtworks.turtles.demo.services.SecretService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

import java.io.File;

@Configuration
@Slf4j
@Conditional(BlackboxConfiguration.BlackboxEnabledCondition.class)
public class BlackboxConfiguration implements PropertiesFileSecretServiceConfiguration {

    @Value("${propertiesFileSecretService.credentialsFilePath}")
    private String credentialsFilePath;

    @Override
    public String getCredentialsFilePath() {
        return credentialsFilePath;
    }

    public static class BlackboxEnabledCondition implements Condition {

        @Override
        public boolean matches(final ConditionContext context, final AnnotatedTypeMetadata metadata) {
            return "blackbox".equals(context.getEnvironment().getProperty("secret.service"));
        }
    }

    @Bean
    public SecretCleaner getSecretCleaner() {
        return new SecretCleaner(){
            @Override
            public void clean() {
                if (!new File(getCredentialsFilePath()).delete()) {
                    throw new RuntimeException("Failed to delete credentials file.");
                }

                if (!new File(getCredentialsFilePath() + ".gpg").delete()) {
                    throw new RuntimeException("Failed to delete encrypted credentials file.");
                }

            }
        };
    }

    @Bean
    @Autowired
    public SecretService secretService(SecretCleaner secretCleaner) {
        return new PropertiesFileSecretService(this, secretCleaner);
    }

}
