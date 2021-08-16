package com.chiho.server.controller

import com.chiho.server.service.ArticleService
import com.chiho.server.service.AssetsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.core.io.support.ResourceRegion
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.util.AntPathMatcher
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.HandlerMapping
import javax.servlet.http.HttpServletRequest


/**
 * Created by IntelliJ IDEA.
 *
 * @Author: ChiHo
 * @Date: 2021/6/14
 * @Time: 4:28 下午
 */
@RestController
class WebController {

    @Autowired
    private lateinit var articleService: ArticleService

    @Autowired
    private lateinit var assetsService: AssetsService

    @RequestMapping(value = ["/"], method = [RequestMethod.GET])
    fun index(offset: Int?, limit: Int?): Map<String, Any> {
        return mapOf("data" to articleService.list(offset, limit), "code" to 200)
    }

    @RequestMapping(
        value = ["/assets/{userId}/**"],
        method = [RequestMethod.GET],
        produces = ["application/octet-stream"]
    )
    fun assets(
        @RequestHeader(value = "Range", required = false) rangeHeader: String?,
        @PathVariable userId: String,
        request: HttpServletRequest
    ): ResponseEntity<ResourceRegion?>? {

        val filepath: String = AntPathMatcher().extractPathWithinPattern(
            request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE).toString(),
            request.getServletPath()
        )

        val match = Regex("(?:(.*)/)?([^./]+)\\.(.*)").find(filepath ?: "")!!
        if (match.groupValues.size == 3) {
            val (filename, extension) = match.destructured
            return assetsService.getAssetRegion(rangeHeader, userId, null, filename, extension)
        }
        if (match.groupValues.size == 4) {
            val (path, filename, extension) = match.destructured
            return assetsService.getAssetRegion(rangeHeader, userId, path, filename, extension)
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null)
    }
}