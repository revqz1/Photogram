import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    `java-library`
    alias(libs.plugins.shadow)
}

dependencies {
    implementation(project(":salute-core"))
    compileOnly(libs.velocity.api)
    implementation(libs.guice)
    implementation(libs.acf.velocity)
    implementation(libs.bstats.velocity)
}

tasks.named<ProcessResources>("processResources") {
    val pluginVersion = version.toString()
    inputs.property("version", pluginVersion)

    filesMatching("velocity-plugin.json") {
        expand("version" to pluginVersion)
    }
}
