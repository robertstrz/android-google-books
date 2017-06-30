package com.example.rostrzep.googlebooksapp.api.exceptions;

import java.io.IOException;
import java.io.Serializable;

public class UnknownServerException extends IOException implements Serializable {

    public UnknownServerException() {
        super();
    }
}
