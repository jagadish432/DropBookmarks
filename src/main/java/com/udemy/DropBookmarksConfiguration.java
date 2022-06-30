package com.udemy;

import io.dropwizard.Configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.*;
import javax.validation.constraints.*;

public class DropBookmarksConfiguration extends Configuration {
    @NotEmpty
    private String password;

    @JsonProperty
    public String getPassword() {
        return password;
    }
}
