# e2immu/e2immu-support

This project produces a small JAR containing the _e2immu_ annotations and support classes for inclusion in your project.
Please refer to https://www.e2immu.org for more information about the _e2immu_ project.

To publish a new version to your local Maven repository, execute:

```
./gradlew publishToMavenLocal
```

A jar with reference `org.e2immu:e2immu-support:0.2.0` will be available for inclusion.

A note on Java versions: Two methods in the support classes make use of the Java 10+ API: `AddOnceSet.toImmutableSet`
and `SetOnceMap.toImmutableMap`. Please remove them to obtain Java 1.8 compatibility. If you then also replace
the `Stream` calls in the same classes, you can go down to Java 1.7.

