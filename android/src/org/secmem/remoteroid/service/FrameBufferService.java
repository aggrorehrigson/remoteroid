package org.secmem.remoteroid.service;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.secmem.remoteroid.activity.ExplorerActivity;
import org.secmem.remoteroid.fragment.ConnectingFragment;
import org.secmem.remoteroid.natives.FrameHandler;
import org.secmem.remoteroid.network.Transmitter;
import org.secmem.remoteroid.socket.SocketModule;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

public class FrameBufferService extends Service {
	
	private boolean flag=false;
	private Process p=null;
	
	private FrameHandler fHandler;
	private Transmitter transmitter;
	
	int count=0;
	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		flag=false;
		if(p!=null)
			suClose();
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("qq","onStartCommand");
		
		transmitter = Transmitter.getInstance();
		Intent bIntent = new Intent("connecting_fragment_connect");
		
		try {
			transmitter.connect(intent.getStringExtra("IP"));
			flag=true;
		} catch (Exception e) {
		}finally{
			bIntent.putExtra("isConnected", flag);
			if(ConnectingFragment.isFinished && flag){
				try {
					transmitter.disconnect();
				} catch (IOException e) {
				}
				stopSelf();
			}
			else{
				sendBroadcast(bIntent);
				if(!flag)
					stopSelf();
			}
		}
		fHandler = new FrameHandler(getApplicationContext());
		suPermission();
		
		Thread thread = new Thread(){
			@Override
			public void run() {
				
				while(flag){
					ByteArrayOutputStream frameStream = fHandler.getFrameStream();
					
					// sendScreen(frameStream);
				}
				
			}
		};
		
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	
//	Bitmap bitmap = fHandler.getTestFrameStream();
//	
//	File copyFile = new File("/mnt/sdcard/captured"+count+".png");
//	OutputStream out = null;
//	try {
//		copyFile.createNewFile();
//		out = new FileOutputStream(copyFile);
//		bitmap.compress(CompressFormat.JPEG,100,out);
//	} catch (IOException e) {
//		// TODO Auto-generated catch block
//		e.printStackTrace();
//	}finally{
//		try {
//			out.close();
//		} catch (Exception e2) {
//			// TODO: handle exception
//		}
//	}
//	
//	
//	count++;
//	Log.i("qq","Service");
	
	public void suPermission() {
		try {
			p = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(p.getOutputStream());
			os.writeBytes("chmod 664 /dev/graphics/fb0\n");
			os.writeBytes("chmod 664 /dev/graphics/fb1\n");
			os.writeBytes("exit\n");
			os.flush();
		} catch (IOException e) {	
			
		}
	}
	
	public void suClose() {
		try {
			p = Runtime.getRuntime().exec("su");
			DataOutputStream os = new DataOutputStream(p.getOutputStream());
			os.writeBytes("chmod 660 /dev/graphics/fb0\n");
			os.writeBytes("chmod 664 /dev/graphics/fb1\n");
			os.writeBytes("exit\n");
			os.flush();
		} catch (IOException e) {
			
		}
	}

}
