import org.gradle.api.tasks.JavaExec

plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ksp)
    application
}

dependencies {
    implementation(libs.adk.core)
    implementation(libs.adk.webserver)
    ksp(libs.adk.processor)
}

kotlin {
    jvmToolchain(17)
}

application {
    mainClass.set(
        project.findProperty("mainClass") as? String
            ?: "com.example.agent.MainKt"
    )
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}
