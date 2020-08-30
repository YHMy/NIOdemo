package com.example.niodemo.demo;

import java.io.*;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Set;

public class NIOdEMO {
    public void test01() throws IOException {
        //非直接缓冲区
        FileInputStream in=new FileInputStream("1.txt");
        FileOutputStream out =new FileOutputStream("2.txt");
        FileChannel fc1=in.getChannel();
        FileChannel fc2=out.getChannel();
        ByteBuffer bb=ByteBuffer.allocate(1024);
        while(fc1.read(bb)!=-1){
            bb.flip();//切换到读模式
            fc2.write(bb);
            bb.clear();
        }
        fc1.close();
        fc2.close();
        //直接缓冲区，内存映射文件
        //---------------------------------------------------------------
       FileChannel fcr=FileChannel.open(Paths.get("1.txt"),StandardOpenOption.READ);
        FileChannel fcw=FileChannel.open(Paths.get("2.txt"),StandardOpenOption.WRITE);
        MappedByteBuffer mb=fcr.map(FileChannel.MapMode.READ_ONLY,0,fcr.size());
        MappedByteBuffer mb2=fcw.map(FileChannel.MapMode.READ_WRITE,0,fcr.size());
        byte[] bytes=new byte[mb.limit()];
        mb.get(bytes);
        mb2.put(bytes);
        //通道之间的数据传输,直接缓冲区
        //--------------------------------------------------------------------------------
        FileChannel inC1=FileChannel.open(Paths.get("1.txt"),StandardOpenOption.READ);
        FileChannel outC1=FileChannel.open(Paths.get("2.txt"),StandardOpenOption.WRITE);
        inC1.transferTo(0,inC1.size(),outC1);
        inC1.close();
        outC1.close();
        /*
        * 分散读取（Scattering）:将通道中的数据分散到多个缓冲区中；
        * 聚集写入（Gathering）:将多个缓冲区中的数据写入到通道中
        * */
        RandomAccessFile raf1=new RandomAccessFile("1.txt","rw");//读写模式
        FileChannel channel=raf1.getChannel();
        //分配到指定大小的缓冲区
        ByteBuffer  bf1=ByteBuffer.allocate(1024);
        ByteBuffer bf2=ByteBuffer.allocate(2048);
        ByteBuffer[] bfs={bf1,bf2};
        channel.read(bfs);
        for(ByteBuffer by:bfs){
            by.flip();
        }
        System.out.print(new String(bfs[0].array(),0,bfs[0].limit()));
        System.out.println(new String(bfs[1].array(),0,bfs[1].limit()));
        //聚集写入
        RandomAccessFile raf2=new RandomAccessFile("2.txt","rw");
        FileChannel channel2= raf2.getChannel();
        channel2.write(bfs);
        //字符集：CharSet 编码：字符串——》字符数组 解码：字符数组-》字符串
       Map<String, Charset> map= Charset.availableCharsets();
        Set<Map.Entry<String,Charset>> set=map.entrySet();
        for(Map.Entry<String,Charset> en:set){
            en.getKey();
            en.getValue();
        }
        Charset cs=Charset.forName("GBK");
        //获取编码器
        CharsetEncoder ce=cs.newEncoder();
        CharsetDecoder cd=cs.newDecoder();
        CharBuffer cbuf=CharBuffer.allocate(1024);
        cbuf.put("qwert");
        cbuf.flip();
        ByteBuffer bbuf=ce.encode(cbuf);
        //bbuf.get();

    }
    public void client() throws IOException {
        FileChannel fileChannel=FileChannel.open(Paths.get("1.txt"),StandardOpenOption.READ);
        SocketChannel socketChannel=SocketChannel.open(new InetSocketAddress("127.0.0.1",8080));
        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
        while(fileChannel.read(byteBuffer)!=-1){
            byteBuffer.flip();
            socketChannel.write(byteBuffer);
            byteBuffer.clear();
        }
    }
    public void Service() throws IOException {
        FileChannel fileChannel=FileChannel.open(Paths.get("2.txt"),StandardOpenOption.WRITE);
        ServerSocketChannel serverSocketChannel=ServerSocketChannel.open();
        SocketChannel socketChannel=serverSocketChannel.accept();
        socketChannel.bind(new InetSocketAddress(8080));
        ByteBuffer byteBuffer=ByteBuffer.allocate(1024);
        while(socketChannel.read(byteBuffer)!=-1){
            byteBuffer.flip();
            fileChannel.write(byteBuffer);
            byteBuffer.clear();
        }
        fileChannel.close();
        socketChannel.close();
        serverSocketChannel.close();
FileChannel fc1=FileChannel.open(Paths.get("1.txt"),StandardOpenOption.READ);
FileChannel fc2=FileChannel.open(Paths.get("2.txt"),StandardOpenOption.WRITE);
fc1.transferTo(0,fc1.size(),fc2);
    }
}
