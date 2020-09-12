plugins {
    java
    id("org.springframework.boot") version "2.3.3.RELEASE"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"

}
allprojects {
    apply(plugin = "java")
}

subprojects {
    apply(plugin = "org.springframework.boot")
    apply(plugin = "io.spring.dependency-management")
    group = "org.psawesome"
    version = "1.0.0-SNAPSHOT"
    java.sourceCompatibility = JavaVersion.VERSION_13

    extra["springCloudVersion"] = "Hoxton.SR8"

    configurations {
        compileOnly {
            extendsFrom(configurations.annotationProcessor.get())
        }
    }

    repositories {
        mavenCentral()
        maven {
            url = uri("http://repo.mycompany.com/repo")
            metadataSources {
                mavenPom()
                artifact()
                ignoreGradleMetadataRedirection()
            }
        }
    }

    dependencyManagement {
        imports {
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:${extra["springCloudVersion"]}")
        }
    }
    tasks.withType(Test::useJUnitPlatform)
}

project(":temperature") {
    dependencies {
        implementation("io.reactivex:rxjava:1.3.8")
        implementation("org.springframework.boot:spring-boot-starter-actuator")
        implementation("org.springframework.boot:spring-boot-starter-web")
        testImplementation("org.springframework.boot:spring-boot-starter-test") {
            exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
        }

        testImplementation("org.junit.jupiter:junit-jupiter-api:5.6.2")
        testImplementation("org.junit.jupiter:junit-jupiter-engine:5.6.2")
        testImplementation("io.projectreactor:reactor-test")
    }
}
