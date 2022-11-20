import com.github.gradle.node.npm.task.NpmTask

plugins {
    id("com.github.node-gradle.node")
}

apply {
    plugin("base")
    plugin("com.github.node-gradle.node")
}

node {
    download.set(true)
    version.set("16.14.0")

    workDir.set(file("${project.projectDir}/.cache/nodejs"))
    npmWorkDir.set(file("${project.projectDir}/.cache/npm"))
}

val generateApiLib = tasks.register<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("generateApiLib") {
    generatorName.set("typescript-fetch")

    inputSpec.set("${project.projectDir}/../specifications/api.yml")

    // Todo: use dir outside of src root, which will require 'craco';
    // Now use a dir in 'src' for sake of simplicity
    outputDir.set("${project.projectDir}/src/generated/api")
}


// This should prevent unnecessary re-builds; only when a source file
// has changed
tasks.named<NpmTask>("npm_run_build") {
    dependsOn(generateApiLib)
    inputs.files(fileTree("public"))
    inputs.files(fileTree("src"))
    inputs.file("package.json")
    inputs.file("package-lock.json")
    outputs.dir("build")
}

val packageNpmApp = tasks.register<Zip>("packageNpmApp") {
    dependsOn(tasks["npm_run_build"], generateApiLib)

    archiveBaseName.set("solar-charging-simulator-gui")
    archiveExtension.set("jar")
    destinationDirectory.set(file("${project.projectDir}/dist"))

    from("build") {
        into("static")
    }
}

val npmResources by configurations.creating
configurations.named("default").get().extendsFrom(npmResources)
artifacts {
    add(npmResources.name, packageNpmApp.get().archiveFile) {
        builtBy(packageNpmApp)
        type = "jar"
    }
}
tasks["assemble"].dependsOn(packageNpmApp)

val testApp = tasks.register<NpmTask>("testApp") {
    dependsOn("assemble")

    environment.set(mapOf("CI" to "true"))
    args.set(listOf("run", "test"))

    inputs.files(fileTree("src"))
    inputs.file("package.json")
    inputs.file("package-lock.json")
}

// Todo: Disabled, the jest test runner reports:
//  import {Chart, LinearScale, CategoryScale, PointElement, LineElement, ChartOptions, Title, Tooltip} from "chart.js";
//  SyntaxError: Cannot use import statement outside a module
//tasks["check"].dependsOn(testApp)

tasks["clean"].doFirst {
    delete("src/generated") // rootProject.buildDir
    delete("node_modules")
    delete("dist")
}



