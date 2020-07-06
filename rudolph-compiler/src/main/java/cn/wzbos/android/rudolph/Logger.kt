package cn.wzbos.android.rudolph

import org.apache.commons.lang3.StringUtils
import javax.annotation.processing.Messager
import javax.tools.Diagnostic

internal class Logger(private val msg: Messager) {
    fun info(info: CharSequence) {
        if (StringUtils.isNotEmpty(info)) {
            msg.printMessage(Diagnostic.Kind.NOTE, Constant.PREFIX_OF_LOGGER + info)
        }
    }

    fun error(error: CharSequence) {
        if (StringUtils.isNotEmpty(error)) {
            msg.printMessage(Diagnostic.Kind.ERROR, Constant.PREFIX_OF_LOGGER + error)
        }
    }

    fun error(error: Throwable?) {
        if (null != error) {
            msg.printMessage(Diagnostic.Kind.ERROR, "${Constant.PREFIX_OF_LOGGER}, [${error.message}]${formatStackTrace(error.stackTrace)} ".trimIndent())
        }
    }

    fun warning(warning: CharSequence) {
        if (StringUtils.isNotEmpty(warning)) {
            msg.printMessage(Diagnostic.Kind.WARNING, Constant.PREFIX_OF_LOGGER + warning)
        }
    }

    private fun formatStackTrace(stackTrace: Array<StackTraceElement>): String {
        val sb = StringBuilder()
        for (element in stackTrace) {
            sb.append("    at ").append(element.toString())
            sb.append("\n")
        }
        return sb.toString()
    }

}