# nucleoid-api-client
A Java client for the [NucleoidMC API](https://api.nucleoid.xyz/).

## Maven coordinates
Repository:
`https://maven.tomthegeek.ml/releases/`

Artifact:
`com.github.tom_the_geek:nucleoid-api-client:<version>`

The latest version is: ![Latest release](https://img.shields.io/maven-metadata/v?label=Latest%20release&metadataUrl=https%3A%2F%2Fmaven.tomthegeek.ml%2Freleases%2Fcom%2Fgithub%2Ftom_the_geek%2Fnucleoid-api-client%2Fmaven-metadata.xml)

### Gradle (default buildscript DSL)
```groovy
repositories {
    mavenCentral()
    maven {
        name = "TomTheGeek-repo"
        url = "https://maven.tomthegeek.ml/releases/"
    }
}

dependencies {
    implementation "com.github.tom_the_geek:nucleoid-api-client:<version>"
}
```

### Gradle (Kotlin buildscript DSL)
```kotlin
repositories {
    mavenCentral()
    maven {
        name = "TomTheGeek-repo"
        url = uri("https://maven.tomthegeek.ml/releases/")
    }
}

dependencies {
    implementation("com.github.tom_the_geek:nucleoid-api-client:<version>")
}
```

## Usage
```java
public class Example {
    public static void main(String[] args) {
        NucleoidApiClient client = NucleoidApiClient.builder()
                // Configure client here.
                .build();
        client.getServerStatus("play").handle((status, throwable) -> {
            if (status != null) {
                System.out.println(status);
            } else if (throwable != null) {
                throwable.printStackTrace();
            }

            return null;
        });
    }
}
```
