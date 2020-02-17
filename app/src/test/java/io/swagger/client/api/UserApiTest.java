/*
 * API V1
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: v1
 * 
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */

package io.swagger.client.api;

import io.swagger.client.ApiException;
import org.junit.Test;
import org.junit.Ignore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * API tests for UserApi
 */
@Ignore
public class UserApiTest {

    private final UserApi api = new UserApi();

    /**
     * list users
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1UsersGetTest() throws ApiException {
        String page = null;
        String authorization = null;
        api.apiV1UsersGet(page, authorization);

        // TODO: test validations
    }
    /**
     * create user
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1UsersPostTest() throws ApiException {
        Object body = null;
        api.apiV1UsersPost(body);

        // TODO: test validations
    }
    /**
     * delete user
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1UsersUsernameDeleteTest() throws ApiException {
        Object _username = null;
        String authorization = null;
        api.apiV1UsersUsernameDelete(_username, authorization);

        // TODO: test validations
    }
    /**
     * show user
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1UsersUsernameGetTest() throws ApiException {
        Object _username = null;
        String authorization = null;
        api.apiV1UsersUsernameGet(_username, authorization);

        // TODO: test validations
    }
}
