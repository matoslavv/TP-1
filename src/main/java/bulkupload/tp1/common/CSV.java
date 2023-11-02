package bulkupload.tp1.common;

import bulkupload.tp1.data.Postcard;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSV {
    private final String path;
    private Reader in;
    private CSVParser parser;
    private Map<String, Integer> headerMap;

    private List<Postcard> postcards = new ArrayList<>();

    public List<Postcard> getPostcards() {
        return postcards;
    }


    public CSV(String path) {
        this.path = path;
    }

    public Map<String, Integer> getHeaderMap() {
        return this.parser.getHeaderMap();
    }

    public void close() {
        if (!this.parser.isClosed()) {
            this.closeParser();
        }

        this.closeReader();
    }

    public void open() {
        this.loadReader();
        this.loadParser();
        headerMap = this.parser.getHeaderMap();

        // Print the header map for debugging
        for (Map.Entry<String, Integer> entry : headerMap.entrySet()) {
            System.out.println("Header Column: " + entry.getKey() + " -> Index: " + entry.getValue());
        }
    }

    private void closeParser() {
        try {
            this.parser.close();
        } catch (IOException e) {
            System.out.println("Error closing CSV file: " + e.getMessage());
        }
    }

    private void loadReader() {
        try {
            in = new FileReader(this.path);
        } catch (FileNotFoundException e) {
            System.out.println("Error loading CSV file: " + e.getMessage());
        }
    }

    private void closeReader() {
        try {
            this.in.close();
        } catch (IOException e) {
            System.out.println("Error resetting CSV file: " + e.getMessage());
        }
    }

    private CSVFormat getCSVFormat() {
        return CSVFormat.DEFAULT.withIgnoreSurroundingSpaces().withHeader();
    }

    private void loadParser() {
        try {
            this.parser = this.getCSVFormat().parse(this.in);
        } catch (IOException e) {
            System.out.println("Error loading CSV file: " + e.getMessage());
        }
    }

    /**
     * Check if the column name exists in the CSV file
     * @param columnName Column name to check
     * @return True if the column name exists, false otherwise
     */
    private boolean checkColumnName(String columnName) {
        return this.parser.getHeaderNames().contains(columnName);
    }

    /**
     * Read the CSV file line by line
     */
    public void readAll() {
        this.open();

        for (CSVRecord record : this.parser) {
            String[] values = record.values();

            for (String value : values) {
                System.out.printf("%-10s", value);
            }
            System.out.println();
        }

        this.close();
    }

    /**
     * Read the CSV file line by line including the header
     */
    public void readAllWithHeader() {
        this.open();

        for (String header : this.parser.getHeaderMap().keySet()) {
            System.out.printf("%-10s", header);
        }
        System.out.println();

        for (CSVRecord record : this.parser) {
            String[] values = record.values();

            for (String value : values) {
                System.out.printf("%-10s", value);
            }
            System.out.println();
        }

        this.close();
    }

    /**
     * Read column from the CSV file
     * @param column Column to read
     */
    public void read(String column) {
        this.open();

        if (!this.checkColumnName(column)) {
            System.out.println("Column name does not exist");
            return;
        }

        for (CSVRecord record : this.parser) {
            String value = record.get(column);
            System.out.println(value);
        }

        this.close();
    }

    /**
     * Read column at provided index from the CSV file
     * @param index Index of the column to read
     */
    public void read(Integer index) {
        this.open();

        for (CSVRecord record : this.parser) {
            String[] values = record.values();

            if (index >= values.length) {
                System.out.println("Index out of bounds");
                return;
            }

            String value = values[index];
            System.out.printf("%-10s", value);
            System.out.println();
        }

        this.close();
    }

    /**
     * Extract all data from the CSV file
     */
    //    TODO: add params Category, data groups, tags
    public List<Postcard> loadPostcardsFromCSV(String token) throws IOException {

        this.open();

        for (CSVRecord csvRecord : this.parser) {
            Postcard postcard = new Postcard();
            postcard.setToken(token);
            postcard.setName(getColumnValue(csvRecord, "name"));
            postcard.setDescription(getColumnValue(csvRecord, "description"));
            postcard.setImageURL(getColumnValue(csvRecord, "imageURL"));
            postcard.setDay(Integer.parseInt(getColumnValue(csvRecord, "day")));
            postcard.setMonth(Integer.parseInt(getColumnValue(csvRecord, "month")));
            postcard.setYear(Integer.parseInt(getColumnValue(csvRecord, "year")));
            postcard.setFlag(Integer.parseInt(getColumnValue(csvRecord, "flag")));
            postcard.setLocation(getColumnValue(csvRecord, "location"));
            postcard.setLanguage(getColumnValue(csvRecord, "language"));
            postcard.setSolved(Integer.parseInt(getColumnValue(csvRecord, "solved")));
            postcard.setAvailability(Integer.parseInt(getColumnValue(csvRecord, "availability")));
            postcard.setSender(getColumnValue(csvRecord, "sender"));
            postcard.setRecipient(getColumnValue(csvRecord, "recipient"));
            postcard.setCategory(getColumnValue(csvRecord, "category"));

            // Set the category, tags, groups, and other fields as needed

            postcards.add(postcard);
        }

        this.close();
        return postcards;
    }

    private String getColumnValue(CSVRecord csvRecord, String columnName) {
        if (headerMap.containsKey(columnName)) {
            int columnIndex = headerMap.get(columnName);
            return csvRecord.get(columnIndex);
        } else {
            return null; // Handle missing columns as needed
        }
    }
}
