# quarkus-nativedb extension

This module provides a native DB subsystem for Quarkus applications.

It uses the native JPA API internally.

# Features

- Generic DAO structure for native (pure SQL) queries, with criteria API, result mapping, sorting and grouping

- Filtering on arbitrary columns types. Provides a few filters (strings, numbers, dates...), you can extend with your own filters.

- Support for paginated lists

- Wrapper functions for DB compatibility (H2, PostgreSQL, MySQL)
 
- Admin console to debug queries in realtime *WIP*
 
# How to use

####  Add the dependency to your `pom.xml` file

```
    <dependency>
      <groupId>com.sismics.quarkus</groupId>
      <artifactId>quarkus-nativedb</artifactId>
      <version>1.0.0</version>
    </dependency>
```

# Enable the admin console 
*WIP*

The admin console allows you to monitor queries in realtime.

Add the following parameter to enable the admin console:

```
nativedb.console.enabled=true
```

Note: the admin console is enabled by default in Dev mode.

### Secure the admin console

Add the following parameter to secure the admin console

```
nativedb.console.username=console
nativedb.console.password=pass1234
```

# License

This software is released under the terms of the Apache License, Version 2.0. See `LICENSE` for more
information or see <https://opensource.org/licenses/Apache-2.0>.
