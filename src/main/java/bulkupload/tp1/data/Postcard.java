package bulkupload.tp1.data;

import org.apache.commons.csv.CSVRecord;

import java.util.ArrayList;
import java.util.List;

public class Postcard {
    private String token;
    private String name;
    private String description;
    private String imageURL;
    private Integer day;
    private Integer month;
    private Integer year;
    private Integer flag;
    private String location;
    private String language;
    private String solved;
    private String availability;
    private String sender;
    private String recipient;
    private Category category;
    private List<String> tags;
    private List<String> groups;
    private List<Data> data;


    // Constructor, getters, and setters as needed

    public Postcard() {
        this.data = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.category = new Category(); // Initialize "category" as an empty Category object
//        this.groups = new ArrayList<>();
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
    public List<Data> getData() {
        return data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDescriptionNE(String description) {
        if (description != null && !description.isEmpty() && !description.equals("null")) {
            this.description = "<p>" + description + "</p>";
        } else {
            this.description = "";
        }
    }


    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public Integer getDay() {
        return day;
    }

    public void setDay(Integer day) {
        this.day = day;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getSolved() {
        return solved;
    }

    public void setSolved(String solved) {
        this.solved = solved;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    // Getter and setter for 'tags'
    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    // Getter and setter for 'groups'
    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    // Method to add a group name to the list
    public void addGroupName(String groupName) {
        if (groupName != null && !groupName.isEmpty()) {
            this.groups.add(groupName);
        }
    }

    public void addData(Data data) {
        this.data.add(data);
    }

    // Getter for the groupNames list
    public List<String> getGroupNames() {
        return this.groups;
    }

    public void updatePath(String path, String thumbnail) {
        // Ensure the path ends with "/"
        path = ensureTrailingSlash(path);

        // Ensure other components end with "/"
//        this.imageURL = ensureTrailingSlash(this.imageURL);
//        this.name = ensureTrailingSlash(this.name);

        // Update the imageURL by concatenating the components
        this.imageURL = path + this.name + thumbnail + this.imageURL+".jpg";

        for (Data data : data) {
            // Check if the filetype is "Image"
            if ("image".equalsIgnoreCase(data.getFiletype())) {
                // Update the imageURL with the specified format
                data.setBlobb(path + this.name + data.getBlobb()+".jpg");
//                break; // Assuming you want to update imageURL only once
            }
        }
    }

    // Helper method to ensure a string ends with "/"
    private String ensureTrailingSlash(String str) {
        if (str != null && !str.endsWith("/")) {
            // If not null and doesn't end with "/", add "/"
            str += "/";
        }
        return str;
    }


}
