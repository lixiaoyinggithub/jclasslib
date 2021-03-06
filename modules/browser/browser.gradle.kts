import org.gradle.api.java.archives.Manifest
import org.gradle.api.tasks.Copy
import org.gradle.jvm.tasks.Jar

plugins {
    kotlin("jvm")
    application
    `maven-publish`
}

configurePublishing()

application {
    mainClassName = "org.gjt.jclasslib.browser.BrowserApplication"
}

dependencies {
    compileOnly(":apple")
    compile("com.install4j:install4j-runtime:7.0.6")
    compile("org.jetbrains:annotations:13.0")
    compile("org.jetbrains.kotlinx:kotlinx.dom:0.0.10")
    compile("com.miglayout:miglayout-swing:5.0")
    compile(project(":data"))
}

tasks {
    val jar by existing(Jar::class) {
        archiveName = "jclasslib-browser.jar"
        manifest {
            attributes("Main-Class" to application.mainClassName)
        }
    }

    val copyDist by registering(Copy::class) {
        dependsOn("jar")
        from(configurations.compile.map { it.files.filterNot { it.name.contains("install4j") } })
        from(jar)
        into(externalLibsDir)
    }

    register("dist") {
        dependsOn(copyDist)
    }
}

