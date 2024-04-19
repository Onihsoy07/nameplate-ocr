package com.daco.nameplateocr.exception;

public class NotAttachMultipartFileException extends RuntimeException {

    public NotAttachMultipartFileException(String message) {
        super(message);
    }

    public NotAttachMultipartFileException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAttachMultipartFileException(Throwable cause) {
        super(cause);
    }

    protected NotAttachMultipartFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
