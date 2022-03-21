package io.github.victorleitecosta10.taskApi.config;

import com.google.gson.*;
import io.github.victorleitecosta10.taskApi.model.entity.Task;
import org.springframework.context.annotation.Bean;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class TaskToJson {

    @Bean
    public Gson gson() {
        return new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }
}

class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime> {

    public JsonElement serialize(LocalDateTime localDateTime, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(localDateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
