package com.etrans.jt.btlibrary.domin;

import android.content.Context;
import android.util.SparseArray;

import java.util.HashSet;

public class XxResourceString {
	static final class ResourceStringHolder{
		static XxResourceString instance = new XxResourceString();
	}
	private XxResourceString(){	
	}
	public static XxResourceString getInstance(){
		return ResourceStringHolder.instance;
	}
	
	public String getResString(int id){
		String val = mSpArrayString.get(id);
		if(val == null){
			val = mContext.getResources().getString(id);
			if(val == null)
				val = "";
			mSpArrayString.put(id, val);
		}
		return val;
	}
	
	public String[] getResStringArray(int id){
		String val[] = mSpArrayStringArray.get(id);
		
		if(val == null){
			val =mContext.getResources().getStringArray(id);
			mSpArrayStringArray.put(id, val);
			return val;
		}
		return val;
	}
	
	public HashSet<String> getResStringSet(int id){
		HashSet<String> set = mSpArrayStringSet.get(id);
		if(set == null){
			String str[] = mContext.getResources().getStringArray(id);
			set = new HashSet<String>();
			if(str != null){
				for(int i = 0; i < str.length; i++){
					set.add(str[i]);
				}
			}
			mSpArrayStringSet.append(id, set);
		}
		return set;
	}
	
	/**
	 * Ϊ��֤Context �־��� ������Application �г�ʼ��
	 * @param context
	 */
	public void init(Context context){
		mContext = context;
	}
	
	
	
	Context mContext;
	

	SparseArray<String> mSpArrayString = new SparseArray<String>(500); 

	SparseArray<String[]> mSpArrayStringArray = new SparseArray<String[]>(50);

	SparseArray<HashSet<String>> mSpArrayStringSet = new SparseArray<HashSet<String>>(200);
	
}
