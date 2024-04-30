plugins {
    `java-library`
    `maven-publish`
}

group = "cc.mewcraft"
version = "2.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.mewcraft.cc/releases/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper", "paper-api", "1.20.4-R0.1-SNAPSHOT")
    compileOnly("org.checkerframework", "checker-qual", "3.42.0")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.github.seeseemelk:MockBukkit-v1.20:3.86.0")
}

tasks.test {
    useJUnitPlatform()
}

java {
    withSourcesJar()
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

publishing {
    repositories {
        maven {
            name = "nyaadanbou"
            url = uri("https://repo.mewcraft.cc/releases/")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }
        }
    }

    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}
