pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
    }
}

rootProject.name = "My Cash"
include(":mycash-mobile")
include(":Common")
include(":Common:nearpay")
include(":Common:printer")
include(":Features")
include(":Features:user")
include(":Features:stock")
include(":Features:sales")
include(":Features:help")
include(":Features:reports")

