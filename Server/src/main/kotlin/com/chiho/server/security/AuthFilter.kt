package com.chiho.server.security

import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * Created by IntelliJ IDEA.
 *
 * @Author: ChiHo
 * @Date: 2021/6/14
 * @Time: 9:15 下午
 */
class AuthFilter : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val encryptToken: String? = request.getHeader("AUTH-TOKEN")
//        if (encryptToken?.let { auth(it) } == true) {
        filterChain.doFilter(request, response)
//        } else {
//            response.status = HttpStatus.FORBIDDEN.value()
//            response.writer.append(HttpStatus.FORBIDDEN.reasonPhrase)
//            response.writer.flush()
//        }
    }

    private fun auth(encryptToken: String): Boolean {
        return try {
            val token: String = CryptUtils.decryptAndBase64(encryptToken, KEY)
            val value: Array<String> = token.split("\\|".toRegex()).toTypedArray()
            if (value.size != 2 || value[0] != "WeiboSpider") {
                return false
            }
            val now = System.currentTimeMillis()
            val currTime = value[1].toLong()
            now - currTime <= EXPIRES_TIME
        } catch (e: Exception) {
            false
        }
    }

    companion object {
        private const val KEY = "TqV3fXPY"
        private const val EXPIRES_TIME = 5L * 60L * 1000L
    }
}
