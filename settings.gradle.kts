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

// -- serializer --
include(":serializer:bukkit-serializer")