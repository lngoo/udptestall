package udpboth;

import java.io.IOException;
import java.net.*;

/**
 * @创建时间： 2019/12/31 15:27
 * @类说明：请填写
 * @修改记录：
 */
public class EchoClient
{
    private String sendStr = "hello";
    private String netAddress = "255.255.255.255";
    private final int PORT = 2555;

    private DatagramSocket datagramSocket;
    private DatagramPacket datagramPacket;

    public EchoClient(){
        System.out.println(Thread.currentThread().getName() + "-----------/////EchoClient//////////");
        try {
            datagramSocket = new DatagramSocket();
            byte[] buf = sendStr.getBytes();
            InetAddress address = InetAddress.getByName(netAddress);
            datagramPacket = new DatagramPacket(buf, buf.length, address, PORT);
            datagramSocket.send(datagramPacket);
//            Counter.send.addAndGet(1);

            byte[] receBuf = new byte[1024];
            DatagramPacket recePacket = new DatagramPacket(receBuf, receBuf.length);
            datagramSocket.receive(recePacket);
//            Counter.receive.addAndGet(1);

            // 监听返回数据
            String receStr = null;
            while (null == receStr) {
                receStr = new String(recePacket.getData(), 0 , recePacket.getLength());
            }

            //获取服务端ip
            String serverIp = recePacket.getAddress().getHostAddress() + ":" + recePacket.getPort();
            System.out.println(serverIp + "/// " + receStr);
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭socket
            if(datagramSocket != null){
                datagramSocket.close();
            }
        }
    }

    public static void main(String[] args) {
        while (true) {
            new Thread(new Runnable() {
                public void run() {
                    EchoClient udpClient = new EchoClient();
                }
            }).start();
            try {
                System.out.println(Thread.currentThread().getName() + "---////MAIN-休息3秒//////////, 存活线程数= " + Thread.activeCount());
//                System.out.println("%%%%%%%%% " + Counter.send.get() + "/" + Counter.receive.get());
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}