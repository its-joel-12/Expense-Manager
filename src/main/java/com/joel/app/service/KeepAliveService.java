package com.joel.app.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

import java.net.SocketTimeoutException;

@Service
public class KeepAliveService {

    private final RestTemplate restTemplate;

    @Value("${test.api}")
    private String URL;

    public KeepAliveService() {
        // Set timeouts (in milliseconds)
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(5000); // Time to establish connection
        requestFactory.setReadTimeout(5000);    // Time to wait for data
        this.restTemplate = new RestTemplate(requestFactory);
    }

    // Optional: Delay this ping with ApplicationReadyEvent (safer)
    @PostConstruct
    public void init() {
        ping();
    }

    @Scheduled(fixedRate = 300000)
    public void ping() {
        try {
            String response = restTemplate.getForObject(URL, String.class);
            System.out.println("Pinged KeepAlive URL: " + response);
        } catch (org.springframework.web.client.ResourceAccessException e) {
            // Check if the cause was a timeout
            if (e.getCause() instanceof java.net.SocketTimeoutException) {
                System.err.println("Timeout pinging KeepAlive URL (exceeded 5 sec)");
            } else {
                System.err.println("Resource access error pinging KeepAlive URL: " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("General error pinging KeepAlive URL: " + e.getMessage());
        }
    }
}
