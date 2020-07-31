dependencies {
    extra["lombokVersion"] = "1.18.8"
    extra["jupiterVersion"] = "5.6.2"
    extra["reactorExtraVersion"] = "3.2.0.RELEASE"
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
    }
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.security:spring-security-core")
//    implementation("org.springframework.boot:spring-boot-autoconfigure")
//    implementation("io.projectreactor.addons:reactor-extra:${extra["reactorExtraVersion"]}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${extra["jupiterVersion"]}")
    testImplementation("org.junit.jupiter:junit-jupiter-engine:${extra["jupiterVersion"]}")
    testImplementation("io.projectreactor:reactor-test")


}