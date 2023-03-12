package mysticfall.kotlin.react.test

import csstype.ClassName
import kotlinx.js.jso
import react.*
import react.dom.html.ReactHTML.div
import react.dom.html.ReactHTML.h1

external interface TestProps : Props {
    var name: String
}

external interface TestState : State {
    var name: String
}

val TestFuncComponent = FC<TestProps> { props ->
    val (name, setName) = useState(props.name)

    useEffect(name) {
        setName("Updated: ${props.name}")
    }

    div {
        className = ClassName("test-component")
        h1 {
            className = ClassName("title")
            +name
        }
    }
}

class TestClassComponent(props: TestProps) : Component<TestProps, TestState>(props) {

    init {
        state = jso { name = props.name }
    }

    override fun componentDidMount() {
        setState({
            it.name = "Updated: ${props.name}"
            it
        })
    }

    override fun render() = FC<TestProps> {
        div {
            className = ClassName("test-component")
            h1 {
                className = ClassName("title")
                +it.name
            }
        }
    }.create { name = state.name }
}
