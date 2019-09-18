package ops.school.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.defaults.DefaultSqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.IOException;

/**
 * CreatebyFang
 * fangfor@outlook.com
 * 2019/9/18
 * 18:15
 * #
 */
@Configuration
public class SqlSessionConfig  {

    private Logger logger = LoggerFactory.getLogger(SqlSessionConfig.class);

    @Value("${spring.datasource}")
    private DataSource dataSource;

    @Value("${mybatis.mapper-locations}")
    private String mapperLocations;


    @Bean
    public SqlSessionFactoryBean createSqlSessionFactoryBean() {

        SqlSessionFactoryBean sqlSessionFactoryBean = null;

        try {

            // 加载JNDI配置
            // 实例SessionFactory

            sqlSessionFactoryBean = new SqlSessionFactoryBean();

            // 配置数据源

            sqlSessionFactoryBean.setDataSource(dataSource);

            // 加载MyBatis配置文件

            PathMatchingResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();

            // 能加载多个，所以可以配置通配符(如：classpath*:mapper/**/*.xml)

            sqlSessionFactoryBean.setMapperLocations(resourcePatternResolver.getResources(mapperLocations));

            // 配置mybatis的config文件(我目前用不上)

            // sqlSessionFactoryBean.setConfigLocation("mybatis-config.xml");

        } catch (Exception e) {

            logger.error("创建SqlSession连接工厂错误：{}", e);

        }

        return sqlSessionFactoryBean;

    }

    @Bean
    public SqlSessionFactory createSqlSessionFactory(){
        SqlSessionFactory sqlSessionFactory = createSqlSessionFactory();
        return sqlSessionFactory;
    }
}
