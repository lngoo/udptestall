import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOption;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class Server {

    public static void main(String[] args) {
        try {
            Bootstrap b = new Bootstrap();
            b.group(new NioEventLoopGroup())
                    .channel(NioDatagramChannel.class)
                    .option(ChannelOption.SO_BROADCAST, true)
                    .handler(new UdpServerHandler())
                    .bind(2555).sync().channel().closeFuture().await();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class UdpServerHandler extends SimpleChannelInboundHandler<DatagramPacket> {
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet) throws Exception {
            ByteBuf buf = packet.copy().content();
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            String body = new String(req, "UTF-8");
            System.out.println("服务端收到信息：" + body);//打印收到的信息
            //向客户端发送消息
            String json = "(来自服务端: 你的注册请求已处理)";
            // 由于数据报的数据是以字符数组传的形式存储的，所以传转数据
            byte[] bytes = json.getBytes("UTF-8");
            DatagramPacket data = new DatagramPacket(Unpooled.copiedBuffer(bytes), packet.sender());
            ctx.writeAndFlush(data);//向客户端发送消息
        }
    }
}