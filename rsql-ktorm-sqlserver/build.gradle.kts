dependencies {
    api(project(":rsql-ktorm"))
    api("org.ktorm:ktorm-support-sqlserver:3.3.0")

    testImplementation(project(":rsql-ktorm", "testArchive"))
    testImplementation("org.testcontainers:mssqlserver:1.15.1")
}
