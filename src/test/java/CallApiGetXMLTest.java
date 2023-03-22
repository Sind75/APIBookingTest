import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CallApiGetXMLTest {
    HttpResponse<String> response;
    String booking = "/booking/";
    @Parameters({"env"})
    @Test(groups = {("Devint"),("PreProd"),("Prod")})
    public void callAPIGetMethodXML(String env){
        response = Action.callAPIGetMethodXML(Action.getUrl(env).concat(booking).concat(Action.getId(env)));
        Action.verifyCallApi(response);
    }

    @Test(groups = {("Devint"),("Prod")})
    public void validateSchemaGet(){
        Action.validateSchemaXML(response.getBody());
    }

}
