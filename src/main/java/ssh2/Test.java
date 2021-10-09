package ssh2;

import com.jcraft.jsch.ChannelShell;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author DJF
 * @version 0.1.0
 * @Description
 * @create 2021-10-09 16:29
 * @since 0.1.0
 **/
public class Test {
    private static String username = "root";
    private static String pwd = "qg-Z9G3Dt";
    private static String IP = "192.168.46.22";
    private static ShellConnectionStream ssh = ShellConnectionStream.builder().username(username).password(pwd).host(IP).port(22).build();
    public static void main(String[] args) {
        String commond = "sshpass -p qg-Z9G3Dt scp -v /data/sonarqube-6.7.721.tar.gz root@192.168.46.21:/data/";
        try{
            ssh.connect();
            if(ssh.isReady()) {
                String result = ssh.execSingleCommand(commond);
                ssh.close();
                System.out.println(result);
            }
        }catch(Exception e){
            e.printStackTrace();
        }


    }
}
