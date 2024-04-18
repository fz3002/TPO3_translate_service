package com.example;

import java.util.HashMap;
import java.util.Map;

public class LanguageDictionary implements DataRepository {

    public String languageCode;
    private String pathToSource;
    private Map<String, String> data = new HashMap<String, String>();

    public LanguageDictionary(String pathToSource) {
        this.pathToSource = pathToSource;
        // TODO: get language code from name of file
    }

    @Override
    public Map<String, String> getData() {
        return data;
    }

}
