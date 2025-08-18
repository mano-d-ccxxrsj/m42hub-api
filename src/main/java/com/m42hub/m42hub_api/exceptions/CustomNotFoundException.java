package com.m42hub.m42hub_api.exceptions;

public class CustomNotFoundException extends RuntimeException {
  public CustomNotFoundException(String message) {
    super(message);
  }
}
