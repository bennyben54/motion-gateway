package com.beo.motiongateway.feign.model;

import lombok.Data;

import java.util.List;

@Data
public class User {


    private boolean active;
    private long exp;
    private String user_name;
    private List<String> authorities;
    private List<String> scope;
    private String client_id;


}
