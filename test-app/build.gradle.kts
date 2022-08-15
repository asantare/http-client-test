plugins {
    id("org.jetbrains.kotlin.jvm") version "1.6.21"
    id("org.jetbrains.kotlin.kapt") version "1.6.21"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.21"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("io.micronaut.application") version "3.5.1"
}

version = "0.1"
group = "com.example"

val kotlinVersion = project.properties.get("kotlinVersion")
repositories {
    mavenCentral()
}

dependencies {
    kapt("io.micronaut:micronaut-http-validation")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-jackson-databind")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")

    implementation("io.micronaut.redis:micronaut-redis-lettuce")


    implementation("jakarta.annotation:jakarta.annotation-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect:${kotlinVersion}")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:${kotlinVersion}")
    runtimeOnly("ch.qos.logback:logback-classic")
    compileOnly("org.graalvm.nativeimage:svm")

    implementation("io.micronaut:micronaut-validation")

    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

}

configurations.all {
    resolutionStrategy {
        // lettuce 5.2.2.RELEASE -> OK
//        eachDependency {
//            if (this.requested.group == "io.lettuce") {
//                this.useVersion("6.1.1.RELEASE")
//            }
//        }
    }
}


application {
    mainClass.set("com.example.ApplicationKt")
}
java {
    sourceCompatibility = JavaVersion.toVersion("11")
}

tasks {
    compileKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
    compileTestKotlin {
        kotlinOptions {
            jvmTarget = "11"
        }
    }
}
graalvmNative {
    toolchainDetection.set(false)
    binaries {
        named("main") {
            imageName.set("application")
            buildArgs.add("--verbose")
//            buildArgs.add("-g")
//            buildArgs.add("-H:+PrintClassInitialization")
            buildArgs.add("-H:+AllowVMInspection")
//            buildArgs.add("-J-Dio.lettuce.core.epoll=false")
//            buildArgs.add("-J-Dio.lettuce.core.kqueue=false")
        }
    }
}
micronaut {
    runtime("netty")
    version(project.properties.get("micronautVersion") as String)
    testRuntime("kotest")
    processing {
        incremental(true)
        annotations("com.example.*")
    }
}