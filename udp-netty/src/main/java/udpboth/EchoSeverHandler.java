package udpboth;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

/**
 * @创建时间： 2019/12/31 15:25
 * @类说明：请填写
 * @修改记录：
 */
public class EchoSeverHandler extends SimpleChannelInboundHandler<DatagramPacket>
{
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DatagramPacket packet)
            throws Exception
    {
        try {
            // 读取收到的数据
            ByteBuf buf = (ByteBuf) packet.copy().content();
            byte[] req = new byte[buf.readableBytes()];
            buf.readBytes(req);
            String body = new String(req, CharsetUtil.UTF_8);
            String clientHost = packet.sender().getAddress().toString() + ":" + packet.sender().getPort();
            System.out.println(clientHost + "【NOTE】>>>>>> 收到客户端的数据："+body);

            // 回复一条信息给客户端
            ctx.writeAndFlush(new DatagramPacket(
                    Unpooled.copiedBuffer("Hello，我是Server，我的时间戳是"+System.currentTimeMillis()
                            , CharsetUtil.UTF_8)
                    , packet.sender())).sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}