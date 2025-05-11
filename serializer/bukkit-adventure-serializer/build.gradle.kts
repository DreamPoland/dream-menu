repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    api(project(":bukkit-adventure"))

    // -- spigot api -- (base)
    compileOnly(libs.spigot.api)

    // -- okaeri-config --
    implementation(libs.okaeri.configs)
}