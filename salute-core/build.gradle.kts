import org.gradle.language.jvm.tasks.ProcessResources

plugins {
    `java-library`
}

dependencies {
    compileOnly(libs.paper.api)
    compileOnly(libs.miniplaceholders.api)
    implementation(libs.configurate.yaml)
    implementation(libs.guice)
    implementation(libs.gson)
    implementation(libs.bundles.mineskin)
}

tasks.named<ProcessResources>("processResources") {
    val pluginVersion = version.toString()
    inputs.property("version", pluginVersion)

    filesMatching("salute.properties") {
        expand("version" to pluginVersion)
    }
}
