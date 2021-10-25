package com.nick.mvvm

import java.io.ByteArrayOutputStream
import java.io.PrintStream

class UEHandler : Thread.UncaughtExceptionHandler {
    override fun uncaughtException(thread: Thread, ex: Throwable) {
        val info = StringBuilder()
        var baos: ByteArrayOutputStream? = null
        var printStream: PrintStream? = null
        try {
            baos = ByteArrayOutputStream()
            printStream = PrintStream(baos)
            ex.printStackTrace(printStream)
            val data = baos.toByteArray()
            info.append(String(data))
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                printStream?.close()
                baos?.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
        val threadId = thread.id
//        LogVast.i("Thread.getName()=" + thread.name + " id=" + threadId + " state=" + thread.state + ", info = " + info.toString())
//        info.append("\n").append("threadId=$threadId")

        write2ErrorLog(info.toString())
        try {
            Thread.sleep(500)
        } catch (e: Exception) {
        }

    }


    private fun write2ErrorLog(content: String) {
//        val file = FileUtils.createFolder(ContextUtils.getContext(), "errorLog")
//        val fileErrorLog = File(file, System.currentTimeMillis().toString() + ".log")
//        if (fileErrorLog.exists()) {
//            fileErrorLog.delete()
//        } else {
//            fileErrorLog.parentFile.mkdirs()
//        }
//
//        var fos: FileOutputStream? = null
//        try {
//            fileErrorLog.createNewFile()
//            fos = FileOutputStream(fileErrorLog)
//            fos.write(content.toByteArray())
//        } catch (e: Exception) {
//            e.printStackTrace()
//        } finally {
//            FileUtils.closeQuietly(fos)
//        }
    }


}
