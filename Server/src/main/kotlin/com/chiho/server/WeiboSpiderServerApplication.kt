package com.chiho.server

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class WeiboSpiderServerApplication

fun main(args: Array<String>) {
    runApplication<WeiboSpiderServerApplication>(*args)
}
