package hr.fer.zemris.renderer.exceptions

import java.lang.RuntimeException

sealed class RendererExceptions(message: String) : RuntimeException(message)

class InvalidNumberOfVertices(message: String) : RendererExceptions(message)

class MeshMustHaveTexelsInformation(): RendererExceptions("")