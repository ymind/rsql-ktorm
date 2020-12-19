import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import team.yi.gradle.plugin.FileSet

object Constants {
    const val gitUrl = "github.com"
    const val gitProjectUrl = "ymind/rsql-ktorm"

    const val projectVersion = "0.2.0-SNAPSHOT"
}

plugins {
    java
    `maven-publish`
    signing

    kotlin("jvm")

    // https://plugins.gradle.org/plugin/org.jlleitschuh.gradle.ktlint
    id("org.jlleitschuh.gradle.ktlint") version "9.4.1"
    // https://plugins.gradle.org/plugin/team.yi.semantic-gitlog
    id("team.yi.semantic-gitlog") version "0.5.17"

    id("se.patrikerdes.use-latest-versions") version "0.2.15"
    id("com.github.ben-manes.versions") version "0.36.0"

    id("io.gitlab.arturbosch.detekt") version "1.15.0"
}

group = "team.yi.rsql"
version = Constants.projectVersion
description = "Integration RSQL query language and Querydsl framework."

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8

    withJavadocJar()
    withSourcesJar()
}

subprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")
    apply(plugin = "java-library")
    apply(plugin = "kotlin")

    version = rootProject.version

    dependencies {
        implementation(kotlin("stdlib-jdk8"))
        implementation("org.jetbrains.kotlin:kotlin-reflect")

        testImplementation(platform("org.junit:junit-bom:5.7.1"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testImplementation("cn.hutool:hutool-all:5.5.8")
        testImplementation("org.apache.logging.log4j:log4j-slf4j-impl:2.14.0")
    }

    tasks {
        val kotlinSettings: KotlinCompile.() -> Unit = {
            kotlinOptions.jvmTarget = JavaVersion.VERSION_1_8.toString()
            kotlinOptions.freeCompilerArgs += listOf(
                "-Xjsr305=strict"
            )
        }

        compileKotlin(kotlinSettings)
        compileTestKotlin(kotlinSettings)
        compileJava { options.encoding = "UTF-8" }
        compileTestJava { options.encoding = "UTF-8" }
        javadoc { options.encoding = "UTF-8" }

        jar { enabled = true }
        test { useJUnitPlatform() }
    }
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "com.github.ben-manes.versions")
    apply(plugin = "se.patrikerdes.use-latest-versions")
    apply(plugin = "io.gitlab.arturbosch.detekt")

    ktlint {
        debug.set(false)
        outputToConsole.set(true)
        outputColorName.set("RED")
        ignoreFailures.set(false)
        enableExperimentalRules.set(true)

        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }

    detekt {
        failFast = true
        buildUponDefaultConfig = false
        config = files("$rootDir/config/detekt/detekt.yml")
        baseline = file("$rootDir/config/detekt/baseline.xml")

        reports {
            html.enabled = true
            xml.enabled = true
            txt.enabled = true
        }
    }

    repositories {
        mavenLocal()
        jcenter()
        mavenCentral()

        // maven {
        //     setUrl("https://maven.aliyun.com/repository/public")
        // }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8

        withJavadocJar()
        withSourcesJar()
    }

    if (project == rootProject) return@allprojects

    rootProject.evaluationDependsOnChildren()

    publishing {
        publications {
            register("mavenJava", MavenPublication::class) {
                from(components["java"])

                versionMapping {
                    usage("java-api") {
                        fromResolutionOf("runtimeClasspath")
                    }
                    usage("java-runtime") {
                        fromResolutionResult()
                    }
                }

                pom {
                    group = rootProject.group
                    name.set(project.name)
                    description.set(project.description)
                    url.set("https://${Constants.gitUrl}/${Constants.gitProjectUrl}")
                    inceptionYear.set("2020")

                    scm {
                        url.set("https://${Constants.gitUrl}/${Constants.gitProjectUrl}")
                        connection.set("scm:git:git@${Constants.gitUrl}:${Constants.gitProjectUrl}.git")
                        developerConnection.set("scm:git:git@${Constants.gitUrl}:${Constants.gitProjectUrl}.git")
                    }

                    licenses {
                        license {
                            name.set("MIT")
                            url.set("https://opensource.org/licenses/MIT")
                            distribution.set("repo")
                        }
                    }

                    organization {
                        name.set("Yi.Team")
                        url.set("https://yi.team/")
                    }

                    developers {
                        developer {
                            name.set("ymind")
                            email.set("ymind@yi.team")
                            url.set("https://yi.team/")
                            organization.set("Yi.Team")
                            organizationUrl.set("https://yi.team/")
                        }
                    }

                    issueManagement {
                        system.set("GitHub")
                        url.set("https://${Constants.gitUrl}/${Constants.gitProjectUrl}/issues")
                    }

                    ciManagement {
                        system.set("GitHub")
                        url.set("https://${Constants.gitUrl}/${Constants.gitProjectUrl}/actions")
                    }
                }
            }
        }

        repositories {
            maven {
                val releasesRepoUrl = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2")
                val snapshotsRepoUrl = uri("https://oss.sonatype.org/content/repositories/snapshots")

                url = if (version.toString().endsWith("SNAPSHOT", true)) snapshotsRepoUrl else releasesRepoUrl

                credentials {
                    username = System.getenv("OSSRH_USERNAME") ?: "${properties["OSSRH_USERNAME"]}"
                    password = System.getenv("OSSRH_TOKEN") ?: "${properties["OSSRH_TOKEN"]}"
                }
            }
        }
    }

    signing {
        val secretKeyRingFile = System.getenv("OSSRH_GPG_SECRET_KEY") ?: "${properties["OSSRH_GPG_SECRET_KEY"]}"

        extra.set("signing.keyId", System.getenv("OSSRH_GPG_SECRET_ID") ?: "${properties["OSSRH_GPG_SECRET_ID"]}")
        extra.set("signing.secretKeyRingFile", "${rootDir.absolutePath}/$secretKeyRingFile")
        extra.set("signing.password", System.getenv("OSSRH_GPG_SECRET_PASSWORD") ?: "${properties["OSSRH_GPG_SECRET_PASSWORD"]}")

        sign(publishing.publications.getByName("mavenJava"))
    }
}

