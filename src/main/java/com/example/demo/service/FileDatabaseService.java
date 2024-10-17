package com.example.demo.service;

import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Service
public class FileDatabaseService {
    private static final String METADATA_FILE = "metadata.txt";
    private static final String DATA_FILE = "table_data.txt";

    public void createTable(String tableDefinition) throws IOException {
        // Write metadata (column names and types) to the metadata file
        System.out.println("-------------------------------");
        System.out.println(tableDefinition);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(METADATA_FILE))) {

            writer.write(tableDefinition);
        }
    }

    public void insertData(String data) throws IOException {
        // Write data entries to the table data file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE, true))) {
            writer.write(data);
            writer.newLine();
        }
    }

    public boolean tableExists() {
        File file = new File(METADATA_FILE);
        return file.exists();
    }
}
