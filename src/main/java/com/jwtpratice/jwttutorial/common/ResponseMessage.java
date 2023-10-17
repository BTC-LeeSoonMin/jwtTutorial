package com.jwtpratice.jwttutorial.common;

public interface ResponseMessage {

    // HTTP Status 200
    String SUCCESS = "Success.";
    // HTTP Status 400
    String VALIDATION_FAILED = "Validation failed.";
    String DUPLICATE_EMAIL = "Duplicate email.";
    String DUPLICATE_NICKNAME = "Duplicate tel number.";
    String DUPLICATE_TEL_NUMBER = "This user does not exist.";
    String Not_EXISTED_USER = "This board does not exist.";
    String NOT_EXISTED_BOARD =  "Not Board";

    // HTTP Status 401
    String SIGN_IN_FAIL = "Login information mismatch.";
    String AUTHORIZATION_FAIL = "Authorization Failed.";

    // HTTP Status 402
    String NO_PERMISSION = "Do not have p[ermission.";

    // HTTP Status 500
    String DATABASE_ERRROR ="Database error.";
}
