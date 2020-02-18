# AuthenticationApi

All URIs are relative to */*

Method | HTTP request | Description
------------- | ------------- | -------------
[**apiV1AuthLoginPost**](AuthenticationApi.md#apiV1AuthLoginPost) | **POST** /api/v1/auth/login | login authentication

<a name="apiV1AuthLoginPost"></a>
# **apiV1AuthLoginPost**
> Auth apiV1AuthLoginPost(body)

login authentication

### Example
```java
// Import classes:
//import io.swagger.client.ApiException;
//import io.swagger.client.api.AuthenticationApi;


AuthenticationApi apiInstance = new AuthenticationApi();
AuthParam body = new AuthParam(); // AuthParam | 
try {
    Auth result = apiInstance.apiV1AuthLoginPost(body);
    System.out.println(result);
} catch (ApiException e) {
    System.err.println("Exception when calling AuthenticationApi#apiV1AuthLoginPost");
    e.printStackTrace();
}
```

### Parameters

Name | Type | Description  | Notes
------------- | ------------- | ------------- | -------------
 **body** | [**AuthParam**](AuthParam.md)|  |

### Return type

[**Auth**](Auth.md)

### Authorization

No authorization required

### HTTP request headers

 - **Content-Type**: application/json
 - **Accept**: */*

