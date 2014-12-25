package tomorrow;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import com.google.common.io.Files;

/**
 * 文件工具类
 * @author houym
 *
 */
public final class FileUtils {
	
	/**
	 * 缺省私有构造函数
	 */
	private FileUtils(){}
	
	/**
	 * 保存内容到临时文件中（以UTF-8的编码方式）
	 * @param content 要保存的内容
	 * @param prefix 临时文件的前缀
	 * @param suffix 临时文件的后缀
	 * @return 被保持后的临时文件的全路径
	 */
	public static String saveToTempFile(CharSequence content, String prefix,
			String suffix) {
		File tmp;
		try {
			tmp = File.createTempFile(prefix, suffix);
			Files.write(content, tmp, Charset.forName("UTF-8"));
			return tmp.getAbsolutePath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
