package pe.com.aldesa.aduanero.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Inicializa la instancia de conexión a base de datos. Toma los valores del
 * archivo de propiedades <tt>application.yml</tt> o del archivo que se designe en forma
 * externa.
 * 
 * @author Juan Pablo Canepa Alvarez
 *
 */
@Configuration
@EnableTransactionManagement
public class DBConfig {

	@Value("${spring.datasource.url}")
	private String url;
	@Value("${spring.datasource.username}")
	private String username;
	@Value("${spring.datasource.password}")
	private String password;
	@Value("${spring.datasource.driver-class-name}")
	private String drivername;
	@Value("${spring.jpa.database-platform}")
	private String dialect;
	@Value("${spring.jpa.show-sql}")
	private String showSql;
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String ddlAuto;

	@Bean
	public LocalSessionFactoryBean sessionFactory() {
		LocalSessionFactoryBean localSessionFactory = new LocalSessionFactoryBean();
		localSessionFactory.setDataSource(getDataSource());
		localSessionFactory.setPackagesToScan("pe.com.aldesa.aduanero.entity");
		localSessionFactory.setHibernateProperties(hibernateProperties());

		return localSessionFactory;
	}

	@Bean
	public DataSource getDataSource() {
		BasicDataSource dataSource = new BasicDataSource();
		dataSource.setDriverClassName(this.drivername);
		dataSource.setUrl(this.url);
		dataSource.setUsername(this.username);
		dataSource.setPassword(this.password);

		return dataSource;
	}

	@Bean
	public PlatformTransactionManager getTransactionManager(SessionFactory sessionFactory) {
		return new HibernateTransactionManager(sessionFactory);
	}

	@Bean
	public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
		return new PersistenceExceptionTranslationPostProcessor();
	}

	private Properties hibernateProperties() {
		Properties hibernateProperties = new Properties();
		hibernateProperties.put("hibernate.dialect", this.dialect);
		hibernateProperties.put("hibernate.ddl-auto", this.ddlAuto);
		hibernateProperties.put("hibernate.show_sql", this.showSql);

		return hibernateProperties;
	}

}
