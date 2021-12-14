package com.example.micronuaut.spring

import io.micronaut.runtime.Micronaut
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Main application object
 * Started by Docker container.
 */
object Application {
    private val log: Logger = LoggerFactory.getLogger(Application::class.java)

    @JvmStatic
    fun main(vararg args: String) {
        Micronaut.build(*args)
            .mainClass(Application.javaClass)
            .banner(false)
            .start()
    }
}