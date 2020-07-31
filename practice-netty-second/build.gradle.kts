dependencies {
    extra["jupiterVersion"] = "5.6.2"
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.security:spring-security-core")

    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit.vintage.engine")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api:${extra["jupiterVersion"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:${extra["jupiterVersion"]}")
}