package jd.chapter2;

import java.net.InetSocketAddress;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class EchoServer {

	private final int port;

	public EchoServer(int port) {
		this.port = port;
	}

	private void start() throws Exception {
		final EchoServerHandler handler = new EchoServerHandler();
		final EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
//		final EventLoopGroup eventLoopGroup = new EpollEventLoopGroup();
		
		try {
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(eventLoopGroup)
				.channel(NioServerSocketChannel.class)
//				.channel(EpollServerSocketChannel.class)
				.localAddress(new InetSocketAddress(port))
				.childHandler(new ChannelInitializer<Channel>() {
					protected void initChannel(Channel ch) throws Exception {
						ch.pipeline().addLast(handler);
					};
				});
			
			ChannelFuture future = bootstrap.bind().sync();
			future.channel().closeFuture().sync();
		} finally {
			eventLoopGroup.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) throws Exception {
		new EchoServer(8080).start();
	}
}
