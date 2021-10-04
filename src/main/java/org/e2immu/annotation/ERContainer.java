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

package org.e2immu.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Short-hand for the combination of {@link E1Immutable} and {@link Container}.
 * It indicates that the type is effectively or eventually level 1 immutable (all fields are (eventually) {@link Final}),
 * and the type is a container.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.TYPE})
public @interface ERContainer {

    /**
     * Parameter to mark that the annotation should be absent, or present.
     * In verification mode, <code>absent=true</code> means that an error will be raised
     * if the analyser computes the annotation. In contract mode, it guarantees absence of the annotation.
     *
     * @return <code>true</code> when the annotation should be absent (verification mode) or must be absent (contract mode).
     */
    boolean absent() default false;

    /**
     * Parameter to set contract mode, even if the annotation occurs in a context
     * where verification mode is normal. Use <code>contract=true</code>
     * to override the computation of the analyser.
     *
     * @return <code>true</code> when switching to contract mode.
     */
    boolean contract() default false;

    /**
     * Marker for eventual immutability.
     *
     * @return when the type is effectively level 1 immutable, set the empty string.
     * When it is eventually level 1 immutable, return a comma-separated list of strings from <code>@Mark</code>
     * values on some of the modifying methods of the type. After these have been called, the
     * type will become effectively level 1 immutable.
     */
    String after() default "";

    /**
     * Some containers are used as "builders" for immutable classes.
     * This parameter shows that there is a build method.
     * <p>
     * This parameter is currently used in a decorative way only.
     *
     * @return the class for which this container is the builder
     */
    Class<?> builds() default Object.class;
}