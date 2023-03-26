@file:Suppress("unused")

package mysticfall.kotlin.react.test

import js.core.jso
import react.ChildrenBuilder
import react.FC
import react.Props
import react.ReactElement
import react.create

interface ReactTestSupport {

    fun ReactTestSupport.render(block: ChildrenBuilder.() -> Unit): TestRenderer = render(null, block)

    fun ReactTestSupport.render(
        options: TestRendererOptions? = null,
        block: ChildrenBuilder.() -> Unit
    ): TestRenderer {
        val builder = FC<Props> {
            block()
        }

        return TestRendererGlobal.create(builder.create(), options)
    }

    fun ReactTestSupport.render(
        mockFactory: (ReactElement<Props>) -> Any,
        block: ChildrenBuilder.() -> Unit
    ): TestRenderer {
        val options = jso<TestRendererOptions> {
            createNodeMock = mockFactory
        }

        return render(options, block)
    }

    fun ReactTestSupport.act(block: () -> Unit): Unit = TestRendererGlobal.act(block)

    fun ReactTestSupport.update(component: TestRenderer, replacement: ChildrenBuilder.(Props) -> Unit) {
        val builder = FC<Props> {
            replacement(it)
        }

        component.update(builder.create())
    }

}
