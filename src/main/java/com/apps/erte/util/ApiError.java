package com.apps.erte.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class ApiError {
    private int status;
    private String message;
    private String details;
    private Date timestamp;
}
