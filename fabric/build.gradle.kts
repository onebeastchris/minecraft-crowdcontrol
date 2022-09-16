val minecraftVersion: String by project
val loaderVersion: String by project
val fabricVersion: String by project
val cloudVersion: String by project
val adventurePlatformFabricVersion: String by project

// inherit resources from common module
sourceSets.main { resources.srcDir(project(":base-common").sourceSets["main"].resources.srcDirs) }

plugins {
    id("fabric-loom")
}

repositories {
    maven {
        name = "Sonatype Snapshots"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots")
    }
}

dependencies {
    implementation(project(":configurate-common"))
    minecraft("com.mojang:minecraft:$minecraftVersion")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:$loaderVersion")
    modImplementation("net.fabricmc.fabric-api:fabric-api:$fabricVersion")
    modImplementation(include("net.kyori:adventure-platform-fabric:$adventurePlatformFabricVersion")!!)
    modImplementation(include("cloud.commandframework:cloud-fabric:$cloudVersion")!!)
}

tasks.withType<ProcessResources> {
    inputs.property("version", project.version)
    filteringCharset = "UTF-8"

    filesMatching("fabric.mod.json") {
        expand("version" to project.version)
    }
}

// Java 17 boilerplate

val targetJavaVersion = 17
tasks.withType<JavaCompile>().configureEach {
    options.release.set(targetJavaVersion)
}

java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
    sourceCompatibility = javaVersion
    targetCompatibility = javaVersion
//    withSourcesJar()
}
