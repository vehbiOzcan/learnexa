package com.learnexa.learnexaapi.schema;

import io.swagger.v3.oas.models.media.Schema;

public class ApiResponseSchema {
    public static Schema<Object> getCustomResponseSchema(String timestampExample, int statusExample, String errorExample, String pathExample) {
        return new Schema<>()
                .addProperty("timestamp",new Schema<String>().type("string").description("Hata oluşma zamanı").example(timestampExample))
                .addProperty("status",new Schema<Integer>().type("integer").description("Hata HTTP durum kodu").example(statusExample))
                .addProperty("error",new Schema<String>().type("string").description("Hata mesajı").example(errorExample))
                .addProperty("path",new Schema<String>().type("string").description("Hatanın oluştuğu endpoint").example(pathExample));
    }
}
