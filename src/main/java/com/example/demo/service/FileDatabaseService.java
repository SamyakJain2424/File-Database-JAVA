package com.example.demo.service;

import com.example.demo.table.Column;

import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class FileDatabaseService {
    private static final String METADATA_FILE = "metadata.txt";
    private static final String DATA_FILE = "table_data.txt";

    public void createTable(String tableName, List<Column> columns) throws IOException {
        // Write metadata to the file in a structured plain text format
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(METADATA_FILE))) {
            writer.write("Table Name: " + tableName);
            writer.newLine();
            for (Column column : columns) {
                writer.write("Column: " + column.getName() + ", Type: " + column.getType());
                writer.newLine();
            }
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
