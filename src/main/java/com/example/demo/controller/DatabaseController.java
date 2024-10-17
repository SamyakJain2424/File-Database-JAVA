package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.service.SQLParserService;

@RestController
@RequestMapping("/api")
public class DatabaseController {

    @Autowired
    private SQLParserService sqlParserService;

    @PostMapping("/execute")
    public ResponseEntity<String> executeSQL(@RequestBody String sql) {
        try {
            System.out.println("-------------Hello---------------");
            sqlParserService.parseAndExecute(sql);
            return ResponseEntity.ok("Database operation has been performed successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
