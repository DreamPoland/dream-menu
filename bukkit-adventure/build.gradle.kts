repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api(project(":core"))

    // -- spigot api -- (base)
    compileOnly(libs.spigot.api)

    // -- dream-utilities --
    api(libs.dream.utilties.bukkit.adventure)

    // -- placeholders --
    api(libs.okaeri.placeholders)

    // -- kyori-adventure --
    api(libs.adventure.minimessage)
    api(libs.adventure.serializer)
}