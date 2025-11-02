package com.company.mongoDb.exception;

import lombok.Data;

@Data
public class ErrorMessage {
    String message;
    String errorCode;
}
