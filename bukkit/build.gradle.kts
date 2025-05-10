repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api(project(":core"))

    // -- spigot api -- (base)
    compileOnly(libs.spigot.api)

    // -- dream-utilities --
    api(libs.dream.utilties.bukkit)

    // -- placeholders --
    api(libs.okaeri.placeholders)
}