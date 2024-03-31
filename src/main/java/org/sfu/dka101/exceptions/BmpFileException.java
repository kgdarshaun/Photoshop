package org.sfu.dka101.exceptions;

public class BmpFileException extends Exception {
    public BmpFileException(String errorMessage, Throwable ex) {
        super(errorMessage, ex);
    }
}
