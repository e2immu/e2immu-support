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

import java.util.Objects;

/**
 * An object which first holds an object of type <code>S</code>, and, in the second and final stage of its life-cycle,
 * holds an object of type <code>T</code>.
 * <p>
 * This class is eventually immutable: once the second stage has been reached, its fields cannot be changed anymore.
 * There is no support data and no possibility of content modification; therefore, the type is eventually {@link E2Immutable}.
 *
 * @param <S> type of the initial stage
 * @param <T> type of the final stage
 */

@E2Container(after = "first")
public class FirstThen<S, T> {
    @Final(after = "first")
    private volatile S first;
    @Final(after = "first")
    private volatile T then;

    /**
     * Only constructor
     *
     * @param first the initial value
     * @throws NullPointerException when the argument is <code>null</code>
     */
    public FirstThen(@NotNull S first) {
        this.first = Objects.requireNonNull(first);
    }

    @NotModified
    public boolean isFirst() {
        return first != null;
    }

    @NotModified
    public boolean isSet() {
        return first == null;
    }

    /**
     * The method that sets the final value, discarding the initial one forever.
     * It transitions from mutable to immutable.
     *
     * @param then the final value
     * @throws NullPointerException  when the final value is <code>null</code>
     * @throws IllegalStateException when the object had already reached its final stage
     */
    @Mark("first")
    public void set(@NotNull T then) {
        Objects.requireNonNull(then);
        synchronized (this) {
            if (first == null) throw new IllegalStateException("Already set");
            this.then = then;
            first = null;
        }
    }

    /**
     * Getter for the initial value.
     *
     * @return The initial value
     * @throws IllegalStateException when the object has transitioned into the final stage
     */
    @NotNull
    @NotModified
    @Only(before = "first")
    public S getFirst() {
        if (first == null) throw new IllegalStateException();
        return first;
    }

    /**
     * Getter for the final value.
     *
     * @return The final value
     * @throws IllegalStateException when the object has not yet transitioned into the final stage
     */
    @NotNull
    @NotModified
    @Only(after = "first")
    public T get() {
        // we could have had a check on "then" directly, but then @Only would not be recognized
        if (first != null) throw new IllegalStateException("Not yet set");
        assert then != null;
        return then;
    }

    /**
     * Delegating equals.
     *
     * @param o the other value
     * @return <code>true</code> when <code>o</code> is also a <code>FirstThen</code> object, with
     * the same initial or final object object, as defined by the <code>equals</code> method on <code>S</code>
     * or <code>T</code> respectively.
     */
    @Override
    @NotModified
    public boolean equals(@NotNull(absent = true) Object o) { // o is @NotModified because of Object
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FirstThen<?, ?> firstThen = (FirstThen<?, ?>) o;
        return Objects.equals(first, firstThen.first) &&
                Objects.equals(then, firstThen.then);
    }

    /**
     * Delegating hash code.
     *
     * @return a hash code based on the hash code of the initial or final value.
     */
    @Override
    @NotModified
    public int hashCode() {
        return Objects.hash(first, then);
    }
}