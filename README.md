# e2immu/e2immu-support

This project produces a small JAR containing the `e2immu` annotations and support classes for inclusion in your project.

To publish a new version:

```
gradle build publish
```

A note on Java versions: Two methods in the support classes make use of the Java 10+ API: `AddOnceSet.toImmutableSet`
and `SetOnceMap.toImmutableMap`. Please remove them to obtain Java 1.8 compatibility. If you then also replace
the `Stream` calls in the same classes, you can go down to Java 1.7.

