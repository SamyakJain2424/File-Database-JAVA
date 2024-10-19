package com.example.demo.controller;

import com.example.demo.entity.TableData;
import com.example.demo.service.RedisCounterService;
import com.example.demo.service.TableService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class DatabaseController {

    Logger logger = LoggerFactory.getLogger(DatabaseController.class);

    @Autowired
    private TableService tableService;

    @Autowired
    private RedisCounterService redisCounterService;

    @PostMapping("/create")
    public ResponseEntity<String> createTable(@RequestBody String sql) {
        logger.info("Received request for creating Table");

        try {
            tableService.createTable(sql);
            redisCounterService.incrementSuccess();

            logger.info("Created Table successfully");
            return ResponseEntity.ok("Table has been created successfully");
        } catch (Exception e) {
            redisCounterService.incrementFailure();
            logger.error("Following error was encountered while creating the table: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/insert")
    public ResponseEntity<String> insertData(@RequestBody String sql) {
        logger.info("Received request for inserting in table");

        try {

            tableService.insertData(sql);
            redisCounterService.incrementSuccess();

            logger.info("Inserted into table successfully");
            return ResponseEntity.ok("Values have been inserted successfully");
        } catch (Exception e) {
            redisCounterService.incrementFailure();
            logger.error("Following error was encountered while inserted into table: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/getdata")
    public ResponseEntity<List<TableData>>  grtTableData() throws IOException {
        logger.info("Received request for getting the data");
        List<TableData> data = tableService.getData();
        logger.info("Data retrieved successfully");
        return ResponseEntity.ok(data);
    }

    @GetMapping("/counters")
    public ResponseEntity<String> getCounters() {
        Long successCount = redisCounterService.getSuccessCount();
        Long failureCount = redisCounterService.getFailureCount();
        return ResponseEntity.ok("Success Count: " + successCount + ", Failure Count: " + failureCount);
    }
}
