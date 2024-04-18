package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class LanguageDictionary implements DataRepository {

    public String languageCode;
    private String pathToSource;
    private Map<String, String> data;

    public LanguageDictionary(String pathToSource) {
        this.pathToSource = pathToSource;
        this.languageCode = pathToSource.substring(pathToSource.length() - 7, pathToSource.length() - 4);
        this.data = mapFile();
    }

    @Override
    public Map<String, String> getData() {
        return data;
    }

    public String get(String key) {
        String value = data.get(key);
        if (value == null)
            return "ERROR: Word not found in dictionary";
        else
            return value;
    }

    private Map<String, String> mapFile() {
        try (Stream<String> lines = Files.lines(Paths.get(pathToSource))) {
            return lines.map(line -> line.substring(1,line.length()-1).split(",", 2))
                    .filter(parts -> parts.length == 2)
                    .collect(Collectors.toMap(
                            parts -> parts[0].trim(),
                            parts -> parts[1].trim()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
