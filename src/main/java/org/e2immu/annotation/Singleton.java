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
 * Annotation used to indicate that a class is a singleton: only one instance can be created.
 * The analyser currently implements two methods of verification:
 * <ol>
 *     <li>a precondition on a private static boolean field</li>
 *     <li>one call from the initializer of a field to a single, private constructor.</li>
 * </ol>
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.TYPE})
public @interface Singleton {
    /**
     * Parameter to mark that the annotation should be absent, or not.
     *
     * @return <code>true</code> when the analyser should not detect this annotation.
     */
    boolean absent() default false;

    /**
     * Parameter to mark that the annotation is present, whether detected or not.
     *
     * @return <code>true</code> when this annotation is contracted, present.
     */
    boolean contract() default false;
}
