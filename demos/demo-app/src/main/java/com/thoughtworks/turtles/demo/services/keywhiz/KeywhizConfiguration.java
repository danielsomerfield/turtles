package com.thoughtworks.turtles.demo.services.keywhiz;

import com.thoughtworks.turtles.demo.services.SecretService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Configuration
@Conditional(KeywhizConfiguration.KeywhizEnabledCondition.class)
public class KeywhizConfiguration {

    public static class KeywhizEnabledCondition implements Condition {
        @Override
        public boolean matches(final ConditionContext context, final AnnotatedTypeMetadata metadata) {
            return "keywhiz".equals(context.getEnvironment().getProperty("secret.service"));
        }
    }

    @Bean
    public SecretService secretService() {
        return new KeywhizSecretService(this);
    }


}
