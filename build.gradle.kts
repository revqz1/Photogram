import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.tasks.Jar
import org.gradle.jvm.toolchain.JavaLanguageVersion

plugins {
    base
    alias(libs.plugins.shadow) apply false
}

val javaRelease = libs.versions.java.get().toInt()

subprojects {
    group = rootProject.group
    version = rootProject.version

    pluginManager.withPlugin("java") {
        extensions.configure<JavaPluginExtension> {
            toolchain.languageVersion = JavaLanguageVersion.of(javaRelease)
        }

        tasks.withType<JavaCompile>().configureEach {
            options.encoding = "UTF-8"
            options.release = javaRelease
        }
    }

    pluginManager.withPlugin("com.gradleup.shadow") {
        tasks.named<Jar>("jar") {
            enabled = false
        }

        tasks.named<ShadowJar>("shadowJar") {
            archiveClassifier.set("")

            exclude(
                "META-INF/*.SF",
                "META-INF/*.DSA",
                "META-INF/*.RSA",
                "META-INF/*.EC",
                "META-INF/SIG-*"
            )

            relocate("org.bstats", "${project.group}.bstats")
        }

        tasks.named("build") {
            dependsOn("shadowJar")
        }
    }
}
