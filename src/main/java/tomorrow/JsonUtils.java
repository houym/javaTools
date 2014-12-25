package tomorrow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import com.google.common.base.Throwables;

/**
 * JSON解析类
 * @author houym
 */
public final class JsonUtils {
	/**
	 * 默认私有构造函数
	 */
	private JsonUtils() {
	}

	/**
	 * Json串转换为bean
	 * 
	 * @param jsonStr
	 *            json串
	 * @param clazz
	 *            要转化bean的class
	 * @param <T>
	 *            bean
	 * 
	 * @return 转换后的bena
	 */
	public static <T> T toBean(String jsonStr, Class<T> clazz) {

		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(jsonStr, new TypeReference<T>() {
			});
		} catch (JsonParseException e) {
			Throwables.propagate(e);
		} catch (JsonMappingException e) {
			Throwables.propagate(e);
		} catch (IOException e) {
			Throwables.propagate(e);
		}
		return null;
	}

	/**
	 * Json串转换为 List
	 * 
	 * @param jsonStr
	 *            json串
	 * @param clazz
	 *            要转化bean的class
	 * @param <T>
	 *            bena
	 * 
	 * @return 转换后的List<bena>
	 */
	public static <T> List<T> toList(String jsonStr, Class<T> clazz) {

		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(jsonStr, mapper.getTypeFactory()
					.constructParametricType(ArrayList.class, clazz));
		} catch (JsonParseException e) {
			Throwables.propagate(e);
		} catch (JsonMappingException e) {
			Throwables.propagate(e);
		} catch (IOException e) {
			Throwables.propagate(e);
		}
		return new ArrayList<T>();
	}

	/**
	 * 对象转为Json串
	 * 
	 * @param obj
	 *            对象
	 * @return Json串
	 */
	public static String getJson(Object obj) {

		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(obj);
		} catch (JsonGenerationException e) {
			Throwables.propagate(e);
		} catch (JsonMappingException e) {
			Throwables.propagate(e);
		} catch (IOException e) {
			Throwables.propagate(e);
		}
		return "";
	}
}
