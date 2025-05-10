package com.reopenai.bookstore.component.jackson.deserializer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * @author Allen Huang
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser parser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        long timestamp;
        if (parser.hasToken(JsonToken.VALUE_STRING)) {
            String ts = parser.getValueAsString();
            timestamp = Long.parseLong(ts);
        } else {
            timestamp = parser.getValueAsLong();
        }
        return timestamp > 0 ? LocalDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()) : null;
    }

}
