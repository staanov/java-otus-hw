plugins {
	java
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "io.github.staanov"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_21
}

allprojects {
	repositories {
		mavenLocal()
		mavenCentral()
	}

	apply(plugin = "io.spring.dependency-management")
	dependencyManagement {
		dependencies {
			dependency("org.springframework.boot:spring-boot-starter:3.2.1")
			dependency("com.google.guava:guava:33.0.0-jre")
			dependency("org.springframework.boot:spring-boot-starter-test:3.2.1")
			dependency("org.assertj:assertj-core:3.25.3")
			dependency("org.mockito:mockito-core:5.10.0")
			dependency("com.google.code.gson:gson:2.10.1")
			dependency("ch.qos.logback:logback-classic:1.5.3")
			dependency("org.flywaydb:flyway-core:9.22.3")
			dependency("com.zaxxer:HikariCP:5.1.0")
			dependency("org.postgresql:postgresql:42.7.3")
		}
	}
}

subprojects {
	apply(plugin = "java")
	apply(plugin = "org.springframework.boot")
}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.bootJar {
	enabled = false
}
