dependencies {
    api(project(":rsql-ktorm"))
    api("org.ktorm:ktorm-support-oracle:3.3.0")

    testImplementation(project(":rsql-ktorm", "testArchive"))
    // testImplementation("oracle:ojdbc6:11.2.0.4.0")
    testImplementation(fileTree("lib"))
    testImplementation("org.testcontainers:oracle-xe:1.15.1")
}
