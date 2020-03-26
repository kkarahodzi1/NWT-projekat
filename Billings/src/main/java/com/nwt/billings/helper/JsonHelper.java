package com.nwt.billings.helper;

import com.fasterxml.jackson.databind.ObjectMapper;

// helper za serijalizaciju modela
public class JsonHelper {

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
