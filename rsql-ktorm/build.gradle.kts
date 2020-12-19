dependencies {
    // https://mvnrepository.com/artifact/cz.jirutka.rsql/rsql-parser
    api("cz.jirutka.rsql:rsql-parser:2.1.0")
    api("org.ktorm:ktorm-core:3.3.0")
    api("io.github.microutils:kotlin-logging-jvm:2.0.4")

    testImplementation("com.h2database:h2:1.4.200")
}

val jarTest by tasks.registering(Jar::class) {
    dependsOn(":testClasses")

    archiveClassifier.set("test")

    from(project.the<SourceSetContainer>()["test"].output)
}

val testArchive by configurations.creating {
    // isCanBeConsumed = true
    // isCanBeResolved = false

    extendsFrom(configurations.testImplementation.get())

    // attributes {
    //     attribute(Usage.USAGE_ATTRIBUTE, project.objects.named(Usage::class, Usage.JAVA_RUNTIME_JARS))
    // }
}

artifacts {
    add(testArchive.name, jarTest)
}
