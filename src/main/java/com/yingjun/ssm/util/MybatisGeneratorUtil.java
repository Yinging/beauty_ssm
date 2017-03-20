package com.yingjun.ssm.util;

import org.apache.commons.lang.ObjectUtils;
import org.apache.velocity.VelocityContext;
import org.mybatis.generator.api.MyBatisGenerator;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.internal.DefaultShellCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 代码生成类
 * Created by ZhangShuzheng on 2017/1/10.
 */
public class MybatisGeneratorUtil {

	// 模板路径
	private static String VM_PATH = "src/main/resources/template/generatorConfig.vm";
	// 项目名称
	private static String PROJECT_NAME = "yingjun.ssm";
	// 数据库名称
	private static String DATABASE_NAME = "beauty_ssm";
	//mapper sql路径
	private static String MAPPER_PATH = "mapper";
	/**
	 * 根据模板生成generatorConfig.xml文件
	 * @param
	 */
	public static void generator(
			String jdbc_driver,
			String jdbc_url,
			String jdbc_username,
			String jdbc_password) {
		String  module_path = "src/main/resources/generator.xml";
		String sql = "SELECT table_name FROM INFORMATION_SCHEMA.TABLES WHERE table_schema = '" + DATABASE_NAME+"'";
		System.out.println("========== 开始生成generatorConfig.xml文件 ==========");
		try {
			VelocityContext context= new VelocityContext();
			List<Map<String, Object>> tables = new ArrayList<Map<String,Object>>();
			Map<String, Object> table = null;

			// 查询定制前缀项目的所有表
			JdbcUtil jdbcUtil = new JdbcUtil(jdbc_driver, jdbc_url, jdbc_username, AESUtil.AESDecode(jdbc_password));
			List<Map> result = jdbcUtil.selectByParams(sql, null);
			for (Map map : result) {
				System.out.println(map.get("TABLE_NAME"));
				table = new HashMap<String,Object>();
				table.put("table_name", map.get("TABLE_NAME"));
				if(ObjectUtils.toString(map.get("TABLE_NAME")).indexOf("t_") != -1) {
					table.put("model_name", StringUtil.lineToHump(ObjectUtils.toString(map.get("TABLE_NAME")).replace("t_","")));
				} else {
					table.put("model_name", StringUtil.lineToHump(ObjectUtils.toString(map.get("TABLE_NAME"))));
				}
				tables.add(table);
			}
			jdbcUtil.release();
			String targetProject = "";
			context.put("tables", tables);
			context.put("generator_javaModelGenerator_targetPackage", "com." + PROJECT_NAME + ".entity");
			context.put("generator_sqlMapGenerator_targetPackage", MAPPER_PATH);
			context.put("generator_javaClientGenerator_targetPackage", "com." + PROJECT_NAME + ".dao");

			context.put("targetProject", targetProject);

			context.put("generator_jdbc_password", AESUtil.AESDecode(jdbc_password));
			context.put("generator_jdbc_driver",jdbc_driver);
			context.put("generator_jdbc_url",jdbc_url);
			context.put("generator_jdbc_username",jdbc_username);
			VelocityUtil.generate(VM_PATH, module_path, context);
			// 删除旧代码
			deleteDir(new File("src/main/java/com/" + PROJECT_NAME.replaceAll("\\.","/") + "/entity"));
			deleteDir(new File("src/main/java/com/" + PROJECT_NAME.replaceAll("\\.","/") +"/dao"));
			deleteDir(new File("src/main/resources/mapper"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("========== 结束生成generatorConfig.xml文件 ==========");
		System.out.println("========== 开始运行MybatisGenerator ==========");
		// 生成代码
		try {
			List<String> warnings = new ArrayList<String>();
			File configFile = new File(module_path);
			ConfigurationParser cp = new ConfigurationParser(warnings);
			Configuration config = cp.parseConfiguration(configFile);
			DefaultShellCallback callback = new DefaultShellCallback(true);
			MyBatisGenerator myBatisGenerator = new MyBatisGenerator(config, callback, warnings);
			myBatisGenerator.generate(null);
			for (String warning : warnings) {
				System.out.println(warning);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("========== 结束运行MybatisGenerator ==========");
		System.out.println("========== 开始生成 ==========");
	}

	// 递归删除非空文件夹
	public static void deleteDir(File dir) {
		if (dir.isDirectory()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i ++) {
				deleteDir(files[i]);
			}
		}
		dir.delete();
	}

}
