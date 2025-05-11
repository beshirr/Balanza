package com.example.blanza;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SQLLoader {
    private static final Map<String, String> sqlQueries = new HashMap<>();
    private static final Pattern QUERY_PATTERN = Pattern.compile("--\\s*@(\\w+)\\s*([\\s\\S]*?)(?=--\\s*@|$)");
    
    static {
        try {
            InputStream inputStream = SQLLoader.class.getResourceAsStream("/sql/queries.sql");
            
            if (inputStream == null) {
                String errorMsg = "Error: Could not find SQL file in resources at /sql/queries.sql";
                System.err.println(errorMsg);
                throw new RuntimeException(errorMsg); // Make the error explicit and prevent partial initialization
            }
            
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder fileContent = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                fileContent.append(line).append("\n");
            }
            
            Matcher matcher = QUERY_PATTERN.matcher(fileContent.toString());
            while (matcher.find()) {
                String queryName = matcher.group(1);
                String queryContent = matcher.group(2).trim();
                sqlQueries.put(queryName, queryContent);
            }
            
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading SQL queries: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static String get(String queryName) {
        String query = sqlQueries.get(queryName);
        if (query == null) {
            System.err.println("No SQL query found with name: " + queryName);
        }
        return query;
    }
}