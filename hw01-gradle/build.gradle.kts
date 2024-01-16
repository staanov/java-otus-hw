tasks.bootJar {
    enabled = true
    mainClass.set("io.github.staanov.hw01gradle.Hw01GradleApplication")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation ("com.google.guava:guava")
    implementation("org.springframework.boot:spring-boot-starter-test")
}
