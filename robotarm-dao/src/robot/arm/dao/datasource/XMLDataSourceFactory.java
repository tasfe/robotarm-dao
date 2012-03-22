package robot.arm.dao.datasource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import robot.arm.dao.util.RobotArmPropUtil;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * 数据源工厂
 * 
 * @author li.li
 * 
 */
public class XMLDataSourceFactory implements DataSourceFactory {
	private static final Logger log = Logger.getLogger(XMLDataSourceFactory.class);
	private static final XMLDataSourceFactory instance = new XMLDataSourceFactory();

	private Map<String, DataSource> dsMap = new HashMap<String, DataSource>();

	@SuppressWarnings("unchecked")
	private XMLDataSourceFactory() {
		log.debug("init datasource...");

		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			String dbConfig = RobotArmPropUtil.getPropValue("db-config");
			document = saxReader.read(getClass().getResource(dbConfig));
			List<Node> nameNodes = document.selectNodes("//db-config//catalog");

			List<Attribute> nameAttributes = (List<Attribute>) document.selectNodes("//db-config//catalog/@name");
			List<Node> dbNodes = document.selectNodes("//db-config//catalog//db");
			List<Attribute> typeAttributes = (List<Attribute>) document.selectNodes("//db-config//catalog//db/@type");
			List<Element> driverElements = (List<Element>) document.selectNodes("//db-config//catalog//db//driver");
			List<Element> urlElements = (List<Element>) document.selectNodes("//db-config//catalog//db//url");
			List<Element> userElements = (List<Element>) document.selectNodes("//db-config//catalog//db//user");
			List<Element> passwordElements = (List<Element>) document.selectNodes("//db-config//catalog//db//password");
			List<Element> ispooledElements = (List<Element>) document.selectNodes("//db-config//catalog//db//ispooled");
			List<Element> minconnsElements = (List<Element>) document.selectNodes("//db-config//catalog//db//minconns");
			List<Element> initconnsElements = (List<Element>) document.selectNodes("//db-config//catalog//db//initconns");
			List<Element> maxconnsElements = (List<Element>) document.selectNodes("//db-config//catalog//db//maxconns");
			List<Element> maxidletimeElements = (List<Element>) document.selectNodes("//db-config//catalog//db//maxidletime");

			List<Catalog> catalogs = new ArrayList<Catalog>(nameNodes.size());// 有多少分类

			for (int j = 0; j < nameNodes.size(); j++) {// 第一层解析分类

				Catalog catalog = new Catalog();
				catalog.setName(nameAttributes.get(j).getText());

				for (int i = 0; i < dbNodes.size(); i++) {// 第二层解析分类有持有的数据源

					DB db = new DB();
					db.setType(typeAttributes.get(i).getText());
					db.setDriver(driverElements.get(i).getText());
					db.setUrl(urlElements.get(i).getText());
					db.setUser(userElements.get(i).getText());
					db.setPassword(passwordElements.get(i).getText());
					db.setIspooled(ispooledElements.get(i).getText());
					db.setMinconns(minconnsElements.get(i).getText());
					db.setInitconns(initconnsElements.get(i).getText());
					db.setMaxconns(maxconnsElements.get(i).getText());
					db.setMaxidletime(maxidletimeElements.get(i).getText());

					catalog.addDb(db);
				}

				catalogs.add(catalog);
			}

			for (Catalog c : catalogs) {

				dsMap.put(c.getName(), createDs(c));
			}

			log.debug("datasource total size: " + dsMap.size());
			log.debug("init datasource end");

		} catch (Throwable e) {
			e.printStackTrace();
		}

	}

	public DataSource getDataSource(String dsName) {
		return dsMap.get(dsName);

	}

	private DataSource createDs(Catalog catalog) throws Throwable {

		List<ComboPooledDataSource> masters = new ArrayList<ComboPooledDataSource>();
		List<ComboPooledDataSource> slaves = new ArrayList<ComboPooledDataSource>();

		for (DB db : catalog.getDbs()) {
			if ("master".equals(db.getType())) {
				// set jdbc
				ComboPooledDataSource master = new ComboPooledDataSource();
				master.setDriverClass(db.getDriver());
				master.setJdbcUrl(db.getUrl());
				master.setUser(db.getUser());
				master.setPassword(db.getPassword());
				// set Pool
				master.setMinPoolSize(Integer.parseInt(db.getMinconns()));
				master.setInitialPoolSize(Integer.parseInt(db.getInitconns()));
				master.setMaxPoolSize(Integer.parseInt(db.getMaxconns()));
				master.setMaxIdleTime(Integer.parseInt(db.getMaxidletime()));
				masters.add(master);
			} else {
				// set jdbc
				ComboPooledDataSource slave = new ComboPooledDataSource();
				slave.setDriverClass(db.getDriver());
				slave.setJdbcUrl(db.getUrl());
				slave.setUser(db.getUser());
				slave.setPassword(db.getPassword());
				// set Pool
				slave.setMinPoolSize(Integer.parseInt(db.getMinconns()));
				slave.setInitialPoolSize(Integer.parseInt(db.getInitconns()));
				slave.setMaxPoolSize(Integer.parseInt(db.getMaxconns()));
				slave.setMaxIdleTime(Integer.parseInt(db.getMaxidletime()));
				slaves.add(slave);
			}

		}

		MasterSlaverDataSource msDataSource = new MasterSlaverDataSource(masters, slaves);

		return msDataSource;
	}

	public static XMLDataSourceFactory getInstance() {
		return instance;
	}
}
