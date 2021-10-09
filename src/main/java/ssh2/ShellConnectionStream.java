package ssh2;

import com.jcraft.jsch.*;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.util.List;

@Getter
@Builder
@Slf4j
public class ShellConnectionStream {


    Session session;
    InputStream commandOutput;
    BufferedReader reader;
    JSch ssh;
    Channel channel;
    boolean ready;
    String username;
    String password;
    String host;
    int port = 22;
    String publicKeyPath;
    String publicKeyPassphrase;

    /**
     * Connect to host with a timeout of 30 seconds and skipping host key check
     *
     * @return true in case of success, false if it fails
     * @throws JSchException
     */
    public boolean connect() throws JSchException {

        try {
            this.ssh = new JSch();
            session = ssh.getSession(this.username, this.host, this.port);

            if (this.publicKeyPath != null && !this.publicKeyPath.isEmpty()) {
                if (this.publicKeyPassphrase != null && !this.publicKeyPassphrase.isEmpty()) {
                    this.ssh.addIdentity(this.publicKeyPath, this.publicKeyPassphrase);
                } else {
                    this.ssh.addIdentity(this.publicKeyPath);
                }
            } else {
                session.setPassword(this.password);
            }

            session.setConfig("StrictHostKeyChecking", "no");
            session.connect(30000);
            this.session = session;
            this.ready = true;

            return true;
        } catch (Exception e) {
            this.ready = false;
        }
        return false;
    }

    /**
     * Write a specific command to a server, executing it and getting back the result
     *
     * @param comand The command as a string to be executed
     * @return A string with the result of the command
     */
    public String write(String comand) {

        try {
            channel = session.openChannel("exec");
            ((ChannelExec) channel).setCommand(comand);
            setCommandOutput(channel.getInputStream());
            channel.connect(30000);

            StringBuilder sBuilder = new StringBuilder();
            String read = reader.readLine();

            while (read != null) {
                sBuilder.append(read);
                sBuilder.append("\n");
                read = reader.readLine();
            }

            return sBuilder.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String execSingleCommand(String shell) {
        String result = "";
        try {
            channel = session.openChannel("shell");
            ChannelShell channelShell = (ChannelShell) channel;
            InputStream inputStream = channelShell.getInputStream();
            channelShell.setPty(true);
            channelShell.connect();
            OutputStream outputStream = channelShell.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            printWriter.println(shell);
            Thread.sleep(1000);
            printWriter.flush();

            byte[] tmp = new byte[1024];
            while (inputStream.available() > 0) {
                int i = inputStream.read(tmp, 0, 1024);
                String s = new String(tmp, 0, i);
                System.out.println(s);
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                }
            }
            outputStream.close();
            inputStream.close();
            channelShell.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String execCommandByShell(List<String> shellList) {
        String result = "";
        try {
            channel = session.openChannel("shell");
            ChannelShell channelShell = (ChannelShell) channel;
            InputStream inputStream = channelShell.getInputStream();
            channelShell.setPty(true);
            channelShell.connect();
            OutputStream outputStream = channelShell.getOutputStream();
            PrintWriter printWriter = new PrintWriter(outputStream);
            for (String shell : shellList) {
                printWriter.println(shell);
                Thread.sleep(1000);
                printWriter.flush();
                if (shell.indexOf("scp") >= 0) {
                    Thread.sleep(15000);
                }
            }
            byte[] tmp = new byte[1024];
            while (true) {
                long waitBuildingTime = 0L;
                long waitStartTime = System.currentTimeMillis();
                while (inputStream.available() > 0) {
                    int i = inputStream.read(tmp, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    String s = new String(tmp, 0, i);
                    if (s.indexOf("--More--") >= 0) {
                        outputStream.write((" ").getBytes());
                        outputStream.flush();
                    }
                    System.out.println(s);
                }
                if (channelShell.isClosed()) {
                    System.out.println("exit-status:" + channelShell.getExitStatus());
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {

                }
                waitBuildingTime = System.currentTimeMillis() - waitStartTime;
            }
            outputStream.close();
            inputStream.close();
            channelShell.disconnect();
            session.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * Upload a file from the specific location to a remote directory on server
     *
     * @param origin               Path pointing to the origin file
     * @param destinationDirectory Path to the destination folder
     * @return true in case of success and false in case of failure
     */
    public boolean upload(String origin, String destinationDirectory) {

        try {
            File origin_ = new File(origin);
            destinationDirectory = destinationDirectory.replace(" ", "_");
            String destination = destinationDirectory.concat("/").concat(origin_.getName());
            return upload(origin, destination, destinationDirectory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Upload a file to a remote destination
     *
     * @param origin               Path to the file origin
     * @param destination          The destination file name
     * @param destinationDirectory The destination directory
     * @return true in case of success and false in case of failure
     */
    public boolean upload(String origin, String destination, String destinationDirectory) {

        try {
            ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
            sftp.connect();
            destinationDirectory = destinationDirectory.replace(" ", "_");
            sftp.cd(destinationDirectory);
            sftp.put(origin, destination);
            sftp.disconnect();
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Close the connection
     */
    public void close() {

        if (channel != null) {
            channel.disconnect();
        }


        if (session != null) {
            session.disconnect();
        }

        this.ready = false;
    }

    public void setCommandOutput(InputStream in) {
        this.commandOutput = in;
        reader = new BufferedReader(new InputStreamReader(in));
    }

    public boolean prepareUpload(String origem) {

        File file = new File(origem);
        if (file.exists() && file.isFile()) {
            return true;
        }
        return false;
    }

    public boolean isReady() {
        return ready;
    }

    public boolean download(String arquivoRemoto, String arquivoLocal) {

        if (prepareUpload(arquivoLocal)) {

            try {
                ChannelSftp sftp = (ChannelSftp) session.openChannel("sftp");
                sftp.connect();
                sftp.get(arquivoRemoto, arquivoLocal);
                sftp.disconnect();
                return true;

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return false;
    }

}
