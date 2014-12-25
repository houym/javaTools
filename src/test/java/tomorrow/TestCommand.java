/**
 * 
 */
package tomorrow;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;

/**
 * @author houym
 *
 */
public class TestCommand {

    /**
     * 测试Execute
     */
    @Test
    public void testExecute() {
      String c = "/usr/java/latest/bin/java -version";

      Command cmd = new Command();
      int result = cmd.execute(c, System.out, System.err,"UTF-8");
      assertThat(result, equalTo(0));
    }

}
