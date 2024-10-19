package com.example.demo.controller;

import com.example.demo.entity.Column;
import com.example.demo.entity.TableData;
import com.example.demo.service.TableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DatabaseController {


    @Autowired
    private TableService tableService;

    @PostMapping("/create")
    public ResponseEntity<String> createTable(@RequestBody String sql) {
        try {
            System.out.println("-------------Hello---------------");
            System.out.println(sql);
            tableService.createTable(sql);
            return ResponseEntity.ok("Table has been created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insertData(@RequestBody String sql) {
        try {
            System.out.println("-------------Hello---------------");
            System.out.println(sql);
            tableService.insertData(sql);
            return ResponseEntity.ok("Values have been inserted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/getdata")
    public ResponseEntity<List<TableData>>  grtTableData() throws IOException {
        List<TableData> data = tableService.getData();

        return ResponseEntity.ok(data);
    }
}
