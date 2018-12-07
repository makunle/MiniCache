# MiniCache
mmap based safety and fast cache

use mmap in java with MappedbByteBuffer

## support data
support key-value data
- key: `string`
- value: `byte[]`

## usage
```java
MiniCache.init(getCacheDir().getAbsolutePath());

MiniCache cache = MiniCache.getDefaultCache();

cache.put("string", "data");
cache.put("int", 123);

cache.getString("string"); // "data"
cache.getInt("int");  // 123

cache.remove("string");
cache.remove("int");

```
