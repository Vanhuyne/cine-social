package com.huy.backend.dto.google;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GoogleUserInfo {
    @JsonProperty("sub")
    private String id; // Unique identifier for the user

    @JsonProperty("name")
    private String name; // Full name of the user

    @JsonProperty("given_name")
    private String givenName; // First name of the user

    @JsonProperty("family_name")
    private String familyName; // Last name of the user

    @JsonProperty("picture")
    private String picture; // URL of the user's profile picture

    @JsonProperty("email")
    private String email; // Email address of the user

    @JsonProperty("email_verified")
    private boolean emailVerified; // Whether the email is verified

    @JsonProperty("locale")
    private String locale;
}
