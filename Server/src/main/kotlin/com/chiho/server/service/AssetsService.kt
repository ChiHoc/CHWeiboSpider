package com.chiho.server.service

import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.FileUrlResource
import org.springframework.core.io.UrlResource
import org.springframework.core.io.support.ResourceRegion
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.MediaTypeFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.io.IOException
import java.lang.Long.min


/**
 * Created by IntelliJ IDEA.
 *
 * @Author: ChiHo
 * @Date: 2021/8/8
 * @Time: 11:12 上午
 */
@Service
class AssetsService {

    private val CHUNK_SIZE: Long = 1000000L

    @Value("\${server.assets.path}")
    private lateinit var assetsPath: String

    @Throws(IOException::class)
    fun getAssetRegion(
        rangeHeader: String?,
        userId: String,
        path: String?,
        filename: String,
        extension: String
    ): ResponseEntity<ResourceRegion?>? {
        return getRegion(rangeHeader ?: "bytes=0", userId, path, filename, extension)
    }

    @Throws(IOException::class)
    private fun getRegion(
        rangeHeader: String,
        userId: String,
        path: String?,
        filename: String,
        extension: String
    ): ResponseEntity<ResourceRegion?>? {
        val assetResource =
            FileUrlResource("${this.assetsPath}/${userId}/${if (path == null) "" else path + "/"}${filename}.${extension}")
        if (!assetResource.exists() || !assetResource.isReadable()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
        }
        if (extension.equals("mp4")) {
            val resourceRegion = getVideoResourceRegion(assetResource, rangeHeader)
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(
                    MediaTypeFactory.getMediaType(assetResource)
                        .orElse(MediaType.APPLICATION_OCTET_STREAM)
                )
                .body(resourceRegion)
        } else {
            val contentLength: Long = assetResource.contentLength()
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .contentType(
                    MediaTypeFactory.getMediaType(assetResource)
                        .orElse(MediaType.IMAGE_JPEG)
                )
                .body(ResourceRegion(assetResource, 0, contentLength))
        }
    }

    @Throws(IOException::class)
    private fun getVideoResourceRegion(
        video: UrlResource, rangeHeader: String
    ): ResourceRegion {
        val resourceRegion: ResourceRegion?
        val contentLength: Long = video.contentLength()
        var fromRange: Long = 0
        var toRange: Long = 0
        if (rangeHeader.isNotBlank()) {
            val ranges: Array<String> =
                rangeHeader.substring("bytes=".length).split("-".toRegex()).toTypedArray()
            fromRange = if (ranges.size > 0 && ranges[0].isNotBlank()) {
                ranges[0].toLong()
            } else {
                0
            }
            toRange = if (ranges.size > 1 && ranges[1].isNotBlank()) {
                ranges[1].toLong()
            } else {
                contentLength - 1
            }
        }
        resourceRegion = if (fromRange > 0) {
            val rangeLength: Long = min(CHUNK_SIZE, toRange - fromRange + 1)
            ResourceRegion(video, fromRange, rangeLength)
        } else {
            val rangeLength: Long = min(CHUNK_SIZE, contentLength)
            ResourceRegion(video, 0, rangeLength)
        }
        return resourceRegion
    }
}