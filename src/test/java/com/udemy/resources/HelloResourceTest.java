package com.udemy.resources;

import com.google.common.base.Optional;
import com.udemy.core.User;
import io.dropwizard.auth.AuthFactory;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicAuthFactory;
import io.dropwizard.auth.basic.BasicCredentials;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;
import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;

import javax.ws.rs.core.MediaType;


public class HelloResourceTest {
    private static final Authenticator<BasicCredentials, User> AUTHENTICATOR = new Authenticator<BasicCredentials, User>() {
        @Override
        public Optional<User> authenticate(BasicCredentials basicCredentials) throws AuthenticationException {
            return Optional.of(new User());
        }
    };

    private static final HttpAuthenticationFeature FEATURE = HttpAuthenticationFeature.basic("user", "password");
    @ClassRule
    public static final ResourceTestRule RULE = ResourceTestRule.builder().addProvider(
            AuthFactory.binder(
                    new BasicAuthFactory<>(
                            AUTHENTICATOR,
                            "realm",
                            User.class
                    )
            )
    ).setTestContainerFactory(
            new GrizzlyWebTestContainerFactory()
            )
    .addResource(new HelloResource()).build();

    @Test
    public void getGreeting() {
        String expected = "HelloWorld!!";
        String actual = RULE.getJerseyTest().target("/hello").request(MediaType.TEXT_PLAIN).get(String.class);
        assert expected.equals(actual);
    }

    @Before
    public void setUpClass(){
        RULE.getJerseyTest().client().register(FEATURE);
    }
    @Test
    public void getSecuredGreeting() {
        String expected = "HelloSecuredWorld!!";
        String actual = RULE.getJerseyTest().target("/hello/secured").request(MediaType.TEXT_PLAIN).get(String.class);
        assert expected.equals(actual);
    }
}