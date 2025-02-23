package cyberflame.kotlin.react.test

import react.Props
import react.ReactElement

external interface TestRendererOptions {

    var createNodeMock: ((ReactElement<Props>) -> Any)?
}
