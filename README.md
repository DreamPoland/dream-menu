# Dream-Menu
[![Build](https://github.com/DreamPoland/dream-menu/actions/workflows/gradle.yml/badge.svg)](https://github.com/DreamPoland/dream-menu/actions/workflows/gradle.yml)

Inventory API with config serialization.

# Installation: (releases)

### Maven
Add these declarations to your ``pom.xml``

```xml
<repository>
  <id>dream-repository-releases</id>
  <name>DreamCode | Repository</name>
  <url>https://repo.dreamcode.cc/releases</url>
</repository>
```
```xml
<dependency>
    <groupId>cc.dreamcode.menu</groupId>
    <artifactId>{platform}</artifactId>
    <version>1.4.3</version>
</dependency>
```

### Gradle
Add these declarations to your ``build.gradle``

```gradle
maven { url "https://repo.dreamcode.cc/releases" }
```
```gradle
implementation "cc.dreamcode.menu:{platform}:1.4.3"
```
