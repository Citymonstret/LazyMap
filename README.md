# LazyMap

Simple Java map implementation that allows for lazily loaded values. Currently only
supports hash maps, but it can be extended.


Example:
```java
final LazyMap<String, String> map = new HashLazyMap<>();
map.put("loaded", "loaded value");
map.put("lazy loaded", () -> "lazy loaded value");
// Value will not be calculated before requested
// but it will then be cached
final String lazilyLoaded = map.get("lazy loaded");
```


## Maven

### Repository

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

### Dependency

```xml
<dependency>
    <groupId>com.github.Sauilitired</groupId>
    <artifactId>LazyMap</artifactId>
    <version>1.0</version>
</dependency>
```
