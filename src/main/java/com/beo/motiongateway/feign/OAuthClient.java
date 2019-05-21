package com.beo.motiongateway.feign;

import com.beo.motiongateway.feign.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "OAuthClient", url = "${servers.auth-server}")
public interface OAuthClient {

    @RequestMapping(method = RequestMethod.POST, value = "/oauth/check_token")
    User checkToken(@RequestParam("token") String token);

}

