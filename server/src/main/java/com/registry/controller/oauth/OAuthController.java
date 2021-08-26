package com.registry.controller.oauth;

import com.registry.constant.ApplicationConstant;
import com.registry.constant.CommonConstant;
import com.registry.constant.Path;
import com.registry.exception.AccessDeniedException;
import com.registry.util.RestApiUtil;
import com.registry.value.common.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by boozer on 2019. 6. 18
 */
@RestController
public class OAuthController {

    protected static final Logger logger = LoggerFactory.getLogger(OAuthController.class);

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Private Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @Value("${security.oauth2.client.client-id}")
    private String clientId;

    @Value("${security.oauth2.client.client-secret}")
    private String clientSecret;

    @Value("${security.oauth2.auth-server-uri}")
    private String authServer;

    /** 토큰 서비스 */
    @Resource(name="tokenServices")
    private ConsumerTokenServices tokenServices;

    @Autowired
    private ApplicationConstant appConst;

    @Autowired
    private RestApiUtil restApiUtil;

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Protected Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Public Variables
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Constructor
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Getter & Setter Method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    /*-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=
    | Public Method
    |-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=*/

    @PreAuthorize("hasAuthority('USER')")
    @DeleteMapping(Path.OAUTH_REVOKE)
    @ApiOperation(
            value = "revokeToken",
            notes = "토큰제거"
    )
    public Object revokeToken(
            // 토큰
            @ApiParam(
                    defaultValue="bearer ",
                    value ="토큰",
                    required = true
            )
            @RequestHeader(name = "Authorization") String authorization ) throws Exception {

        // 토큰
        String token = authorization.substring(authorization.indexOf(" ")+1, authorization.length());

        // 토큰제거
        boolean isDelete = tokenServices.revokeToken(token);

        // 결과
        Result result = new Result();
        result.setData(isDelete);
        result.setCode(CommonConstant.CommonCode.SUCCESS);
        result.setMessage(CommonConstant.CommonMessage.SUCCESS);
        return result;
    }

    @PostMapping(Path.OAUTH_TOKEN)
    @ApiOperation(value = "dummy token", notes = "dummy token API")
    protected Object getDummyToken(
            @ApiParam(
                defaultValue = "Basic cmVnaXN0cnk6cmVnaXN0cnktc2VjcmV0",
                value = "",
                required = true) @RequestHeader(name = "Authorization") String authorization,
           @ApiParam(
                   defaultValue = "password",
                   value = "grant type",
                   required = true
           ) @RequestParam(name = "grant_type") String grantType,
           @ApiParam(
                   defaultValue = "read",
                   value = "scope",
                   required = true
           ) @RequestParam(name = "scope") String scope,
           @ApiParam(
                   defaultValue = "admin",
                   value = "username",
                   required = true
           ) @RequestParam(name = "username") String username,
           @ApiParam(
                   defaultValue = "password",
                   value = "password",
                   required = true
           ) @RequestParam(name = "password") String password ) throws Exception {

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(clientId, clientSecret);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
        map.add("scope", "read");

        map.add("grant_type", "password");
        map.add("username", username);
        map.add("password", password);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

        String url = authServer + "/oauth/token";
        RestTemplate restTemplate = new RestTemplate();
        Map result = null;
        try {
            ResponseEntity<Map> response = restTemplate.postForEntity( url, request , Map.class );
            result = response.getBody();
        } catch (Exception e) {
            logger.debug("fail get token");
            logger.debug("user : ", username);
        }

        return result;
    }
}
