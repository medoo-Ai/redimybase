package com.test;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import org.junit.Test;

/**
 * Created by Irany 2018/6/19 12:53
 */
public class GeneratorTest {

    @Test
    public void generateCode() {
        String packageName = "com.aispread.manager.device";      //包路径
        boolean serviceNameStartWithI = false;//user -> UserService, 设置成true: user -> IUserService
        generateByTables(false, "t_", packageName,
                "t_device_type");
//        ,t_unit,t_brand,t_device_type,t_device
    }

    private void generateByTables(boolean serviceNameStartWithI, String tablePrefix, String packageName, String... tableNames) {
        GlobalConfig config = new GlobalConfig();
        String dbUrl = "jdbc:mysql://123.206.207.161:3306/dev_redi_db?serverTimezone=GMT%2B8";
        DataSourceConfig dataSourceConfig = new DataSourceConfig();
        dataSourceConfig
                .setUrl(dbUrl)
                .setDbType(DbType.MYSQL)
                .setUsername("dev_redi_db")
                .setPassword("Hellokitty666")
                .setDriverName("com.mysql.jdbc.Driver");
        StrategyConfig strategyConfig = new StrategyConfig();
        strategyConfig
                .setTablePrefix(tablePrefix)
                .setCapitalMode(true)
                .entityTableFieldAnnotationEnable(true)
                .setEntityLombokModel(true)
                .setControllerMappingHyphenStyle(true)
                .setNaming(NamingStrategy.underline_to_camel)
                .setInclude(tableNames);//修改替换成你需要的表名，多个表名传数组
        config.setActiveRecord(false)
                .setAuthor("Mr.D")
                .setOutputDir("d:\\codeGen")
                .setFileOverride(true);
        if (!serviceNameStartWithI) {
            config.setServiceName("%sService");
        }
        new AutoGenerator().setGlobalConfig(config)
                .setDataSource(dataSourceConfig)
                .setStrategy(strategyConfig)
                .setPackageInfo(
                        new PackageConfig()
                                .setParent(packageName)
                                .setEntity("entity")
                ).execute();
    }

    private void generateByTables(String packageName, String tablePrefix, String... tableNames) {
        generateByTables(true, packageName, tablePrefix, tableNames);
    }
}
