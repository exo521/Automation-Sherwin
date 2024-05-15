package org.selenium.utilities;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;

public class JsonHandler {

    private DocumentContext jsonContext;

    private final Configuration config = Configuration.
            defaultConfiguration().
            addOptions(Option.DEFAULT_PATH_LEAF_TO_NULL);

    public JsonHandler(String jsonString) {
        try {
            this.jsonContext = JsonPath.using(config).parse(jsonString);
        } catch (Exception e) {
            throw new RuntimeException("Unable to parse JSON string");
        }
    }


    public Object getJsonValue(String jsonPath) {
        try {
            return jsonContext.read(JsonPath.compile(jsonPath)).toString();
        } catch (Exception e) {
//            ExtentLogs.log("Unable to get value from JSON using JSONPath: " + jsonPath);
//            Log.info(e.getMessage());
            return null;
        }
    }

    public String writeToJson(String jsonPath, Object value) {
        String finalJson = "";
        try {
            finalJson = jsonContext.set(JsonPath.compile(jsonPath), value).jsonString();
        } catch (Exception e) {
            System.out.println("Unable to set value in JSON using JSONPath: " + jsonPath);
            e.printStackTrace();
        }
        return finalJson;
    }
}