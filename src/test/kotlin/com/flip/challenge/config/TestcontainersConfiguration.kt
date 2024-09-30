package com.flip.challenge.config

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    fun mysqlContainer(): MySQLContainer<*> {
        return MySQLContainer(DockerImageName.parse("mysql:9.0.1")).apply {
            withDatabaseName("challenge_db")
            withUsername("root")
            withPassword("root")
            withReuse(true)
            withTmpFs(mapOf("/var/lib/mysql" to "rw"))
            withLabel("reuseId", "challenge_mysql")
        }
    }

}
