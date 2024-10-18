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

    @PostMapping("/create")
    public ResponseEntity<String> executeSQL(@RequestBody String sql) {
        try {
            System.out.println("-------------Hello---------------");
            System.out.println(sql);
            sqlParserService.parseAndExecuteTable(sql);
            return ResponseEntity.ok("Table has created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<String> executeSQL2(@RequestBody String sql) {
        try {
            System.out.println("-------------Hello---------------");
            System.out.println(sql);
            sqlParserService.parseAndExecuteData(sql);
            return ResponseEntity.ok("Values has been inserted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }
}
