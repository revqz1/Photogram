import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    `java-library`
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(project(":salute-core"))
    compileOnly(libs.paper.api)
    implementation(libs.guice)
    implementation(libs.acf.paper)
    implementation(libs.bstats.bukkit)
}

tasks.named<ProcessResources>("processResources") {
    val pluginVersion = version.toString()
    inputs.property("version", pluginVersion)

    filesMatching("paper-plugin.yml") {
        expand("version" to pluginVersion)
    }
}
