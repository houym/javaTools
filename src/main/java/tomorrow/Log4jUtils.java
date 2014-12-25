package tomorrow;

import java.net.URL;

import org.apache.log4j.LogManager;
import org.apache.log4j.helpers.Loader;
import org.apache.log4j.helpers.OptionConverter;

/**
 * Log4j工具类
 * @author houym
 *
 */
public final class Log4jUtils {
	
	/**
	 * 缺省默认构造函数 
	 */
	private Log4jUtils(){}
	/**
	 * 加载指定的Log4j配置文件 <br/>
	 * 一个工程中有多个Log4j文件时使用 <br/>
	 * 从Classpath中加载 <br/>
	 * @param confFile 配置文件名
	 */
	public static void addConfigFile(String confFile){
		URL url = Loader.getResource(confFile);
		String configuratorClassName = null;
		OptionConverter.selectAndConfigure(url, configuratorClassName,
				LogManager.getLoggerRepository());
	}
}
