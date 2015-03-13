package org.zerotul.specification.exception;

/**
 * Created by zerotul on 12.03.15.
 */
public class BuildException extends Exception{

    public BuildException() {
    }

    public BuildException(String message) {
        super(message);
    }

    public BuildException(String message, Throwable cause) {
        super(message, cause);
    }

    public BuildException(Throwable cause) {
        super(cause);
    }

    public BuildException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
