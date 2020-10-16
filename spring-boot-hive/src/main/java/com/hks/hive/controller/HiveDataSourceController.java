package com.hks.hive.controller;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.DataAccessException;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

/**
 * 使用 DataSource 操作 Hive
 */
@RestController
@RequestMapping("/hive")
@Api(tags = "大数据-【HIVE】相关接口", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
public class HiveDataSourceController {

	private static final Logger logger = LoggerFactory.getLogger(HiveDataSourceController.class);

//	@Autowired
//	@Qualifier("hiveJdbcDataSource")
//	org.apache.tomcat.jdbc.pool.DataSource jdbcDataSource;

	@Autowired
	@Qualifier("hiveDruidDataSource")
	DataSource druidDataSource;

	/**
	 * 列举当前Hive库中的所有数据表
	 */
	@GetMapping("/table/list")
	@ApiOperation(value = "列举当前Hive库中的所有数据表")
	public List<String> listAllTables() throws SQLException {
		List<String> list = new ArrayList<String>();
		// Statement statement = jdbcDataSource.getConnection().createStatement();
		Statement statement = druidDataSource.getConnection().createStatement();
		String sql = "show tables";
		logger.info("Running: " + sql);
		ResultSet res = statement.executeQuery(sql);
		while (res.next()) {
			list.add(res.getString(1));
		}
		return list;
	}

	/**
	 * 查询Hive库中的某张数据表字段信息
	 */
	@GetMapping("/table/describe")
	@ApiOperation(value = "查询Hive库中的某张数据表字段信息")
	public List<String> describeTable(@RequestParam(value = "tableName", required = true) String tableName) throws SQLException {
		List<String> list = new ArrayList<String>();
		// Statement statement = jdbcDataSource.getConnection().createStatement();
		Statement statement = druidDataSource.getConnection().createStatement();
		String sql = "describe " + tableName;
		logger.info("Running: " + sql);
		ResultSet res = statement.executeQuery(sql);
		while (res.next()) {
			list.add(res.getString(1));
		}
		return list;
	}

	/**
	 * 查询指定tableName表中的数据
	 */
	@GetMapping("/table/select")
	@ApiOperation(value = "查询指定tableName表中的数据")
	public List<String> selectFromTable(@RequestParam(value = "tableName", required = true) String tableName) throws SQLException {
		// Statement statement = jdbcDataSource.getConnection().createStatement();
		Statement statement = druidDataSource.getConnection().createStatement();
		String sql = "select * from " + tableName;
		logger.info("Running: " + sql);
		ResultSet res = statement.executeQuery(sql);
		List<String> list = new ArrayList<String>();
		int count = res.getMetaData().getColumnCount();
		String str = null;
		while (res.next()) {
			str = "";
			for (int i = 1; i < count; i++) {
				str += res.getString(i) + " ";
			}
			str += res.getString(count);
			logger.info(str);
			list.add(str);
		}
		return list;
	}

	@PostMapping("/table/create")
	@ApiOperation(value = "创建新表", notes = "CREATE TABLE IF NOT EXISTS " +
			"user_sample " +
			"(user_num BIGINT, user_name STRING, user_gender STRING, user_age INT) " +
			"ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' " +
			"STORED AS TEXTFILE")
	public String createTable(@RequestParam(value = "sql", required = true) String sql) {
		String result = "Create table successfully...";
		try {
			Statement statement = druidDataSource.getConnection().createStatement();
//			StringBuffer sql = new StringBuffer("CREATE TABLE IF NOT EXISTS ");
//			sql.append("user_sample");
//			sql.append("(user_num BIGINT, user_name STRING, user_gender STRING, user_age INT)");
//			sql.append("ROW FORMAT DELIMITED FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n' "); // 定义分隔符
//			sql.append("STORED AS TEXTFILE"); // 作为文本存储
			logger.info("Running: " + sql);
			if(statement.execute(sql)){
				logger.info(result);
			}
		} catch (SQLException ex) {
			result = "Create table encounter an error: " + ex.getMessage();
			logger.error(result);
		}
		return result;
	}

	@PostMapping("/table/insert")
	@ApiOperation(value = "向Hive表中添加数据", notes = "INSERT INTO TABLE  user_sample(user_num,user_name,user_gender,user_age) VALUES(888,'Plum','M',32)")
	public String insertIntoTable(@RequestParam(value = "sql", required = true) String sql) {
		String result = "Insert into table successfully...";
		try {
			Statement statement = druidDataSource.getConnection().createStatement();
	//		String sql = "INSERT INTO TABLE  user_sample(user_num,user_name,user_gender,user_age) VALUES(888,'Plum','M',32)";
			if(statement.execute(sql)){
				logger.info(result);
			}
		} catch (SQLException ex) {
			result = "Insert into table encounter an error: " + ex.getMessage();
			logger.error(result);
		}
		return result;
	}

	@PostMapping("/table/delete")
	@ApiOperation(value = "删除表")
	public String delete(@RequestParam(value = "tableName", required = true) String tableName) {
		String result = "Drop table successfully...";
		try {
			Statement statement = druidDataSource.getConnection().createStatement();
			String sql = "DROP TABLE IF EXISTS "+tableName;
			logger.info("Running: " + sql);
			if(statement.execute(sql)){
				logger.info(result);
			}
		} catch (SQLException ex) {
			result = "Drop table encounter an error: " + ex.getMessage();
			logger.error(result);
		}
		return result;
	}
}