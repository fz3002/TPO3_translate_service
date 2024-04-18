package com.example;

import java.util.Map;

public interface DataRepository {

    public <T, V> Map<T, V> getData();

}
