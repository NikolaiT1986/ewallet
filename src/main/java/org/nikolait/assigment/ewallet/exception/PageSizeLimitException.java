package org.nikolait.assigment.ewallet.exception;

public class PageSizeLimitException extends RuntimeException {
    public PageSizeLimitException(int limit) {
        super("Page size must not exceed %s".formatted(limit));
    }
}
