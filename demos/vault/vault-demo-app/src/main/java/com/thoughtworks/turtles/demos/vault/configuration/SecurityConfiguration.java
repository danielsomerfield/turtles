package com.thoughtworks.turtles.demos.vault.configuration;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.net.InetAddress;
import java.net.NetworkInterface;

@Configuration
public class SecurityConfiguration {

    @Value("${vault.appId}")
    private String appId;

    public String getAppId() {
        return appId;
    }

    public String getUserId() {
        return "vault_demo"; //TODO: this should be replaced with the MAC
    }

    @SneakyThrows
    private static String getMacAddress() {
        byte [] mac = NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mac.length; i++) {
            sb.append(String.format("%02X", mac[i]));
        }
        return sb.toString();
    }

    public String getVaultEndpoint() {
        return "http://localhost:8200";
    }
}
