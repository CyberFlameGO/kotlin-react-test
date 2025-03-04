@file:Suppress("unused")

package cyberflame.kotlin.react.test

import react.Component
import react.ReactNode
import kotlin.js.Json

external interface TestRenderer {

    val root: TestInstance<*>

    fun toJSON(): Json

    fun toTree(): Json

    fun update(node: ReactNode)

    fun unmount()

    fun getInstance(): Component<*, *>?
}

/**
 * Created to swap root target
 */
class RootFixedTestRenderer(private val testRenderer: TestRenderer): TestRenderer by testRenderer {
    override val root: TestInstance<*> by lazy { testRenderer.root.children.first() }
}

@JsModule("react-test-renderer")
@JsNonModule
external val renderer: dynamic

//TODO Until we have https://youtrack.jetbrains.com/issue/KT-39602
object TestRendererGlobal {

    fun create(node: ReactNode): TestRenderer = renderer.create(node).unsafeCast<TestRenderer>().fix()

    fun create(node: ReactNode, options: dynamic): TestRenderer =
        renderer.create(node, options).unsafeCast<TestRenderer>().fix()

    fun act(block: () -> Any) {
        val callback: () -> Nothing? = {
            block()

            undefined
        }

        renderer.act(callback)
    }

    private fun TestRenderer.fix(): TestRenderer = RootFixedTestRenderer(this)
}
