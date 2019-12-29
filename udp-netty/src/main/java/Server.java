import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
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
            String address = packet.sender().getAddress().toString() + packet.sender().getPort();
            System.out.println( address + "/"+ ctx.channel().id() +"/"+  "服务端收到信息：" + body);//打印收到的信息
            //向客户端发送消息
            String json = address + ctx.channel().id() + body;
            // 由于数据报的数据是以字符数组传的形式存储的，所以传转数据
            byte[] bytes = json.getBytes("UTF-8");
//            DatagramPacket data = new DatagramPacket(Unpooled.copiedBuffer(bytes), packet.sender());
//            ctx.channel().writeAndFlush(data);
            try {
                Channel channel = ctx.channel();
                channel.connect(packet.sender());
                DefaultChannelPromise promise = (DefaultChannelPromise) channel.writeAndFlush(Unpooled.copiedBuffer(bytes)).sync();//向客户端发送消息
                System.out.println(promise.isSuccess());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            System.out.println("%%%%%%%% channelActive");
            super.channelActive(ctx);
        }
    }
}
