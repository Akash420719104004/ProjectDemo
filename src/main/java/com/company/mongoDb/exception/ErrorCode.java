package com.company.mongoDb.exception;

public enum ErrorCode implements ErrorHandle{
    CAP_1001("1001", "No valid branches found for IDs"),
    CAP_1002("1002", "Invalid User"),
    CAP_1003("1003", "Invalid Input"),
    CAP_1004("1004","Mobile Number Already Exists "),
    CAP_1005("1005","Email Already Exists"),
    CAP_1006("1006","Role Input Invalid"),
    CAP_1007("1007","Areaofexpertice Input Invalid"),
    ;
    private final String errorCode;
    private final String message;

    ErrorCode(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    @Override
    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
