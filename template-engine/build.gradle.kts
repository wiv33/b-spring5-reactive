dependencies {
    extra["jupiterVersion"] = "5.6.2"

    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-webflux")


    testImplementation("org.junit.jupiter:junit-jupiter-api:${extra["jupiterVersion"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:${extra["jupiterVersion"]}")

    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    testImplementation("io.projectreactor:reactor-test")

}