import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;


public class Client1 {

    private static int scanPort = 2555;

    public Client1(int scanPort) {
        this.scanPort = scanPort;
    }

    public static void main(String[] args) {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioDatagramChannel.class)
                    .handler(new ClientHandler());

            Channel ch = b.bind(0).sync().channel();

            ch.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("(来自客户端:请帮我注册)", CharsetUtil.UTF_8),
                    new InetSocketAddress("127.0.0.1", scanPort))).sync();

            ch.closeFuture().await();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    private static class ClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
            String body = packet.content().toString(CharsetUtil.UTF_8);
            System.out.println("客户端收到信息：" + body);
        }
    }
}
