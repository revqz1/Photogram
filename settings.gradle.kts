import org.gradle.api.initialization.resolve.RepositoriesMode

pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS

    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
        maven("https://oss.sonatype.org/content/repositories/snapshots/")
        maven("https://repo.aikar.co/content/groups/public/")
        maven("https://repo.inventivetalent.org/repository/public/")
    }
}

rootProject.name = "Salute"

include("salute-core")
include("salute-paper")
include("salute-velocity")
