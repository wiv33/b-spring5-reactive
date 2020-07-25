dependencies {
    extra["lombokVersion"] = "1.18.8"
    extra["jupiterVersion"] = "5.6.2"
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testCompileOnly("org.projectlombok:lombok:${extra["lombokVersion"]}")
    testImplementation("org.projectlombok:lombok:${extra["lombokVersion"]}")
    annotationProcessor("org.projectlombok:lombok:${extra["lombokVersion"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-api:${extra["jupiterVersion"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:${extra["jupiterVersion"]}")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.slf4j:slf4j-api")
}