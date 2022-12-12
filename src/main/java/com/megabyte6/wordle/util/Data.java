package com.megabyte6.wordle.util;

import java.util.HashMap;
import java.util.UUID;
import java.util.function.Consumer;

public class Data {

    private final HashMap<UUID, Object> data = new HashMap<>();
    private final HashMap<UUID, HashMap<UUID, Consumer<Object>>> observers = new HashMap<>();

    private final HashMap<String, UUID> customKeys = new HashMap<>();

    /**
     * @param key The key whose associated value is to be returned.
     * @return    The value to which the specified key is mapped to, or
     *            {@code null} if there is no property matching this key.
     */
    public Object get(UUID key) {
        if (!data.containsKey(key)) {
            System.err.println("WARNING: Attempted to get key that doesn't exist.");
            Thread.dumpStack();
            return null;
        }
        return data.get(key);
    }

    /**
     * This method words the same way as {@link #get(UUID)} except that it
     * accepts a {@link java.lang.String String} as a key. This is purely for
     * convenience and not at all recommended as keys are likely to conflict
     * and overwrite each other.
     * 
     * @param key The key whose associated value is to be returned.
     * @return    The value to which the specified key is mapped to, or
     *            {@code null} if there is no property matching this key.
     */
    public Object get(String key) {
        if (!customKeys.containsKey(key)) {
            System.err.println("WARNING: Attempted to get key that doesn't exist.");
            Thread.dumpStack();
            return null;
        }
        return data.get(customKeys.get(key));
    }

    /**
     * Creates a new property holding the value given.
     * 
     * @param value The value to store.
     * @return      The UUID used to fetch the value at a later date.
     */
    public UUID set(Object value) {
        UUID newUUID = UUID.randomUUID();
        data.put(newUUID, value);
        return newUUID;
    }

    /**
     * Sets the value at the specified key. If there is no existing property
     * with that key, the property will be created. This is very similar to
     * {@link #set(Object)} except that this method allows you to set a
     * specific {@link java.util.UUID UUID} to use.
     * 
     * @param key The key with which the specified value is to be associated.
     * @param to  The value to put there.
     * @return    The previous value associated with the key given or
     *            {@code null} if there was no previous value.
     */
    public Object set(UUID key, Object to) {
        Object previousValue = data.put(key, to);
        updateObservers(key);
        return previousValue;
    }

    /**
     * This method words the same way as {@link #set(UUID)} except that it
     * accepts a {@link java.lang.String String} as a key. This is purely for
     * convenience and not at all recommended as keys are likely to conflict
     * and overwrite each other.
     * 
     * @param key The key whose associated value is to be returned.
     * @param to  The value to put there.
     * @return    The previous value associated with the key given or
     *            {@code null} if there was no previous value.
     */
    public Object set(String key, Object to) {
        UUID uuid = customKeys.containsKey(key)
                ? customKeys.get(key)
                : UUID.randomUUID();
        return set(uuid, to);
    }

    /**
     * Removes the specified key if present.
     * 
     * @param key The key of the property to be removed.
     * @return    The previous value associated with the key, or {@code null}
     *            if there was no property associated with that key.
     */
    public Object remove(UUID key) {
        observers.remove(key);
        return data.remove(key);
    }

    /**
     * This method words the same way as {@link #remove(UUID)} except that it
     * accepts a {@link java.lang.String String} as a key. This is purely for
     * convenience and not at all recommended as keys are likely to conflict
     * and overwrite each other.
     * 
     * @param key The key of the property to be removed.
     * @param to  The value to put there.
     * @return    The previous value associated with the key, or {@code null}
     *            if there was no property associated with that key.
     */
    public Object remove(String key) {
        if (!customKeys.containsKey(key))
            return null;
        customKeys.remove(key);
        return remove(customKeys.get(key));
    }

    /**
     * Check if there is a property with the specified key.
     * 
     * @param key The key to test for.
     * @return    {@code true} if there is a property with the specified key.
     *            {@code false} if there is no property with the specified key.
     */
    public boolean containsKey(UUID key) {
        return data.containsKey(key);
    }

    /**
     * This method words the same way as {@link #containsKey(UUID)} except that it
     * accepts a {@link java.lang.String String} as a key. This is purely for
     * convenience and not at all recommended as keys are likely to conflict
     * and overwrite each other.
     * 
     * @param key The key to test for.
     * @return    {@code true} if there is a property with the specified key.
     *            {@code false} if there is no property with the specified key.
     */
    public boolean containsKey(String key) {
        if (!customKeys.containsKey(key))
            return false;
        return containsKey(customKeys.get(key));
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
    public UUID addObserver(UUID key, Consumer<Object> observer) {
        if (!containsKey(key) || observer == null)
            return null;
        UUID uuid = UUID.randomUUID();
        observers.get(key).put(uuid, observer);
        return uuid;
    }

    /**
     * This method words the same way as {@link #addObserver(UUID)} except that it
     * accepts a {@link java.lang.String String} as a key. This is purely for
     * convenience and not at all recommended as keys are likely to conflict
     * and overwrite each other.
     * 
     * @param key      The key to watch.
     * @param observer The function to run when the data is changed.
     * @return         The {@link java.util.UUID UUID} of the new observer or
     *                 {@code null} if the key does not exist yet or the
     *                 observer given is {@code null}.
     */
    public UUID addObserver(String key, Consumer<Object> observer) {
        if (!customKeys.containsKey(key) || observer == null)
            return null;
        return addObserver(customKeys.get(key), observer);
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
    public Consumer<Object> removeObserver(UUID key, UUID observer) {
        return observers.get(key).remove(observer);
    }

    /**
     * This method words the same way as {@link #addObserver(UUID)} except that it
     * accepts a {@link java.lang.String String} as a key. This is purely for
     * convenience and not at all recommended as keys are likely to conflict
     * and overwrite each other.
     * 
     * @param key      The key that the observer is watching.
     * @param observer The {@link java.util.UUID UUID} of the observer to
     *                 remove.
     * @return         The observer that was removed or {@code null} if there
     *                 is no observer with that key and
     *                 {@link java.util.UUID UUID}.
     */
    public Consumer<Object> removeObserver(String key, UUID observer) {
        if (!customKeys.containsKey(key) || observer == null)
            return null;
        return removeObserver(customKeys.get(key), observer);
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
    public boolean containsObserver(UUID key, UUID observer) {
        return observers.get(key).containsKey(observer);
    }

    /**
     * This method words the same way as {@link #addObserver(UUID)} except that it
     * accepts a {@link java.lang.String String} as a key. This is purely for
     * convenience and not at all recommended as keys are likely to conflict
     * and overwrite each other.
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
        if (!customKeys.containsKey(key) || observer == null)
            return false;
        return containsObserver(customKeys.get(key), observer);
    }

    /**
     * Clear all observers associated with the given key.
     * 
     * @param key The key that the observers are associated with.
     */
    public void clearObservers(UUID key) {
        observers.get(key).clear();
    }

    /**
     * This method words the same way as {@link #addObserver(UUID)} except that it
     * accepts a {@link java.lang.String String} as a key. This is purely for
     * convenience and not at all recommended as keys are likely to conflict
     * and overwrite each other.
     * 
     * @param key The key that the observers are associated with.
     */
    public void clearObserver(String key) {
        if (!customKeys.containsKey(key))
            return;
        clearObservers(customKeys.get(key));
    }

    private void updateObservers(UUID key) {
        if (observers.get(key) == null)
            return;
        observers.get(key).forEach((uuid, observer) -> {
            observer.accept(data.get(key));
        });
    }

}
