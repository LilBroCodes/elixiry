package org.lilbrocodes.elixiry.common.util;

public class NotAllFieldsFilledException extends RuntimeException {
    public NotAllFieldsFilledException() {
        super("All fields of builder must be filled and must not be null!");
    }
}
