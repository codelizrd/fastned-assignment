plugins {
    id("org.springframework.boot")
    kotlin("jvm")
    kotlin("plugin.spring")
    id("org.openapi.generator")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("reflect"))

    implementation(project(":Common"))
    runtimeOnly(project(path = ":Gui", configuration = "npmResources"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-json")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("javax.validation:validation-api:2.0.1.Final")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")

    testImplementation("io.strikt:strikt-core:0.34.1")
}

val generateApiStubs = tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generateApiStubs") {
    generatorName.set("kotlin-spring")
    library.set("spring-boot")

    inputSpec.set("$buildDir/../../specifications/api.yml")
    outputDir.set("$buildDir/generated/server")
    apiPackage.set("com.fastned.assignment.solarchargingsimulator.rest.api")
    modelPackage.set("com.fastned.assignment.solarchargingsimulator.rest.model")
    modelNameSuffix.set("Dto")
    configOptions.set(mapOf(
        "documentationProvider" to "none",
        "useBeanValidation" to "true",
        "interfaceOnly" to "true",
        "exceptionHandler" to "false",
        "enumPropertyNaming" to "UPPERCASE",
    ))
    globalProperties.set(mapOf(
        "models" to "",
        "apis" to "",
        "modelDocs" to "false",
        "modelTests" to "false",
        "supportingFiles" to ""
    ))

    doLast {
        delete(
            fileTree(
                "$buildDir/generated/server").matching {
                exclude("src/**")
            }
        )
    }
}
tasks["compileKotlin"].dependsOn(generateApiStubs)

sourceSets {
    main {
        java {
            srcDirs("$buildDir/generated/server/src/main/kotlin/")
        }
    }
}

