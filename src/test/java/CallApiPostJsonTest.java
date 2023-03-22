import com.mashape.unirest.http.HttpResponse;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class CallApiPostJsonTest {
    public HttpResponse<String> response;
    String booking = "/booking/";
    @Parameters({"env"})
    @Test(groups = {("Devint"),("PreProd"),("Prod")})
    public void callAPIJsonPostMethod(String evn) {
        response = Action.callAPIPostMethodJson(Action.getUrl(evn).concat(booking));
        Action.verifyCallApi(response);
    }
    @Test(groups = {("Devint"),("Prod")})
    public void validateSchemaPost(){
        Action.validateSchemaPost(response.getBody());
    }
}
