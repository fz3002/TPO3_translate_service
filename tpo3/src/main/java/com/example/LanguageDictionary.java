package com.example;

import java.util.HashMap;
import java.util.Map;

public class LanguageDictionary implements DataRepository{

    public String languageCode;
    private String pathToSource;
    private Map<String, String> data = new HashMap<String, String>();
    
    public LanguageDictionary(String pathToSource) {
        this.pathToSource = pathToSource;
        //TODO: get language code from name of file
    }
    
    @Override
    public void getData() {
        //TODO Method to get data from file of given path and adding them to hash map
    }
    
}
