package com.megabyte6.wordle;

import java.util.HashMap;

import com.megabyte6.wordle.exception.KeyAlreadyExistsException;
import com.megabyte6.wordle.exception.KeyDoesNotExistException;

public class Data {

    private HashMap<String, Object> data = new HashMap<>();

    public Object get(String key) {
        return data.get(key);
    }

    public void set(String key, Object value) throws KeyDoesNotExistException {
        if (!data.containsKey(key))
            throw new KeyDoesNotExistException();
        data.put(key, value);
    }

    public void add(String key, Object value) throws KeyAlreadyExistsException {
        if (data.containsKey(key))
            throw new KeyAlreadyExistsException();
        data.put(key, value);
    }

    public void remove(String key, Object value) {
        data.remove(key);
    }

    public Object contains(String key) {
        return data.containsKey(key);
    }

}
