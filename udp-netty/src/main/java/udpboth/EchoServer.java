package udpboth;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

/**
 * @创建时间： 2019/12/31 15:24
 * @类说明：请填写
 * @修改记录：
 */
public class EchoServer
{
    public static void main(String[] args) throws InterruptedException
    {
        Bootstrap b = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        b.group(group)
                .channel(NioDatagramChannel.class)
                .handler(new EchoSeverHandler());

        // 服务端监听在9999端口
        b.bind(2555).sync().channel().closeFuture().await();
    }
}