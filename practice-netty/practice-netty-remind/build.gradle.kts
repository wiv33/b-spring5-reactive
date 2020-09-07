dependencies {
    extra["jupiterVersion"] = "5.6.2"

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${extra["jupiterVersion"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:${extra["jupiterVersion"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-params:${extra["jupiterVersion"]}")
}