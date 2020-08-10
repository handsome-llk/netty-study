package com.study.netty.netty.protobuf;

public class README {
	/**
	 * 这个弄了很久，得记录下问题
	 * 一开始直接下了proto3，和netty权威指南的proto2用法好像有点不一样，所以一开始很懵逼
	 * 这里说下proto2的用法。
	 * 
	 * 1、下载
	 * 
	 * 下载是在git上。路径，https://github.com/protocolbuffers/protobuf/releases/download/v3.6.1/protoc-3.6.1-win32.zip
	 * 如果要下其他版本，就把路径中的版本号改为自己需要的。netty权威指南是2.5.0的版本，所以这里下了
	 * https://github.com/protocolbuffers/protobuf/releases/download/v2.5.0/protoc-2.5.0-win32.zip
	 * 解压出来就是了，有个protoc.exe
	 * 
	 * 2、使用
	 * 
	 * 这里说下目录结构。以protoc.exe所在目录说明。
	 * 我这里在protoc.exe的同级目录下建了文件夹nettyStudy,文件夹中有两个文件。（这两个文件我现在放在了这个目录下的proto包中。）
	 * 然后我想自动生成这两个文件的java pojo。
	 * 拿文件SubscribeReq.proto举例
	 * 在protoc.exe的目录下打开cmd。执行
	 * protoc.exe --java_out=.\nettyStudy .\nettyStudy\SubscribeReq.proto
	 * 
	 * 第一个路径是生成代码的存放路径，假设是path1；第二个路径是源文件路径，假设是path2。所以通用命令是
	 * protoc.exe --java_out=path1 path2
	 * 
	 * 
	 */
}
