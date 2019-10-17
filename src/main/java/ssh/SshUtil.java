package ssh;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

public class SshUtil {

	private static final String ENCODING = "UTF-8";

	public static Session getJSchSession(DestHost destHost) throws JSchException {
		JSch jsch = new JSch();

		Session session = jsch.getSession(destHost.getUsername(), destHost.getHost(), destHost.getPort());
		session.setPassword(destHost.getPassword());
		session.setConfig("StrictHostKeyChecking", "no");// 第一次访问服务器不用输入yes
		session.setTimeout(destHost.getTimeout());
		session.connect();

		return session;
	}

	public static String execCommandByJSch(Session session, String command, String resultEncoding)
			throws IOException, JSchException {
		String result = "";
		// 1.默认方式，执行单句命令
		ChannelExec channelExec = (ChannelExec) session.openChannel("exec");
		InputStream in = channelExec.getInputStream();
		channelExec.setCommand(command);
		channelExec.setErrStream(System.err);
		channelExec.connect();

		BufferedReader reader = new BufferedReader(new InputStreamReader(in));
		String buf = null;
		while ((buf = reader.readLine()) != null) {
			result += " " + buf;
		}
		channelExec.disconnect();
		System.out.println("result:"+result);
		return result;

	}

	public static void main(String[] args) throws JSchException, IOException {
		DestHost dest = new DestHost("192.168.251.101", "root", "101@yusys.com");
		Session session = getJSchSession(dest);
//		String command1 = "cd /opt/yusys/data/git/TCTS/";
		/**
		 * 一次性执行多条shell的方法：

        1）每个命令之间用;隔开
            说明：各命令的执行给果，不会影响其它命令的执行。换句话说，各个命令都会执行，但不保证每个命令都执行成功。

        2）每个命令之间用&&隔开
            说明：若前面的命令执行成功，才会去执行后面的命令。这样可以保证所有的命令执行完毕后，执行过程都是成功的。

        3）每个命令之间用||隔开
            说明：||是或的意思，只有前面的命令执行失败后才去执行下一条命令，直到执行成功一条命令为止。
		 */
		String command2 = "cd /opt/yusys/data/git/TCTS/&&/opt/yusys/git/bin/git push --mirror http://admin:51c59df0e7794cd0afbb390ca5a6413e@172.16.90.236:2297/git/TCTS";
		
		
		String command3 = "cd /home/weblogic/Oracle/Middleware/user_projects/domains/base_domain/bin && nohup ./startWebLogic.sh &";
//		String command3 = "nohup sh /home/weblogic/Oracle/Middleware/user_projects/domains/base_domain/bin/startWebLogic.sh &";
//		execCommandByJSch(session, command1, "");
		execCommandByJSch(session, command3, "");
	}
	

}
