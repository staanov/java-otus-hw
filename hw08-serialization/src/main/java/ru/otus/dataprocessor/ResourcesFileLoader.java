package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.model.Measurement;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class ResourcesFileLoader implements Loader {

    private final String path;
    private final Gson gson = new Gson();

    public ResourcesFileLoader(String fileName) {
        this.path = fileName;
    }

    @Override
    public List<Measurement> load() {
        // читает файл, парсит и возвращает результат
      try (var reader = new InputStreamReader(Objects.requireNonNull(ClassLoader.getSystemResourceAsStream(path)))) {
        var type = TypeToken.getParameterized(List.class, Measurement.class).getType();
        return gson.fromJson(reader, type);
      } catch (IOException e) {
          throw new FileProcessException(e);
      }
    }
}
