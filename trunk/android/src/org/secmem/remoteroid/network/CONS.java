package org.secmem.remoteroid.network;

public class CONS {
	static public class OPCODE{
		final static public int OP_SENDFILEINFO = 1;
		final static public int OP_SENDFILEDATA = 2;
		final static public int OP_REQFILEDATA	= 5;
		final static public int OP_READYSEND	= 6;
		final static public int OP_REQFILEINFO	= 7;
	}
	
	static public class HOST{
		final static public int PORT = 50000;
	}
	
	//header contents
	final static public int HEADERSIZE = 6;
	final static public int OPCODESIZE = 2;	//opcode : 2 bytes
	final static public int PACKETSIZE = 4; //packet size : 4bytes
	//
	
	final static public int MAXPACKETSIZE = 4096;	
	final static public int FILENAMESIZE = 100;
	final static public int FILESIZESIZE = 100;	
}
