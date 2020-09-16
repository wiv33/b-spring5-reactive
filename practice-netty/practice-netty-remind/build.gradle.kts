dependencies {
    extra["jupiterVersion"] = "5.6.2"
    extra["lombokVersion"] = "1.18.8"


    annotationProcessor("org.projectlombok:lombok:${extra["lombokVersion"]}")
    testImplementation("org.projectlombok:lombok:${extra["lombokVersion"]}")

    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.security:spring-security-core")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${extra["jupiterVersion"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:${extra["jupiterVersion"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${extra["jupiterVersion"]}")
}