package com.beo.motiongateway.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "OAuthClient", url = "${security.oauth2.auth-server}")
public interface OAuthClient {

    @RequestMapping(method = RequestMethod.POST, value = "/check_token")
    String checkToken(@RequestParam("token") String token);

}

