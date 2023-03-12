![Build](https://img.shields.io/github/actions/workflow/status/turtton/kotlin-react-test/publish.yml?style=flat-square)
<img alt="Maven metadata URL" src="https://img.shields.io/maven-metadata/v?metadataUrl=https%3A%2F%2Fmaven.turtton.net%2Fio%2Fgithub%2Fmysticfall%2Fkotlin-react-test%2Fmaven-metadata.xml&style=flat-square">

# Kotlin API for React Test Renderer

Kotlin wrapper for [React Test Renderer](https://reactjs.org/docs/test-renderer.html), 
which can be used to unit test React components in a Kotlin/JS project.

## How to Use

### Installation

With Gradle (using Kotlin DSL):
```kotlin
repositories {
    maven("https://maven.turtton.net")
}

dependencies {
    implementation("io.github.mysticfall:kotlin-react-test:$version")
}
```

Alternatively, using Groovy DSL:

```groovy
repositories {
    maven "https://maven.turtton.net"
}

dependencies {
    implementation "io.github.mysticfall:kotlin-react-test:$version"
}
```

### Code Example

The most straightforward way of using the library is to make your test class implement 
`ReactTestSupport`, as shown below:

```kotlin
import mysticfall.kotlin.react.test.ReactTestSupport

class ComponentTest : ReactTestSupport {

    @Test
    fun testHeaderTitle() {
        val renderer = render {
            HeaderTitle {
                title = "Kotlin/JS"
            }
        }

        val title = renderer.root.findByType(HeaderTitle)

        assertEquals("Kotlin/JS", title.props.title)
    }
}
```

The project itself has quite an extensive set of test cases, which can serve as examples that show 
how various features of React Test Renderer can be used in Kotlin.

## LICENSE

This project is provided under the terms of [MIT License](LICENSE).
