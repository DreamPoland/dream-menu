repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.reposilite.com/maven-central")
}

dependencies {
    api(project(":bukkit"))

    // -- spigot api -- (base)
    compileOnly(libs.spigot.api)

    // -- okaeri-config --
    implementation(libs.okaeri.configs)
}