package com.identity_service.dtos;

public record ChangeEmailRequest(String currentEmail, String newEmail,String password){}
