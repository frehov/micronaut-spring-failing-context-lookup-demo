package com.example.micronuaut.spring

import io.micronaut.context.annotation.ConfigurationInject
import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Context
import io.micronaut.context.annotation.EachProperty
import io.micronaut.context.annotation.Parameter
import java.net.URI

@Context
@EachProperty("application.configs")
class DemoConfig(
    @param:Parameter val serviceId: String,
) {
    lateinit var jms: JmsConfig

    @ConfigurationProperties("jms")
    data class JmsConfig @ConfigurationInject constructor(
        val topicName: String,
        val brokerUrl: URI,
    )

    override fun toString(): String {
        return "DemoConfig(serviceId='$serviceId', jms=$jms)"
    }

}