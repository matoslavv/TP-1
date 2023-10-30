package bulkupload.tp1.common;

import bulkupload.tp1.data.Postcard;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CSV {
    private final String path;
    private Reader in;
    private CSVParser parser;

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
    public List<Postcard> extractAll() {

        List<Postcard> postcards = new ArrayList<>();

        try {
            this.open();

//    TODO: add params Category, data groups, tags
            for (CSVRecord record : this.parser) {
                Postcard postcard = new Postcard();
                postcard.setToken(record.get(0));  // Numeric index 0
                postcard.setName(record.get(1));   // Numeric index 1
                postcard.setDescription(record.get(2));  // Numeric index 2
                postcard.setImageURL(record.get(3));  // Numeric index 3
                postcard.setDay(Integer.parseInt(record.get(4)));  // Numeric index 4
                postcard.setMonth(Integer.parseInt(record.get(5)));  // Numeric index 5
                postcard.setYear(Integer.parseInt(record.get(6)));  // Numeric index 6
                postcard.setFlag(Integer.parseInt(record.get(7)));  // Numeric index 7
                postcard.setLocation(record.get(8));  // Numeric index 8
                postcard.setLanguage(record.get(9));  // Numeric index 9
                postcard.setSolved(Integer.parseInt(record.get(10)));  // Numeric index 10
                postcard.setAvailability(record.get(11));  // Numeric index 11
                postcard.setSender(record.get(12));  // Numeric index 12
                postcard.setRecipient(record.get(13));  // Numeric index 13

                postcards.add(postcard);
            }


            this.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return postcards;
    }
}
