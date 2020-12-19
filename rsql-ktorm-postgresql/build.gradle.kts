dependencies {
    api(project(":rsql-ktorm"))
    api("org.ktorm:ktorm-support-postgresql:3.3.0")

    testImplementation(project(":rsql-ktorm", "testArchive"))
    testImplementation("org.postgresql:postgresql:42.2.18")
    testImplementation("org.testcontainers:postgresql:1.15.1")
}
