package org.e2immu.annotation.rare;

public @interface Hint {

    enum H {
        DO_NOT_INLINE
    }

    H[] value();
}
