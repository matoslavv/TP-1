package bulkupload.tp1.common;

import bulkupload.tp1.data.Data;
import bulkupload.tp1.data.Postcard;
import bulkupload.tp1.data.Category;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.util.*;

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
//            String[] valuesString = record.toMap().values().toArray(new String[0]);

            // Split the string using commas, while handling escaped quotes around the last element
//            String[] valuesArray = valuesString[0].split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);

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

            postcard.setName(getNonEmptyColumnValue(csvRecord, "filename"));
            postcard.setDescriptionNE(getNonEmptyColumnValue(csvRecord, "description"));
            postcard.setImageURL(getNonEmptyColumnValue(csvRecord, "imageUrl"));
            postcard.setDay(parseInteger(getColumnValue(csvRecord, "day")));
            postcard.setMonth(parseInteger(getColumnValue(csvRecord, "month")));
            postcard.setYear(parseInteger(getColumnValue(csvRecord, "year")));
            postcard.setFlag(parseInteger(getColumnValue(csvRecord, "flag")));
            postcard.setLocation(getNonEmptyColumnValue(csvRecord, "location"));
            postcard.setLanguage(getNonEmptyColumnValue(csvRecord, "language"));
            postcard.setSolved(getNonEmptyColumnValue(csvRecord, "solved"));
            postcard.setAvailability(getColumnValue(csvRecord, "availability"));
            postcard.setSender(getNonEmptyColumnValue(csvRecord, "sender"));
            postcard.setRecipient(getNonEmptyColumnValue(csvRecord, "recipient"));


            String categoryValue = getColumnValue(csvRecord, "category");

            Category postcardCategory = new Category();

            if (categoryValue != null) {
                if (categoryValue.equals("Monoalphabetic") || categoryValue.equals("Homophonic") || categoryValue.equals("Polyalphabetic") || categoryValue.equals("Polygraphic")) {
                    // If the category is one of these, set both name and mainCategory
                    postcardCategory.setName(categoryValue);
                    postcardCategory.setMainCategory("Substitution");
                } else if (categoryValue.equals("Ancient")) {
                    postcardCategory.setName(categoryValue);
                    postcardCategory.setMainCategory("Historical");
                } else {
                    // Otherwise, set mainCategory to NULL and name to the category value
                    postcardCategory.setName(categoryValue);
                    postcardCategory.setMainCategory(null);
                }
            } else {
                // Handle the case where categoryValue is null, e.g., set default values for the category
                postcardCategory.setName(null);
                postcardCategory.setMainCategory(null);
            }

            // Set the category in the postcard object
            postcard.setCategory(postcardCategory);


            // Extract the tags as a single string and split them
            String tagsString = getColumnValue(csvRecord, "tag");
            List<String> tags = new ArrayList<>();

            if (tagsString != null) {
                // Split the tags only if tagsString is not null
                tags = splitTagsBack(tagsString);
            }

            // Set the tags in the postcard object
            postcard.setTags(tags);


            int counter = 0;

            // Check if dataValue1 is not null and add groupName1 to the group list
            String dataValue1 = getColumnValue(csvRecord, "dataValue");
            if (!dataValue1.equals("null")) {
                postcard.addGroupName(getColumnValue(csvRecord, "groupName"));
                String blobb = getColumnValue(csvRecord, "dataValue");
                String description = getColumnValue(csvRecord, "dataName");
                String fileType = getColumnValue(csvRecord, "dataType");
                Data data1 = new Data(blobb, description, fileType, counter);
                counter++;

                postcard.addData(data1);

            }

            // Check if dataValue2 is not null and add groupName2 to the group list
            String dataValue2 = getColumnValue(csvRecord, "dataValue2");
            if (!dataValue2.equals("null")) {
                postcard.addGroupName(getColumnValue(csvRecord, "groupName2"));
                String blobb = getColumnValue(csvRecord, "dataValue2");
                String description = getColumnValue(csvRecord, "dataName2");
                String fileType = getColumnValue(csvRecord, "dataType2");
                Data data2 = new Data(blobb, description, fileType, counter);
//                counter++;

                postcard.addData(data2);
            }

            // Check if dataValue3 is not null and add groupName3 to the group list
            String dataValue3 = getColumnValue(csvRecord, "dataValue3");
            if (!dataValue3.equals("null")) {
//                postcard.addGroupName(getColumnValue(csvRecord, "groupName2"));
                String blobb = getColumnValue(csvRecord, "dataValue3");
                String description = getColumnValue(csvRecord, "dataName3");
                String fileType = getColumnValue(csvRecord, "dataType3");
                Data data3 = new Data(blobb, description, fileType, counter);

                counter++;
                postcard.addData(data3);
            }

            String dataValue4 = getColumnValue(csvRecord, "dataValue4");
            if (!dataValue4.equals("null")) {
                postcard.addGroupName(getColumnValue(csvRecord, "groupName4"));
                String blobb = getColumnValue(csvRecord, "dataValue4");
                String description = getColumnValue(csvRecord, "dataName4");
                String fileType = getColumnValue(csvRecord, "dataType4");
                Data data4 = new Data(blobb, description, fileType, counter);


                postcard.addData(data4);
            }


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

    private List<String> splitTags(String tagsString) {
        String[] tagsArray = tagsString.split("\\s+");
        return List.of(tagsArray);
    }

    private List<String> splitTagsSemi(String tagsString) {
        String[] tagsArray = tagsString.split(";");
        return List.of(tagsArray);
    }

    private List<String> splitTagsBack(String tagsString) {
        String[] tagsArray = tagsString.split("_");
        return List.of(tagsArray);
    }



    private String getNonEmptyColumnValue(CSVRecord csvRecord, String columnName) {
        String value = getColumnValue(csvRecord, columnName);
        return (value != null && !value.isEmpty()) ? value : null;
    }


    private Integer parseInteger(String value) {
        return value.isEmpty() ? null : Integer.parseInt(value);
    }

}
