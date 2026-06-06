repositories {
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.reposilite.com/maven-central")
}

dependencies {
    api(project(":core"))

    // -- spigot api -- (base)
    compileOnly(libs.spigot.api)

    // -- dream-utilities --
    api(libs.dream.utilties.bukkit)
}