package com.example.micronuaut.spring

import io.micronaut.context.ApplicationContext
import io.micronaut.context.event.ApplicationEventListener
import io.micronaut.context.event.StartupEvent
import io.micronaut.spring.context.MicronautApplicationContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.BeanFactoryUtils
import org.springframework.beans.factory.NoSuchBeanDefinitionException
import jakarta.inject.Singleton

@Singleton
class StartupListener(
    private val micronautContext: ApplicationContext,
    private val micronautSpringContext: MicronautApplicationContext,
) : ApplicationEventListener<StartupEvent> {
    companion object {
        val logger: Logger = LoggerFactory.getLogger(StartupListener::class.java)
    }

    override fun onApplicationEvent(event: StartupEvent) {
        // Step 1: Look up beans by type through micronaut context
        micronautContext.getBeansOfType(DemoConfig::class.java)
            .sortedBy { it.serviceId }
            .forEach {
                logger.info("Step 1: {} - {}", it.serviceId, it)
            }

        // Step 1.5: Look up a single named bean using the spring adapted context.
        logger.info("Step 1.5: {}", micronautSpringContext.getBean("app1", DemoConfig::class.java))

        try {
            // Step 2: Look up beans by type through spring adapted context
            micronautSpringContext.getBeansOfType(DemoConfig::class.java).values
                .sortedBy { it.serviceId }
                .forEach {
                    logger.info("Step 2: {} - {}", it.serviceId, it)
                }
        } catch (e: NoSuchBeanDefinitionException) {
            logger.error("Step 2: Could not look up beans through spring adapted context", e)
        }

        try {
            // Step 3: Look up beans by type through spring adapted context as input to BeanFactoryUtils
            BeanFactoryUtils.beansOfTypeIncludingAncestors(micronautSpringContext, DemoConfig::class.java)
                .values
                .sortedBy { it.serviceId }
                .forEach {
                    logger.info("Step 3: {} - {}", it.serviceId, it)
                }
        } catch (e: NoSuchBeanDefinitionException) {
            logger.error("Step 3: Could not look up beans through spring adapted context using BeanFactoryUtils", e)
        }
    }
}