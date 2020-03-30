package hr.fer.zemris.resources.loader.exceptions

import java.lang.RuntimeException

sealed class LoaderException(message: String): RuntimeException(message)

class InvalidLoadingFormat(message: String) : LoaderException(message)

class UnsupportedFormatMode(message: String): LoaderException(message)