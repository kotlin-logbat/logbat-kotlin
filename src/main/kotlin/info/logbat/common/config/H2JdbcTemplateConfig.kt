//package info.logbat.common.config
//
//import com.zaxxer.hikari.HikariDataSource
//import info.logbat.domain.log.repository.AsyncMultiProcessor
//import org.springframework.beans.factory.annotation.Value
//import org.springframework.context.annotation.Bean
//import org.springframework.context.annotation.Configuration
//import org.springframework.context.annotation.DependsOn
//import org.springframework.jdbc.core.JdbcTemplate
//import org.springframework.jdbc.datasource.DriverManagerDataSource
//import javax.sql.DataSource
//
//@Configuration
//class H2JdbcTemplateConfig(
//    @Value("\${spring.datasource.hikari.driver-class-name}") private val driverClassName: String,
//    @Value("\${spring.datasource.hikari.jdbc-url}") private val jdbcUrl: String,
//    @Value("\${spring.datasource.hikari.username}") private val username: String,
//    @Value("\${spring.datasource.hikari.password}") private val password: String
//) {
//
//    @Bean
//    fun h2DataSource(): HikariDataSource {
//        val dataSource = HikariDataSource()
//        dataSource.setDriverClassName(driverClassName)
//        dataSource.jdbcUrl = jdbcUrl
//        dataSource.username = username
//        dataSource.password = password
//        return dataSource
//    }
//
//    @Bean
//    fun jdbcTemplate(dataSource: DataSource): JdbcTemplate {
//        return JdbcTemplate(dataSource)
//    }
//}