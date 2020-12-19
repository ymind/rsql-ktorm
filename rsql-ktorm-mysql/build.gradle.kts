dependencies {
    api(project(":rsql-ktorm"))
    api("org.ktorm:ktorm-support-mysql:3.3.0")

    testImplementation(project(":rsql-ktorm", "testArchive"))
    testImplementation("mysql:mysql-connector-java:8.0.23")
    testImplementation("org.testcontainers:mysql:1.15.1")
}
