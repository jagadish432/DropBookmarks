import com.udemy.DropBookmarksApplication;
import com.udemy.DropBookmarksConfiguration;
import io.dropwizard.testing.junit.DropwizardAppRule;
import org.glassfish.jersey.client.JerseyClient;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class AuthIntegrationTest {
    private static final String CONFIG_PATH = "config.yml";

    @ClassRule
    public static final DropwizardAppRule<DropBookmarksConfiguration> RULE = new DropwizardAppRule<>(
            DropBookmarksApplication.class,
            CONFIG_PATH
    );

//    private static final String TARGET = "http://localhost:8080";
    private static final String TARGET = "https://localhost:8443";
    private static final String PATH = "/hello/secured";
    private Client client;

    private static final HttpAuthenticationFeature FEATURE = HttpAuthenticationFeature.basic("user", "p@ssw0rd");

    @Before
    public void setUp(){
        client = ClientBuilder.newClient();
    }

    @After
    public void tearDown(){
        client.close();
    }

    @Test
    public void testSadPath(){
        Response response = client.target(TARGET).path(PATH).request().get();
        assert Response.Status.UNAUTHORIZED.getStatusCode() == response.getStatus(); //because we're not passing the auth credentials for secured method
    }

    @Test
    public void testHappyPath(){
        String exepcted = "HelloSecuredWorld!!";
        client.register(FEATURE);
        String response = client.target(TARGET).path(PATH).request(MediaType.TEXT_PLAIN).get(String.class);
        assert exepcted.equals(response);
    }

}
