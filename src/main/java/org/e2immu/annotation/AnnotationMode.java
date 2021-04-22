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

/**
 * The names of two annotation modes. They decide which annotations are the default when no annotations are present.
 * <ul>
 *     <li>
 *         In the green mode, non-modification is assumed to be exceptional. Therefore the defaults
 *         are, for methods, {@link Modified}, {@link Dependent}, for fields, {@link Variable},
 *         and for types and sub-types {@link MutableModifiesArguments}.
 *     </li>
 *     <li>
 *         In the red mode, modification is assumed to be exceptional. The default annotations are
 *         for methods {@link NotModified}, {@link Independent}, for fields, {@link Final}, and for
 *         types and sub-types {@link Container}.
 *     </li>
 * </ul>
 * <p>
 * In the IntelliJ highlighter, the user chooses an annotation mode for visualisation.
 * <p>
 * In the analyser, the mode is decided per type. This decision is only important when parsing
 * annotated API files; it has no bearing on annotations that are computed.
 * The green mode is the default unless annotations from the green mode are present.
 * In other words, as soon as one of {@link Modified}, {@link Dependent} for methods, {@link Variable} for fields,
 * or {@link MutableModifiesArguments} on the type or any sub-types, is present, the default becomes the red mode.
 */
public enum AnnotationMode {
    /**
     * Green annotation mode. Modification is the default, non-modification the exception.
     */
    GREEN,
    /**
     * Red annotation mode. Non-modification is the default, modification the exception.
     */
    RED
}
