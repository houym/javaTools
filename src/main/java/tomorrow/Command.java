package tomorrow;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 执行外部命令行
 * 
 * @author houym
 * 
 */
public class Command {
	/**
	 * log
	 */
	private final Logger logger = LoggerFactory.getLogger(Command.class);

	/**
	 * 启动新的进程执行用户指定命令
	 * 
	 * @param command
	 *            命令行
	 * @param sysout
	 *            当前进程的标准输出
	 * @param syserr
	 *            当前进程的标准错误输出
	 * @param charsetName
     *            标准输出和标准错误输出的字符集
	 * @return command执行结果
	 */
	public int execute(final String command, final PrintStream sysout,
			final PrintStream syserr,String charsetName) {
		
		try {
			return executeException(command, sysout, syserr,charsetName);
		} catch (InterruptedException e) {
			processException(command,e);
		} catch (IOException e) {
			processException(command,e);
		}
		return -1;
	}
	
	/**
	 * 处理异常
	 * @param command 执行命令
	 * @param e 错误异常
	 */
	private void processException(final String command, Exception e){
		logger.error("execute " + command, e);
		throw new RuntimeException(e);
	}
	
	/**
	 * 启动新的进程执行用户指定命令
	 * 
	 * @param command
	 *            命令行
	 * @param sysout
	 *            当前进程的标准输出
	 * @param syserr
	 *            当前进程的标准错误输出
	 * @param charsetName
     *            标准输出和标准错误输出的字符集
	 * @return command执行结果
	 * @throws InterruptedException 异常
	 * @throws IOException  异常
	 */
	private int executeException(final String command, final PrintStream sysout,
			final PrintStream syserr,String charsetName) throws InterruptedException, IOException {
		logger.info(command);

		Process process = Runtime.getRuntime().exec(command);
		InputStream stdout = process.getInputStream();
		InputStream stderr = process.getErrorStream();

		final BufferedReader stdoutReader = new BufferedReader(
				new InputStreamReader(stdout,charsetName));
		final BufferedReader stderrReader = new BufferedReader(
				new InputStreamReader(stderr,charsetName));

		Thread t1 = new Thread() {
			@Override
			public void run() {
				String line = null;
				try {
					while ((line = stdoutReader.readLine()) != null) {
						sysout.println(line);
					}
				} catch (IOException e) {
					logger.error("execute " + command, e);
					e.printStackTrace();
				}
			}
		};
		Thread t2 = new Thread() {
			@Override
			public void run() {
				try {
					String line = null;
					while ((line = stderrReader.readLine()) != null) {
						System.err.println(line);
					}
				} catch (IOException e) {
					logger.error("execute " + command, e);
					e.printStackTrace();
				}
			}
		};
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		stdout.close();
		stderr.close();
		try {
			// process.exitValue未非同步，process.waitFor为同步阻塞
			// 虽然使用了waitFor但是有时依然会在java.lang.UNIXProcess.exitValue方法中
			// 抛出 java.lang.UNIXProcess.exitValue异常，因此，等待2秒后再返回
			return process.waitFor();
		} catch (InterruptedException e) {
			logger.warn("wait 2sed after waitFor");
			Thread.sleep(2000);
			return process.waitFor();
		}
	}
}