tasks {
    ktlintFormat {
        outputs.upToDateWhen { false }
    }

    ktlintCheck {
        outputs.upToDateWhen { false }
    }

    val gitlogFileSets = setOf(
        FileSet(
            file("${rootProject.rootDir}/config/gitlog/CHANGELOG.md.mustache"),
            file("${rootProject.rootDir}/CHANGELOG.md")
        ),
        FileSet(
            file("${rootProject.rootDir}/config/gitlog/CHANGELOG_zh-cn.md.mustache"),
            file("${rootProject.rootDir}/CHANGELOG_zh-cn.md")
        )
    )
    val gitlogLocaleProfiles = mapOf(
        "zh-cn" to file("${rootProject.rootDir}/config/gitlog/commit-locales_zh-cn.md")
    )

    changelog {
        group = "gitlog"

        toRef = "main"
        preRelease = "SNAPSHOT"

        issueUrlTemplate = "https://${Constants.gitUrl}/${Constants.gitProjectUrl}/issues/:issueId"
        commitUrlTemplate = "https://${Constants.gitUrl}/${Constants.gitProjectUrl}/commit/:commitId"
        mentionUrlTemplate = "https://${Constants.gitUrl}/:username"
        // jsonFile = file("${rootProject.rootDir}/CHANGELOG.json")
        fileSets = gitlogFileSets
        commitLocales = gitlogLocaleProfiles

        outputs.upToDateWhen { false }
    }

    derive {
        group = "gitlog"

        toRef = "main"
        derivedVersionMark = "NEXT_VERSION:=="
        preRelease = "SNAPSHOT"
        commitLocales = gitlogLocaleProfiles

        outputs.upToDateWhen { false }
    }
}

tasks.register("bumpVersion") {
    group = "gitlog"

    dependsOn(":changelog")

    doLast {
        var newVersion = rootProject.findProperty("newVersion") as? String

        if (newVersion.isNullOrEmpty()) {
            // ^## ([\d\.]+(-SNAPSHOT)?) \(.+\)$
            val changelogContents = file("${rootProject.rootDir}/CHANGELOG.md").readText()
            val versionRegex = Regex("^## ([\\d\\.]+(-SNAPSHOT)?) \\(.+\\)\$", setOf(RegexOption.MULTILINE))
            val changelogVersion = versionRegex.find(changelogContents)?.groupValues?.get(1)

            changelogVersion?.let { newVersion = it }

            logger.warn("changelogVersion: {}", changelogVersion)
            logger.warn("newVersion: {}", newVersion)
        }

        newVersion?.let {
            logger.info("Set Project to new Version $it")

            val contents = buildFile.readText()
                .replaceFirst("const val projectVersion = \"$version\"", "const val projectVersion = \"$it\"")

            buildFile.writeText(contents)
        }
    }
}
