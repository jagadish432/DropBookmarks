package com.udemy.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.ClassRule;
import org.junit.Ignore;

import javax.ws.rs.core.MediaType;

import static org.junit.jupiter.api.Assertions.*;

class HelloResourceTest {


    @ClassRule
    public static final ResourceTestRule RULE = ResourceTestRule.builder().addResource(new HelloResource()).build();

    @org.junit.jupiter.api.Test
    @Ignore
    void getGreeting() {
        String exepected = "Helloworld!!";
        String actual = RULE.getJerseyTest().target("/hello").request(MediaType.TEXT_PLAIN).get(String.class);
        assertEquals(exepected, actual);
    }
}