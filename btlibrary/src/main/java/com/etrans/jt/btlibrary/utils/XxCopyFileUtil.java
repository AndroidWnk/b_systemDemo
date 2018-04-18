package com.etrans.jt.btlibrary.utils;

import android.content.Context;
import android.content.res.AssetManager;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class XxCopyFileUtil {
	
	//private static final String radioDBMD5 = "1d64bd81cb040136addb682665376847";
	private int mFileLength;
	private Context mContext ;
	private String mPath ;
	private String mFileName ;
	
	public void moveRadioInfos(Context context, String path , String fileName) {
		this.mContext = context;
		this.mPath = path;
		this.mFileName = fileName;
		new Thread(new Runnable() {
			@Override
			public void run() {
				copyRadioFile(mContext ,mPath , mFileName);
			}
		}).start();
	}
	
	public void copyRadioFile(Context context , String path , String fileName) {
		AssetManager am = context.getAssets();
		byte[] buff = new byte[1024];
		InputStream inputStream = null;
		FileOutputStream fileOutputStream = null;
		File file = null;
		try {
			inputStream = am.open(fileName);
			mFileLength = inputStream.available();
			File fileDir = new File(path);
			if (!fileDir.exists()) {
				fileDir.mkdirs();
			}
			file = new File(path + fileName);
			if (!file.exists()) {
				file.createNewFile();
			}else {
				if(validateDate(file)) {
					return ;
				}
			}
			for(int i=0 ; i<3 ; i++) {
				fileOutputStream = new FileOutputStream(file);
				while (inputStream.read(buff) != -1) {
					fileOutputStream.write(buff);
					fileOutputStream.flush();
				}
				file = new File(path + fileName);
				if(validateDate(file)) {
					break;
				}
			}
			
		}catch(IOException e2) {
			e2.printStackTrace();
		}finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
				if (fileOutputStream != null) {
					fileOutputStream.close();
				}
				if(file != null) {
					validateDate(file);
				}
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private boolean validateDate(File file) {
		if(mFileLength != 0 && mFileLength == file.length()){
			//设置数据库可用
//			XxRadioManager.getInstance().setBRaidoDBInit(true);
			return true;
		}else {
			return false ;
		}
	}
	
	
	private String getMD5Str(File file) {
    	long length = file.length();
    	String ret = null;
    	byte[] sBuf = new byte[(int) (length + 1)];
    	FileInputStream inputStream = null;
		try {
			inputStream = new FileInputStream(file);
			while(inputStream.read(sBuf) != -1) {
	    	}
			ret = XxMD5Util.GetMD5Code(sBuf.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ret;
	}
}
