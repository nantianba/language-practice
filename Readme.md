* JDK21时的运行vm参数
```
```
--add-modules=jdk.incubator.vector
--add-opens java.base/java.lang=ALL-UNNAMED
--add-opens java.base/java.util=ALL-UNNAMED
--add-opens java.base/java.math=ALL-UNNAMED
--add-opens java.base/java.lang.reflect=ALL-UNNAMED
--add-opens java.base/java.text=ALL-UNNAMED
--add-opens java.desktop/java.awt.font=ALL-UNNAMED
* TTL配置
```
-javaagent:$ProjectFileDir$\src\main\resources\transmittable-thread-local-2.14.2.jar
```
