package ru.ustimov.weather.util

private const val CALLER_INDEX: Int = 1

fun Throwable?.println(logger: Logger) {
    val stackTraceThrowable = Throwable()
    val traces = stackTraceThrowable.stackTrace
    if (traces?.size ?: 0 > CALLER_INDEX + 1) {
        val trace = traces[CALLER_INDEX]

        val fileName: String? = trace.fileName
        val tag: String = if (fileName.isNullOrEmpty()) trace.className else fileName.orEmpty()

        val lineNumber = if (trace.lineNumber > 0) " at line ${trace.lineNumber}" else ""
        val message = "Exception in ${trace.className}.${trace.methodName}$lineNumber"
        logger.e(tag, message, this)

    } else {
        logger.e("Throwables", this?.message.orEmpty(), this);
    }
}