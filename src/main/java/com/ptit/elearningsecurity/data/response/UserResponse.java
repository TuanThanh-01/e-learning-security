package com.ptit.elearningsecurity.data.response;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class UserResponse {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String avatar;
}
