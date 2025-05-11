repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api(project(":core"))

    // -- spigot api -- (base)
    compileOnly(libs.spigot.api)

    // -- dream-utilities --
    implementation(libs.dream.utilties.bukkit.adventure)

    // -- placeholders --
    implementation(libs.okaeri.placeholders)

    // -- kyori-adventure --
    implementation(libs.adventure.minimessage)
    implementation(libs.adventure.serializer)
}