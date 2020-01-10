package websocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;

import java.util.logging.Logger;

/**
 * 测试地址： http://www.websocket-test.com/
 */
public class NioWebSocketServer {
    private void init(){
        System.out.println("正在启动websocket服务器");
        NioEventLoopGroup boss=new NioEventLoopGroup();
        NioEventLoopGroup work=new NioEventLoopGroup();
        try {
            ServerBootstrap bootstrap=new ServerBootstrap();
            bootstrap.group(boss,work);
            bootstrap.channel(NioServerSocketChannel.class);
            bootstrap.childHandler(new ChannelInitializer<SocketChannel>(){
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast("logging",new LoggingHandler("DEBUG"));//设置log监听器，并且日志级别为debug，方便观察运行流程
                    ch.pipeline().addLast("http-codec",new HttpServerCodec());//设置解码器
                    ch.pipeline().addLast("aggregator",new HttpObjectAggregator(65536));//聚合器，使用websocket会用到
                    ch.pipeline().addLast("http-chunked",new ChunkedWriteHandler());//用于大数据的分区传输
                    ch.pipeline().addLast("handler",new NioWebSocketHandler());//自定义的业务handler
                }
            });
            Channel channel = bootstrap.bind(7613).sync().channel();
            System.out.println("webSocket服务器启动成功："+channel);
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("运行出错："+e);
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
            System.out.println("websocket服务器已关闭");
        }
    }

    public static void main(String[] args) {
        new NioWebSocketServer().init();
    }
}
