package jd.netty5;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

public class BufTest {

	public static void main(String[] args) {
		final ByteBuf buf = Unpooled.buffer("Jared".length(), "Jared".length() * 10);
		
		ExecutorService svc = Executors.newFixedThreadPool(2);
		// write
		svc.submit(new Runnable() {
			@Override
			public void run() {
				while(buf.isWritable("Jared".length())) {
					buf.writeCharSequence("Jared", StandardCharsets.UTF_8);
				}
			}
		});
		
		// read
		svc.submit(new Runnable() {
			@Override
			public void run() {
				while(buf.isReadable("Jared".length())) {
					System.out.println(buf.readCharSequence("Jared".length(), StandardCharsets.UTF_8));
				}
			}
		});
		
		svc.shutdown();
	}
	
	public static void getAndSet() {
		Charset utf8 = Charset.forName("UTF-8");
        //创建一个新的 ByteBuf以保存给定字符串的字节
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        //打印第一个字符'N'
        System.out.println((char)buf.getByte(0));
        //存储当前的 readerIndex 和 writerIndex
        int readerIndex = buf.readerIndex();
        int writerIndex = buf.writerIndex();
        //将索引 0 处的字 节更新为字符'B'
        buf.setByte(0, (byte)'B');
        //打印第一个字符，现在是'B'
        System.out.println((char)buf.getByte(0));
        //将会成功，因为这些操作并不会修改相应的索引
        assert readerIndex == buf.readerIndex();
        assert writerIndex == buf.writerIndex();
	}
	
	public static void readAndWrite() {
		 Charset utf8 = Charset.forName("UTF-8");
        //创建一个新的 ByteBuf 以保存给定字符串的字节
        ByteBuf buf = Unpooled.copiedBuffer("Netty in Action rocks!", utf8);
        //打印第一个字符'N'
        System.out.println((char)buf.readByte());
        //存储当前的readerIndex
        int readerIndex = buf.readerIndex();
        //存储当前的writerIndex
        int writerIndex = buf.writerIndex();
        //将字符 '?'追加到缓冲区
        buf.writeByte((byte)'?');
        assert readerIndex == buf.readerIndex();
        //将会成功，因为 writeByte()方法移动了 writerIndex
        assert writerIndex != buf.writerIndex();
	}
}
