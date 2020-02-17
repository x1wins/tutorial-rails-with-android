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
 * API tests for PostApi
 */
@Ignore
public class PostApiTest {

    private final PostApi api = new PostApi();

    /**
     * list posts
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1PostsGetTest() throws ApiException {
        String authorization = null;
        Integer categoryId = null;
        Integer page = null;
        Integer per = null;
        Integer commentPage = null;
        Integer commentPer = null;
        String search = null;
        api.apiV1PostsGet(authorization, categoryId, page, per, commentPage, commentPer, search);

        // TODO: test validations
    }
    /**
     * delete post
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1PostsIdDeleteTest() throws ApiException {
        String id = null;
        String authorization = null;
        api.apiV1PostsIdDelete(id, authorization);

        // TODO: test validations
    }
    /**
     * show post
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1PostsIdGetTest() throws ApiException {
        String id = null;
        String authorization = null;
        Integer commentPage = null;
        Integer commentPer = null;
        api.apiV1PostsIdGet(id, authorization, commentPage, commentPer);

        // TODO: test validations
    }
    /**
     * update post
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1PostsIdPutTest() throws ApiException {
        Object body = null;
        String id = null;
        String authorization = null;
        api.apiV1PostsIdPut(body, id, authorization);

        // TODO: test validations
    }
    /**
     * create post
     *
     * 
     *
     * @throws ApiException
     *          if the Api call fails
     */
    @Test
    public void apiV1PostsPostTest() throws ApiException {
        Object body = null;
        String authorization = null;
        api.apiV1PostsPost(body, authorization);

        // TODO: test validations
    }
}
