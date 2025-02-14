package com.counter.test_counter.exception;

public class OCRPhotoException extends RuntimeException {
  public OCRPhotoException(Throwable cause) {
    super(cause);
  }

  public OCRPhotoException(String message, Throwable cause) {
    super(message, cause);
  }

  public OCRPhotoException(String message) {
        super(message);
    }

  public OCRPhotoException() {
  }
}
