package com.thoughtworks.turtles.demo.services.blackbox;

import com.thoughtworks.turtles.demo.services.SecretService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Configuration
@Conditional(BlackboxConfiguration.BlackboxEnabledCondition.class)
public class BlackboxConfiguration {

    @Value("${blackbox.credentialsFilePath}")
    private String credentialsFilePath;

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
    public SecretService secretService() {
        return new BlackboxSecretService(this);
    }

}
