package com.udemy.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.glassfish.jersey.test.JerseyTest;
//import org.junit.jupiter.api.*;
import org.junit.ClassRule;
import org.junit.Test;

import javax.ws.rs.core.MediaType;

//import static org.junit.jupiter.api.Assertions.*;

public class HelloResourceTest {

    @ClassRule
    public static final ResourceTestRule RULE = ResourceTestRule.builder().addResource(new HelloResource()).build();

    @Test
    public void getGreeting() {
        String expected = "HelloWorld!!";
        String actual = RULE.getJerseyTest().target("/hello").request(MediaType.TEXT_PLAIN).get(String.class);
        assert expected.equals(actual);
    }

    @Test
    public void getSecuredGreeting() {
        String expected = "HelloSecuredWorld!!";
        String actual = RULE.getJerseyTest().target("/hello/secured").request(MediaType.TEXT_PLAIN).get(String.class);
        assert expected.equals(actual);
    }
}