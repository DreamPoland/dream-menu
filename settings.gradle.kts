pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "dream-menu"

// -- core --
include(":core")

// -- bukkit --
include(":bukkit")
include(":bukkit-adventure")

// -- serializer --
include(":serializer:bukkit-serializer")
include(":serializer:bukkit-adventure-serializer")