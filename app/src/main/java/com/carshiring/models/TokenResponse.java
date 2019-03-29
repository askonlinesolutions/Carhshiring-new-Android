package com.carshiring.models;

/**
 * Created by rakhi on 07/12/2017.
 */

public class TokenResponse {

    /**
     * response_code : 22000
     * device_id : ffffffff-c1df-c07a-a074-8f690033c587
     * response_message : Success
     * service_command : SDK_TOKEN
     * sdk_token : 5CBD8011AAF11A8FE053321E320ACAA1
     * signature : fb9f9c3b4350d4ed6014e6b701d0ba8bd9990a48d9e107fb83492c3277cf5845
     * merchant_identifier : GjitDYjm
     * access_code : qa2s6awTpBNc04Q65T8v
     * language : en
     * status : 22
     */

    private String response_code;
    private String device_id;
    private String response_message;
    private String service_command;
    private String sdk_token;
    private String signature;
    private String merchant_identifier;
    private String access_code;
    private String language;
    private String status;

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getDevice_id() {
        return device_id;
    }

    public void setDevice_id(String device_id) {
        this.device_id = device_id;
    }

    public String getResponse_message() {
        return response_message;
    }

    public void setResponse_message(String response_message) {
        this.response_message = response_message;
    }

    public String getService_command() {
        return service_command;
    }

    public void setService_command(String service_command) {
        this.service_command = service_command;
    }

    public String getSdk_token() {
        return sdk_token;
    }

    public void setSdk_token(String sdk_token) {
        this.sdk_token = sdk_token;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getMerchant_identifier() {
        return merchant_identifier;
    }

    public void setMerchant_identifier(String merchant_identifier) {
        this.merchant_identifier = merchant_identifier;
    }

    public String getAccess_code() {
        return access_code;
    }

    public void setAccess_code(String access_code) {
        this.access_code = access_code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
