repositories {
    maven("https://repo.codemc.io/repository/nms")
    maven("https://oss.sonatype.org/content/repositories/snapshots")
}

dependencies {
    implementation(project(":core"))

    // -- spigot api -- (base)
    compileOnly("com.destroystokyo.paper:paper-api:1.16.5-R0.1-SNAPSHOT")

    // -- placeholders --
    implementation("eu.okaeri:okaeri-placeholders-core:5.0.1")

    // -- kyori-adventure --
    compileOnly("net.kyori:adventure-text-minimessage:4.16.0")

    // -- dream-utilities --
    implementation("cc.dreamcode:utilities:1.4.1")
    implementation("cc.dreamcode:utilities-bukkit:1.4.1")
}