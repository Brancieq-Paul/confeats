/**
 * Code in this file is based on the following source:
 * @link https://stackoverflow.com/a/62013873
 */
package fr.paulbrancieq.confeats.client.gui.gson;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.TypeAdapter;
import com.google.gson.TypeAdapterFactory;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AnnotatedTypeAdapterFactory implements TypeAdapterFactory {
  @Override
  public <T> TypeAdapter<T> create(Gson gson, TypeToken<T> typeToken) {
    Class<? super T> rawType = typeToken.getRawType();

    Set<Field> requiredFields = Stream.of(rawType.getDeclaredFields())
        .filter(f -> f.getAnnotation(JsonOptional.class) == null)
        .collect(Collectors.toSet());

    if (requiredFields.isEmpty()) {
      return null;
    }

    @SuppressWarnings("unchecked")
    final TypeAdapter<T> baseAdapter = (TypeAdapter<T>) gson.getAdapter(rawType);

    return new TypeAdapter<>() {

      @Override
      public void write(JsonWriter jsonWriter, T o) throws IOException {
        baseAdapter.write(jsonWriter, o);
      }

      @Override
      public T read(JsonReader in) {
        JsonElement jsonElement = Streams.parse(in);

        if (jsonElement.isJsonObject()) {
          ArrayList<String> missingFields = new ArrayList<>();
          for (Field field : requiredFields) {
            if (!jsonElement.getAsJsonObject().has(field.getName())) {
              missingFields.add(field.getName());
            }
          }
          if (!missingFields.isEmpty()) {
            throw new JsonParseException(
                String.format("Missing required fields %s for %s",
                    missingFields, rawType.getName()));
          }
        }
        TypeAdapter<T> delegate = gson.getDelegateAdapter(AnnotatedTypeAdapterFactory.this, typeToken);
        return delegate.fromJsonTree(jsonElement);
      }
    };

  }
}
