package com.nick.mvvm.io.request

import android.graphics.Bitmap.CompressFormat
import com.nick.mvvm.io.M3SDK
import com.nick.mvvm.io.util.Md5Util
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.util.*

/**
 * MulPart 图片、文件、音视频等资源上传
 */
class MulPart<T> @JvmOverloads constructor(url: String, tClass: Class<T>? = null) : Request<T>(url, tClass) {
    private val fileTypes = ArrayList<FileType>()

    open fun putImage(f: File): MulPart<T> {
        return putImage(f.name, f)
    }

    open fun putImage(fName: String?, f: File): MulPart<T> {
        fileTypes.add(ImageType(fName, f))
        return this
    }

    open fun putVideo(f: File): MulPart<T> {
        return putVideo(f.name, f)
    }

    open fun putVideo(fName: String?, f: File): MulPart<T> {
        fileTypes.add(VideoType(fName, f))
        return this
    }

    open fun putAudio(f: File): MulPart<T> {
        return putAudio(f.name, f)
    }

    open fun putAudio(fName: String?, f: File): MulPart<T> {
        fileTypes.add(AudioType(fName, f))
        return this
    }

    override fun generateKey(): String {
        val builder = StringBuilder(url)
        builder.append(params.toString())
        if (fileTypes.size > 0) {
            builder.append(fileTypes.toString())
        }
        return Md5Util.gen(builder.toString())
    }

    override suspend fun sendBody(): String {
        val partArray = ArrayList<MultipartBody.Part>()
        val fileTypes = fileTypes
        val size = fileTypes.size

        for (i in 0 until size) {
            val fileType = fileTypes[i]
            var key: String? = null
            var mediaType: MediaType? = null
            when (fileType) {
                is ImageType -> {
                    key = "image-" + System.currentTimeMillis()
                    mediaType = if (parseFileFormat(fileType.file.absolutePath) == CompressFormat.PNG) {
                        "image/png".toMediaTypeOrNull()
                    } else {
                        "image/jpeg".toMediaTypeOrNull()
                    }
                }
                is AudioType -> {
                    key = "audio-" + System.currentTimeMillis()
                    mediaType = "audio/*".toMediaTypeOrNull()
                }
                is VideoType -> {
                    key = "video-" + System.currentTimeMillis()
                    mediaType = "video/*".toMediaTypeOrNull()
                }
            }
            if (null == key || null == mediaType) {
                continue
            }
            val body = fileType.file.asRequestBody(mediaType)
            val part = MultipartBody.Part.createFormData(key, fileType.fileName, body)
            partArray.add(part)
        }

        val responseBody = M3SDK.call().post(url, partArray)
        return responseBody.string()
    }

    //    /**
    //     * 获取当前泛型对象类型
    //     *
    //     * @param o
    //     * @param i
    //     * @return
    //     */
    //    public static Type getType(Object o, int i) {
    //        Type types = o.getClass().getGenericSuperclass();
    //        if (types instanceof ParameterizedType) {
    //            ParameterizedType parameterizedType = (ParameterizedType) types;
    //            Type[] actualType = parameterizedType.getActualTypeArguments();
    //            if (actualType.length > i) {
    //                return actualType[i];
    //            }
    //        }
    //        return null;
    //    }

    private fun parseFileFormat(path: String): CompressFormat {
        val dotPos = path.lastIndexOf(".")
        if (dotPos <= 0) {
            return CompressFormat.JPEG
        }
        val ext = path.substring(dotPos + 1)
        if (ext.equals("jpg", ignoreCase = true) || ext.equals("jpeg", ignoreCase = true)) {
            return CompressFormat.JPEG
        }
        if (ext.equals("png", ignoreCase = true)) {
            return CompressFormat.PNG
        }
        return if (ext.equals("webp", ignoreCase = true)) {
            CompressFormat.WEBP
        } else CompressFormat.JPEG
    }


    private abstract class FileType protected constructor(var fileName: String?, val file: File) {
        init {
            if (fileName == null) {
                fileName = file.name
            }
        }
    }

    /**
     * 图片格式
     */
    private class ImageType(fileName: String?, file: File) : FileType(fileName, file)

    /**
     * 视频格式
     */
    private class VideoType(fileName: String?, file: File) : FileType(fileName, file)

    /**
     * 音频格式
     */
    private class AudioType(fileName: String?, file: File) : FileType(fileName, file)
}
