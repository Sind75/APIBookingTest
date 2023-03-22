import com.mashape.unirest.http.HttpResponse;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CallApiPutJsonTest {
    public HttpResponse<String> response;
    String booking = "/booking/";
    @Parameters({"env"})
    @Test(groups = {("Devint"),("Prod"),("PreProd")})
    public void callAPIJsonPutMethod(String env){
        response = Action.callAPIPutMethodJson(Action.getUrl(env).concat(booking).concat(Action.getId(env)));
        Action.verifyCallApi(response);
    }
    @Test(groups = {("Devint")})
    public void validateSchemaPut(){
        Action.validateSchema(response.getBody());
    }
    @Parameters({"env"})
    @Test(groups = {("Prod"),("PreProd")})
    public void validateFirstName(String env){
        Action.verifyEqualsFirstName(Action.getFirstNameJson(response),Action.getFirstNameProperties(env));
    }
}
