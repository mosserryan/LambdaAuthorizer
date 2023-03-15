package com.appsdeveloperblog.aws.lambda.service;

import com.google.gson.JsonObject;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserRequest;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AdminGetUserResponse;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;

import java.util.List;


public class CognitoUserService {

    private final CognitoIdentityProviderClient cognitoIdentityProviderClient;

    public CognitoUserService(String region) {
       this.cognitoIdentityProviderClient = CognitoIdentityProviderClient.builder()
               .region(Region.of(region))
               .build();
    }

    public JsonObject getUserByUsername(String userName, String userPoolId) {
        AdminGetUserRequest adminGetUserRequest = AdminGetUserRequest.builder()
                .username(userName)
                .userPoolId(userPoolId)
                .build();

        AdminGetUserResponse adminGetUserResponse = cognitoIdentityProviderClient.adminGetUser(adminGetUserRequest);
        JsonObject userDetails = new JsonObject();

        if (!adminGetUserResponse.sdkHttpResponse().isSuccessful()) {
            throw new IllegalArgumentException("Unseccessful result. Status code: "
                    + adminGetUserResponse.sdkHttpResponse().statusCode());
        }

        List<AttributeType> userAttributes = adminGetUserResponse.userAttributes();
        userAttributes.stream().forEach((attribute) -> {
            userDetails.addProperty(attribute.name(), attribute.value());
        });

        return userDetails;
    }

}
