pluginManagement {
    val kotlinPluginVersion = "1.4.30"

    plugins {
        kotlin("jvm") version kotlinPluginVersion
    }
}

rootProject.name = "rsql-ktorm"

include("rsql-ktorm")
include("rsql-ktorm-mysql")
include("rsql-ktorm-oracle")
include("rsql-ktorm-postgresql")
include("rsql-ktorm-sqlite")
include("rsql-ktorm-sqlserver")
