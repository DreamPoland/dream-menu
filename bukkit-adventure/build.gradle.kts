repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(project(":core"))

    // -- spigot api -- (base)
    compileOnly("org.spigotmc:spigot-api:1.8.8-R0.1-SNAPSHOT")

    // -- placeholders --
    implementation("eu.okaeri:okaeri-placeholders-core:5.0.1")

    // -- kyori-adventure --
    implementation("net.kyori:adventure-text-minimessage:4.17.0")
    implementation("net.kyori:adventure-text-serializer-legacy:4.17.0")

    // -- dream-utilities --
    implementation("cc.dreamcode:utilities:1.5.1")
    implementation("cc.dreamcode:utilities-bukkit-adventure:1.5.1")
}