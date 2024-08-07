package org.cianid.nadir.model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomLocalTimeDeserializer extends JsonDeserializer<LocalTime> {
    private static final DateTimeFormatter[] FORMATTERS = {
            DateTimeFormatter.ofPattern("HH:mm"),
            DateTimeFormatter.ofPattern("H:mm")
    };

    @Override
    public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String time = p.getText().trim();
        for (DateTimeFormatter formatter : FORMATTERS) {
            try {
                return LocalTime.parse(time, formatter);
            } catch (DateTimeParseException ignored) {
            }
        }
        throw new IOException("Unable to parse time: " + time);
    }
}
