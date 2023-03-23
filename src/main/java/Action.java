import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.networknt.schema.*;
import com.networknt.schema.ValidationMessage;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Set;

public class Action {
    public static final String FILE_CONFIG = "src/main/resources/enviroments.properties";
    public static final String FILE_SCHEMA_JSON = "src/main/resources/schema.json";
    public static final String FILE_SCHEMA_JSON_POST = "src/main/resources/schema_post.json";
    public static final String FILE_INFORMATION = "src/main/resources/information.json";
    public static final String FILE_SCHEMA_XSD = "src/main/resources/schema.xsd";
    public static Properties properties(){
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(FILE_CONFIG);
            properties.load(inputStream);
        } catch (IOException e) {
//            throw new RuntimeException(e);
            System.out.println(e.getMessage());
        }
        return properties;
    }
    public static String getUrl(String key){
        return properties().getProperty(key.concat(".url"));
    }
    public static String getId(String key){
        return properties().getProperty(key.concat(".id"));
    }
    public static HttpResponse<String> callAPIGetMethodJSON(String url){
        try {
            Unirest.setTimeouts(0,0);
            return Unirest.get(url)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .asString();
        } catch (UnirestException e) {
//            System.out.println("Call API fail");
            throw new RuntimeException(e);
        }
    }
    public static String getString(String pathName){
        try {
            return new String(Files.readAllBytes(Paths.get(pathName)));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public static HttpResponse<String> callAPIPostMethodJson(String url){
        try {
            Unirest.setTimeouts(0, 0);
            return Unirest.post(url)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .body(getString(FILE_INFORMATION))
                    .asString();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }
    public static HttpResponse<String> callAPIPutMethodJson(String url){
        try {
            return Unirest.put(url)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .header("Authorization", "Basic YWRtaW46cGFzc3dvcmQxMjM=")
                    .body(getString(FILE_INFORMATION))
                    .asString();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }
    public static void verifyCallApi(HttpResponse<String> response){
        if (response == null){
            System.out.println("Call API fail");
        }else {
            System.out.println("Call API successful");
        }
    }
    public static void verifyEqualsFirstName(String actual, String expected){
        if (actual.equalsIgnoreCase(expected)){
            System.out.println("Pass\n$.firstname: "+actual+" found, "+expected +" expected");
        }else {
            System.out.println("Fail\n$.firstname: "+actual+" found, "+expected +" expected");
        }
    }
    public static String getFirstNameJson(HttpResponse<String> response){
        JSONObject jsonObject = new JSONObject(response.getBody());
        return jsonObject.getString("firstname");
    }

    public static String getFirstNameProperties(String env){
        Properties properties = new Properties();
        try {
            InputStream inputStream = new FileInputStream(FILE_CONFIG);
            properties.load(inputStream);
            return properties.getProperty(env.concat(".firstname"));
        } catch (IOException e) {
//            throw new RuntimeException(e);
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static void validateSchema(String jsonText){
        try {
            FileInputStream schemaJson = new FileInputStream(FILE_SCHEMA_JSON);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
            JsonNode jsonbase = objectMapper.readTree(jsonText);
            JsonSchema schema = schemaFactory.getSchema(schemaJson);
            Set<ValidationMessage> validationResult = schema.validate(jsonbase);
            System.out.println("========Schema Validate==========");
            // print validation errors
            if (validationResult.isEmpty()) {
                System.out.println("no validation errors :-)");
            } else {
                validationResult.forEach(vm -> System.out.println(vm.getMessage()));
            }
        }catch (JsonProcessingException j){
            j.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static void validateSchemaPost(String jsonText){
        try {
            FileInputStream schemaJson = new FileInputStream(FILE_SCHEMA_JSON_POST);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonSchemaFactory schemaFactory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V6);
            JsonNode jsonbase = objectMapper.readTree(jsonText);
            JsonSchema schema = schemaFactory.getSchema(schemaJson);
            Set<ValidationMessage> validationResult = schema.validate(jsonbase);
            System.out.println("========Schema Validate==========");
            // print validation errors
            if (validationResult.isEmpty()) {
                System.out.println("no validation errors :-)");
            } else {
                validationResult.forEach(vm -> System.out.println(vm.getMessage()));
            }
        }catch (JsonProcessingException j){
            j.printStackTrace();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    public static HttpResponse<String> callAPIGetMethodXML(String url) {
        try {
            Unirest.setTimeouts(0, 0);
            return Unirest.get(url)
                    .header("Accept", "application/xml")
                    .asString();
        } catch (UnirestException e) {
            throw new RuntimeException(e);
        }
    }
    public static void validateSchemaXML(String xmlText){
        try {
            FileInputStream file = new FileInputStream(FILE_SCHEMA_XSD);
            Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(xmlText)));
            DOMSource xmlFile = new DOMSource (doc);
            SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Source schemeFile = new StreamSource(file);
            Schema schema = schemaFactory.newSchema(schemeFile);
            Validator validator =schema.newValidator();
            validator.validate(xmlFile);

        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.out.println(e.getMessage());
        }

    }
}
