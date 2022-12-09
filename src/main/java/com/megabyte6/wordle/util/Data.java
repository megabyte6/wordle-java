package com.megabyte6.wordle.util;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class Data {

    private final HashMap<String, Object> data = new HashMap<>();
    private final HashMap<String, HashMap<UUID, Consumer<Object>>> observers = new HashMap<>();

    /**
     * Associates the specified value with the specified key. If there was
     * previously a property with that key, the value will not be replaced. Use
     * {@link #set(String, Object)} if you wish to change the value instead.
     * 
     * @param key   The key with which the specified value is to be associated.
     * @param value The value to be associated with the specified key.
     * @return      {@code true} if the property was set successfully.
     *              {@code false} if the property was already initialized.
     */
    public boolean add(String key, Object value) {
        if (data.containsKey(key)) {
            System.err.println("WARNING: Attempted to add key that already exists.");
            Thread.dumpStack();
            return false;
        }
        data.put(key, value);
        observers.put(key, new HashMap<>());
        return true;
    }

    /**
     * @param key The key whose associated value is to be returned.
     * @return    The value to which the specified key is mapped to, or
     *            {@code null} if there is no property matching this key.
     */
    public Object get(String key) {
        if (!data.containsKey(key)) {
            System.err.println("WARNING: Attempted to get key that doesn't exist.");
            Thread.dumpStack();
            return null;
        }
        return data.get(key);
    }

    /**
     * Changes the value at the specified key. If there is no existing property
     * with that key, the property will not created. Use
     * {@link #add(String, Object)} if you wish to add a new property.
     * 
     * @param key The key with which the specified value is to be associated.
     * @param to  The new value to replace the old value with.
     * @return    {@code true} if the property was set successfully.
     *            {@code false} if the property was not already initialized.
     */
    public boolean set(String key, Object to) {
        if (!data.containsKey(key)) {
            System.err.println("WARNING: Attempted to change key that doesn't exist.");
            Thread.dumpStack();
            return false;
        }
        data.put(key, to);
        updateObservers(key);
        return true;
    }

    /**
     * Removes the specified key if present.
     * 
     * @param key The key to be removed.
     * @return    The previous value associated with the key, or {@code null}
     *            if there was no property associated with that key.
     */
    public Object remove(String key) {
        observers.remove(key);
        return data.remove(key);
    }

    /**
     * Check if there is a property with the specified key.
     * 
     * @param key The key to test for.
     * @return    {@code true} if there is a property with the specified key.
     *            {@code false} if there is no property with the specified key.
     */
    public boolean containsKey(String key) {
        return data.containsKey(key);
    }

    /**
     * Check if there is a property with the specified value.
     * 
     * @param value The value to test for.
     * @return      {@code true} if there is a property with the specified
     *              value. {@code false} if there is no property with the
     *              specified value.
     */
    public boolean containsValue(Object value) {
        return data.containsValue(value);
    }

    /**
     * Add an observer that runs when the related value changes.
     * 
     * @param key      The key to watch.
     * @param observer The function to run when the data is changed.
     * @return         The {@link java.util.UUID UUID} of the new observer or
     *                 {@code null} if the key does not exist yet or the
     *                 observer given is {@code null}.
     */
    public UUID addObserver(String key, Consumer<Object> observer) {
        if (!containsKey(key) || observer == null)
            return null;
        UUID uuid = UUID.randomUUID();
        this.observers.get(key).put(uuid, observer);
        return uuid;
    }

    /**
     * Remove an observer that was created at an earlier time.
     * 
     * @param key      The key that the observer is watching.
     * @param observer The {@link java.util.UUID UUID} of the observer to
     *                 remove.
     * @return         The observer that was removed or {@code null} if there
     *                 is no observer with that key and
     *                 {@link java.util.UUID UUID}.
     */
    public Consumer<Object> removeObserver(String key, UUID observer) {
        return observers.get(key).remove(observer);
    }

    /**
     * Check if a key has a particular observer.
     * 
     * @param key      The key that the observer to check watches.
     * @param observer The {@link java.util.UUID UUID} of the observer to
     *                 check.
     * @return         {@code true} if there is an observer with the specified
     *                 key and {@link java.util.UUID UUID}. {@code false} if
     *                 there is no observer with the specified key and
     *                 {@link java.util.UUID UUID}.
     */
    public boolean containsObserver(String key, UUID observer) {
        return observers.get(key).containsKey(observer);
    }

    /**
     * Clear all observers associated with the given key.
     * 
     * @param key The key that the observers are associated with.
     */
    public void clearObservers(String key) {
        observers.get(key).clear();
    }

    private void updateObservers(String key) {
        if (observers.get(key) == null)
            return;
        observers.get(key).forEach((uuid, observer) -> {
            observer.accept(data.get(key));
        });
    }

}
