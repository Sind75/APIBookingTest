import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;


public class CallApiGetJsonTest {
    public HttpResponse<String> response;
    String booking = "/booking/";
    @Parameters({"env"})
    @Test(groups = {("Devint"),("PreProd"),("Prod")})
    public void callAPIJsonGetMethod(String env)  {
//        response = Action.callAPIGet(env+id);
        response = Action.callAPIGetMethodJSON(Action.getUrl(env).concat(booking).concat(Action.getId(env)));
        Action.verifyCallApi(response);
    }
    @Parameters({"env"})
    @Test(groups = {("PreProd"),("Prod"),("Devint")})
    public void validateFirstNameJsonGetMethod(String env){
        String firstName = Action.getFirstNameJson(response);
//        System.out.println(firstName);
        Action.verifyEqualsFirstName(firstName,Action.getFirstNameProperties(env));
    }
//    @Parameters({"env"})
    @Test(groups = {("Prod")})
    public void validateSchemaJsonGetMethod(){
        Action.validateSchema(response.getBody());
    }
}
