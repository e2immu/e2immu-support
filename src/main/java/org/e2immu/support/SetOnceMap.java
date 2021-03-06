/*
 * e2immu: a static code analyser for effective and eventual immutability
 * Copyright 2020-2021, Bart Naudts, https://www.e2immu.org
 *
 * This program is free software: you can redistribute it and/or modify it under the
 * terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License for
 * more details. You should have received a copy of the GNU Lesser General Public
 * License along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.e2immu.support;

import org.e2immu.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * Example of an eventually level 2 immutable map, where each key can be put only once,
 * and removal is not permitted. Once the map is frozen, elements cannot be added anymore.
 * <p>
 * This is an example class! Please extend and modify for your needs.
 *
 * @param <K> The type for keys.
 * @param <V> The type for values.
 */
@E2Container(after = "frozen")
public class SetOnceMap<K, V> extends Freezable {

    private final Map<K, V> map = new HashMap<>();

    /**
     * Put an key-value pair in the map. You cannot use the same key twice, not even with the same value.
     * Null keys or values are not permitted.
     *
     * @param k the key, must not be null
     * @param v the value, must not be null
     * @throws IllegalStateException when the map is already frozen, or the key is already present
     * @throws NullPointerException  when a parameter is null
     */
    @Only(before = "frozen")
    @Modified
    public void put(@NotNull K k, @NotNull V v) {
        Objects.requireNonNull(k);
        Objects.requireNonNull(v);
        ensureNotFrozen();
        if (isSet(k)) {
            throw new IllegalStateException("Already decided on " + k + ": have " + get(k) + ", want to write " + v);
        }
        map.put(k, v);
    }


    /**
     * Get the value associated to the key already in the map, or generate one and put it in the map.
     *
     * @param k         the key
     * @param generator the generator to generate a value, given the key, when the key has no value associated yet.
     *                  The generator must generate a non-null value.
     * @return either the value already present, or the value generated. This is the value in the map.
     * @throws IllegalStateException when the map is already frozen
     * @throws NullPointerException  when the generator generates a null value.
     */
    @Only(before = "frozen")
    @NotNull
    @Modified
    public V getOrCreate(@NotNull K k, @NotNull1 @NotModified @PropagateModification Function<K, V> generator) {
        ensureNotFrozen();
        V v = map.get(k);
        if (v != null) return v;
        V vv = Objects.requireNonNull(generator.apply(k));
        map.put(k, vv);
        return vv;
    }

    /**
     * Obtain the value for a given key, but only when the key is already present!
     * Use <code>getOtherwiseNull</code> or <code>getOrDefault</code> for other behaviour when the key is absent.
     *
     * @param k the key, not null
     * @return the value, not null.
     * @throws IllegalStateException when the key is not yet present.
     */
    @NotNull
    @NotModified
    public V get(@NotNull K k) {
        if (!isSet(k)) throw new IllegalStateException("Not yet decided on " + k);
        return Objects.requireNonNull(map.get(k));
    }

    /**
     * A more permissive method, which can be called whether the key is present or not.
     *
     * @param k the key.
     * @return null when the key is not present, the value of the key otherwise.
     */
    @Nullable
    @NotModified
    public V getOrDefaultNull(@NotNull K k) {
        return map.get(k);
    }

    /**
     * A more permissive method, which can be called whether the key is present or not.
     * Because the default value is not allowed to be null, the result is never null.
     *
     * @param k the key.
     * @param v the value returned when the key is not present. Cannot be null.
     * @return the second parameter when the key is not present, the value of the key otherwise.
     */
    @NotModified
    @NotNull
    public V getOrDefault(@NotNull K k, @NotNull V v) {
        return Objects.requireNonNull(map.getOrDefault(k, Objects.requireNonNull(v)));
    }

    /**
     * Return the size of the map
     *
     * @return the size of the map.
     */
    @NotModified
    public int size() {
        return map.size();
    }

    /**
     * Check if the key has been set.
     *
     * @param k the key
     * @return <code>true</code> when a value has been put for this key.
     */
    @NotModified
    public boolean isSet(K k) {
        return map.containsKey(k);
    }

    /**
     * Check if the map is empty.
     *
     * @return <code>true</code> when the map is empty.
     */
    @NotModified
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /**
     * Return a stream of (non-null) map entries.
     *
     * @return the map entries.
     */
    @NotNull1
    @NotModified
    public Stream<Map.Entry<K, V>> stream() {
        return map.entrySet().stream();
    }

    /**
     * Convenience method which calls <code>put</code> for every key-value pair in the
     * argument.
     *
     * @param setOnceMap the source.
     */
    @Only(before = "frozen")
    public void putAll(SetOnceMap<K, V> setOnceMap) {
        // NOTE: this line in technically not needed, https://github.com/e2immu/e2immu/issues/49
        ensureNotFrozen();
        setOnceMap.stream().forEach(e -> put(e.getKey(), e.getValue()));
    }

    /**
     * Return a level 2 immutable copy of the underlying map.
     * <p>
     * Only present in Java 10+.
     *
     * @return a level 2 immutable copy
     */
    @NotNull
    @NotModified
    @E2Container
    public Map<K, V> toImmutableMap() {
        return Map.copyOf(map);
    }
}
