package com.thoughtworks.turtles.demos.blackbox.wireTypes;

import lombok.Value;

/**
 * {"lease_id":"",
 * "renewable":false,
 * "lease_duration":0,
 * "data":null,
 * "auth":{
 * "client_token":"d5f21bc0-bbf6-7e8c-804f-37a840e2108d",
 * "policies":["root"],
 * "metadata":{
 * "app-id":"sha1:732395991959afaad55d58fcde486fd49e400190",
 * "user-id":"sha1:732395991959afaad55d58fcde486fd49e400190"},
 * "lease_duration":0,
 * "renewable":false}}
 */
@Value
public class Token {
    private final String value;
}
