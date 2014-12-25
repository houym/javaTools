package tomorrow;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.log4j.helpers.Loader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.google.common.base.Throwables;

/**
 * XML解析类
 * @author houym
 */

public final class XmlUtils {

    /**
     * 私有默认构造函数
     */
    private XmlUtils() {
    }

    /**
     * log
     */
    private static final Logger logger = LoggerFactory.getLogger(XmlUtils.class);

    /**
     * 把Xml转换到Bean</br> Xml以UTF-8编码处理
     * @param clazz Bean必须是使用JAXB注解来标注过的class
     * @param xml Xml内容本身
     * @param <T> aa
     * @return Bean的实例
     */
    @SuppressWarnings("unchecked")
    public static <T> T xmlToBean(Class<T> clazz, String xml) {
        try {
            JAXBContext jc = JAXBContext.newInstance(clazz);
            Unmarshaller u = jc.createUnmarshaller();
            ByteArrayInputStream bis = new ByteArrayInputStream(strToBytes(xml));
            return (T) u.unmarshal(bis);
        } catch (JAXBException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * 把Bean中的内容转换为String</br> Xml以UTF-8编码处理
     * @param bean 必须是使用JAXB注解来标注过的
     * @param <T> bean的类型
     * @return 转换后的String是Xml内容本身
     */
    public static <T> String beanToXml(T bean) {
        try {
            JAXBContext jc = JAXBContext.newInstance(bean.getClass());
            Marshaller m = jc.createMarshaller();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            m.marshal(bean, bos);
            return bos.toString("UTF-8");
        } catch (JAXBException e) {
            logger.error("beanToXml error！", e);
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            logger.error("beanToXml error！", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * 使用指定的xsd对xml进行验证
     * @param xsdPath xsd的路径
     * @param xml xml内容
     * @return true：成功<br/>
     *         false：失败
     */
    public static boolean schemaCheck(String xsdPath, String xml) {
        String schemaLanguage = XMLConstants.DEFAULT_NS_PREFIX;
        SchemaFactory schemaFactory = SchemaFactory.newInstance(schemaLanguage);

        URL url = Loader.getResource(xsdPath);

        try {
            Schema schema = schemaFactory.newSchema(url);

            Validator validator = schema.newValidator();
            InputSource inputSource = new InputSource(
                    new ByteArrayInputStream(strToBytes(xml)));
            Source source = new SAXSource(inputSource);
            validator.validate(source);
            return true;
        } catch (SAXException e) {
            Throwables.propagate(e);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
        return false;
    }

    /**
     * 字符串转换为bytes
     * @param str 字符串
     * @return bytes
     */
    private static byte[] strToBytes(String str) {
        try {
            return str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
