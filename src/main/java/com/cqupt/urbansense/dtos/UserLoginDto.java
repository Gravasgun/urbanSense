package com.cqupt.urbansense.dtos;

import lombok.Data;
import java.io.Serializable;

@Data
public class UserLoginDto implements Serializable {
    private String code;
}
