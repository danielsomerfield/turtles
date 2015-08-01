package com.thoughtworks.turtles.demo.services.vault;

import com.thoughtworks.turtles.demo.services.SecretService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.type.AnnotatedTypeMetadata;

@Configuration
@Conditional(VaultConfiguration.VaultEnabledCondition.class)
public class VaultConfiguration {

    @Value("${demo.appId}")
    private String appId;

    public String getAppId() {
        return appId;
    }

    public String getUserId() {
        return "demo"; //TODO: this should be replaced with the MAC
    }

    public String getVaultEndpoint() {
        return "http://localhost:8200";
    }

    public static class VaultEnabledCondition implements Condition {

        @Override
        public boolean matches(final ConditionContext context, final AnnotatedTypeMetadata metadata) {
            return context.getEnvironment().getProperty("secret.service").equals("vault");
        }
    }

    @Bean
    public SecretService secretService(AppIdAuthService appIdAuthService) {
        return new VaultSecretService(appIdAuthService, this);
    }

    /*
    @SneakyThrows
    private static String getMacAddress() { //TODO: this should really be the source of the user id
        byte[] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X", mac[i]));
        }
        return sb.toString();
    }
    */

}
