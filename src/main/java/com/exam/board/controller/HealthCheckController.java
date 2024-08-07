package com.exam.board.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@RestController
public class HealthCheckController {

    @Value("${server.env}")
    private String env;

    @Value("${server.port}")
    private String serverPort;

    @Value("${server.serverAddress}")
    private String serverAddress;

    @Value("${serverName}")
    private String serverName;


    @GetMapping("/hc")
    public ResponseEntity<?> healthCheck() {
        Map<String,String> response = new TreeMap<>();
        // nginx가 블루 - 그린 배포를 하는데 이떄 해당 서버가 잘 열려있는지 확인

        response.put("serverName", serverName);
        response.put("serverAddress", serverAddress);
        response.put("serverPort", serverPort);
        response.put("env", env);


        return ResponseEntity.ok(response);
    }

    @GetMapping("/env")
    public ResponseEntity<?> getEnv() {
        return ResponseEntity.ok(env);

    }

}
