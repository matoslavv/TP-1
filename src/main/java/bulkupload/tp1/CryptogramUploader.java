package bulkupload.tp1;

import bulkupload.tp1.data.Postcard;
import com.google.gson.JsonObject;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import java.util.List;

import com.google.gson.Gson;

public class CryptogramUploader {
    private static final String API_URL = "https://www.test.hcportal.eu/api/rest/createCryptogram.php";

    public void uploadPostcards(List<Postcard> postcards) {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(API_URL);

        try {
            Gson gson = new Gson();

            for (Postcard postcard : postcards) {
                // Convert a single Postcard to JSON
//                String json = gson.toJson(postcard);

                // Create a JSON object to hold the "records" property
                JsonObject jsonObject = new JsonObject();
                // Convert the current postcard to a JSON object
                JsonObject postcardJson = gson.toJsonTree(postcard).getAsJsonObject();
                // Add the postcard as a property to the "records" object
                jsonObject.add("records", postcardJson);
                // Convert the JSON object to a JSON string
                String json = jsonObject.toString();

                // Print the JSON to the console
                System.out.println("Sending JSON to API: " + json);

                // Set the JSON as the request body
                StringEntity entity = new StringEntity(json);
                httpPost.setEntity(entity);

                // Set the headers if needed (e.g., content type)
                httpPost.setHeader("Content-Type", "application/json");

                HttpResponse response = httpClient.execute(httpPost);
                String responseJson = EntityUtils.toString(response.getEntity());

                if (response.getStatusLine().getStatusCode() == 200) {
                    // Successful response
                    System.out.println("API Response: " + responseJson);
                } else {
                    // Handle error
                    System.err.println("API Error: " + response.getStatusLine().getStatusCode());
                    System.err.println("API Response: " + responseJson);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
