dependencies {
    api(project(":rsql-ktorm"))
    api("org.ktorm:ktorm-support-sqlite:3.3.0")

    testImplementation(project(":rsql-ktorm", "testArchive"))
    testImplementation("org.xerial:sqlite-jdbc:3.34.0")
}
