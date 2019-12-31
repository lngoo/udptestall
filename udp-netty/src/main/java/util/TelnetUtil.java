package util;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;

/**
 * @创建时间： 2019/12/31 17:06
 * @类说明：请填写
 * @修改记录：
 */
public class TelnetUtil {

    public static void tryTelnet(String ip, int port) {
        try {
            TelnetClient telnetClient = new TelnetClient("vt200");
            telnetClient.setDefaultTimeout(5000); //socket延迟时间：5000ms
            telnetClient.connect(ip, port);  //建立一个连接,默认端口是23
            telnetClient.getRemoteOptionState();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
