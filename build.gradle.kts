import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

buildscript {
	repositories {
		mavenCentral()
	}
}

plugins {
	id("org.springframework.boot") version "2.7.5" apply false
	id("io.spring.dependency-management") version "1.0.15.RELEASE" apply false
	kotlin("jvm") version "1.6.21" apply false
	kotlin("plugin.spring") version "1.6.21" apply false

	id("org.openapi.generator") version "6.2.0" apply false
	id("com.github.node-gradle.node") version "3.5.0" apply false
}

allprojects {
	group = "com.fastned.assignment"
	version = "0.0.1"
}

subprojects {
	repositories {
		mavenCentral()
	}

	apply {
		plugin("io.spring.dependency-management")
	}

	tasks.withType<JavaCompile> {
		sourceCompatibility = "17"
		targetCompatibility = "17"
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "17"
		}
	}

	tasks.withType<Test> {
		useJUnitPlatform()
	}
}

