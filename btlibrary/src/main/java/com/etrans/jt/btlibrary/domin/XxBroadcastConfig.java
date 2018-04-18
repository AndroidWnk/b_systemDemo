package com.etrans.jt.btlibrary.domin;

import android.os.Environment;

import java.io.File;

public class XxBroadcastConfig {
	/**
	 * 导航启动完成的广播
	 */
	public static final String ACTION_NAV_MAP_LAODCOMPLETE = "com.uniits.map.loadcomplete";

	/**
	 * 蓝牙已经打开广播
	 */
	public static final String ACTION_BLUETOOTH_ON = "com.uniits.bluetooth.on";

	/**
	 * 蓝牙关闭 广播消息
	 */

	public static final String ACTION_BLUETOOTH_OFF = "com.uniits.bluetooth.off";

	/**
	 * 有设备连接到蓝牙
	 */

	public static final String ACTION_BLUETOOTH_LINKED = "com.uniits.bluetooth.linked";

	/**
	 * 语音助理请求获取音频焦点广播
	 * 
	 */
	public static final String ACTION_VOICE_AUDIO_REQUEST = "com.uniits.voice.assistant.audio.request";
	/**
	 * 语音助理释放广播焦点
	 */
	public static final String ACTION_VOICE_AUDIO_ABANDON = "com.uniits.voice.assistant.audio.abandon";
	/**
	 * 电话请求音频焦点
	 */
	public static final String ACTION_TEL_AUDIO_REQUEST = "com.uniits.tel.audio.request";
	/**
	 * 电话释放音频焦点
	 */
	public static final String ACTION_TEL_AUDIO_ABANDON = "com.uniits.tel.audio.abandon";
	
	
	public static final String ACTION_OPEN_MUSIC = "com.uniits.open.MUSIC";
	
	public static final String ACTION_OPEN_VEDIO = "com.uniits.open.VEDIO";
	
	public static final String ACTION_OPEN_RADIO = "com.uniits.open.RADIO";

	/**
	 * 物理按键启动语音助理广播
	 */
	public static final String ACTION_LAUNCH_VOICE_ASSIST = "com.uniits.launch.voice.assist";

	/**
	 * 语音助理的提供者 BAIDU 由百度提供 IFLYTEK 由科大讯飞提供
	 */
	public static final String EXTRA_PROVIDER = "provider";

	/**
	 * 语音助理操作应用程序的广播定义
	 * 
	 * @author ping
	 *
	 */
	public class AppContral {
		/**
		 * 语音助理发送操作应用的广播
		 */
		public static final String ACTION_APP = "com.uniits.voice.assistant.APP";

		/**
		 * 操作的内容 launch|search|close 缺省为 launch
		 * 
		 */
		public static final String EXTRA_OPERATION = "operation";

		/**
		 * 任意文本 操作应用的名称
		 */
		public static final String EXTRA_NAME = "name";

		/**
		 * 类型是String 棋牌游戏、射击游戏 有可能为null
		 */
		public static final String EXTRA_CATEGORY = "category";

		/**
		 * 类型是int 是否收费 0：免费
		 */
		public static final String EXTRA_PRICE = "price";

		/**
		 * 描述应用的关键词
		 */
		public static final String EXTRA_KEYWORK = "keyword";

	}

	/**
	 * 语音助理操作音乐的广播定义
	 * 
	 * @author ping
	 *
	 */
	public class MusicContral {
		/**
		 * 语音助理发送操作音乐的的广播
		 */
		public static final String ACTION_MUSIC = "com.uniits.voice.assistant.MUSIC";

		/**
		 * 操作的内容 paly|search|query 缺省为 play
		 * 
		 */
		public static final String EXTRA_OPERATION = "operation";

		/**
		 * 查询字段 歌曲名还是歌手 name|singer
		 */
		public static final String EXTRA_QUERIED = "queried";

		/**
		 * 
		 * 歌曲名称
		 * 
		 */
		public static final String EXTRA_NAME = "name";

		/**
		 * 歌曲来源 U 盘|iPod|SD|CD|本地|网络
		 * 
		 */
		public static final String EXTRA_SOURCE = "source";

		/**
		 * 歌手名称
		 */
		public static final String EXTRA_SINGER = "singer";
		/**
		 * 专辑名称
		 */
		public static final String EXTRA_ALBUM = "album";

		/**
		 * 歌曲类型
		 * 
		 * 抒情/古典/流行/蓝调/乡村/校园/嘻哈/摇滚/爵士/轻音乐/经典
		 */
		public static final String EXTRA_CATEGORY = "category";

		/**
		 * 歌曲tag 百度语音发送
		 */
		public static final String EXTRA_GENRE = "genre";
		/**
		 * 地域 百度语音发送
		 */
		public static final String EXTRA_COUNTRY = "country";

		/**
		 * 人群 百度语音发送
		 */
		public static final String EXTRA_AUDIENCE = "audience";
		/**
		 * 心情 百度语音发送
		 */
		public static final String EXTRA_MOOD = "mood";
		/**
		 * 电影名 百度语音发送
		 */
		public static final String EXTRA_MOVIE = "movie";
		/**
		 * 电视剧名 百度语音发送
		 */
		public static final String EXTRA_TV = "tv";
		/**
		 * 节目名称 百度语音发送
		 */
		public static final String EXTRA_PROGRAM = "program";

		/**
		 * 语言 百度语音发送
		 */
		public static final String EXTRA_INLANGUAGE = "inlanguage";

		/**
		 * 戏曲 百度语音发送
		 */
		public static final String EXTRA_OPERA = "oprea";

		/**
		 * 乐器 百度语音发送
		 */
		public static final String EXTRA_INSTUMENT = "instrument";

	}

	/**
	 * 语音助理发送收音机的广播定义
	 * 
	 * @author ping
	 *
	 */
	public class RadioContral {
		/**
		 * 语音助理发送操作收音机的广播
		 */
		public static final String ACTION_RADIO = "com.uniits.voice.assistant.RADIO";

		/**
		 * 
		 * FM|AM
		 */
		public static final String EXTRA_CATEGORY = "category";

		/**
		 * 操作类型 launch
		 */
		public static final String EXTRA_OPERATION = "operation";

		/**
		 * 电台名称
		 */
		public static final String EXTRA_NAME = "name";

		/**
		 * 电台频点
		 */
		public static final String EXTRA_CODE = "code";

		/**
		 * 节目分类
		 */

		public static final String EXTRA_MODIFIER = "modifier";
		/**
		 * 节目的地域信息省份
		 */
		public static final String EXTRA_PROVINCE = "province";

		/**
		 * 节目的地域信息城市
		 */
		public static final String EXTRA_CITY = "city";

	}

	/**
	 * 语音助理发送命令的 广播定义
	 * 
	 * @author ping
	 *
	 */
	public class CMDContral {

		/**
		 * 命令的名称 视图切换|2D 视图|3D 视图|地图放大|地图 缩小|导航路线|取消导航|收藏当前点 顺序循环|单曲循环|随机播放
		 * 音量+|音量-|静音|打开音量 暂停|播放|上一首|下一首 {@link #CMD_SET_VOLUME}
		 */
		public static final String EXTRA_NAME = "name";

		/**
		 * 设置声音传入的值 int 类型 小于 100传入的是百分比 大于100 整除100是音量
		 */
		public static final String EXTRA_VALUE = "value";

		/**
		 * 百度语音 发送 {@link #ACTION_CMD_MUSIC_PLAYMODE} 对应的时间的分钟数 int}
		 */
		public static final String EXTRA_MINUTE = "minute";
		/**
		 * 百度语音 发送 {@link #ACTION_CMD_MUSIC_PLAYMODE} 对应的时间的秒数 int}
		 */
		public static final String EXTRA_SECOND = "second";

		/**
		 * 音乐播放模式控制广播 {@link #EXTRA_NAME }
		 */
		public static final String ACTION_CMD_MUSIC_PLAYMODE = "com.uniits.voice.assistant.cmd.music.PLAYMODE";

		/**
		 * 音乐曲目控制广播 {@link #EXTRA_NAME} {@link #EXTRA_SECOND } and
		 * {@link #EXTRA_MINUTE}
		 */
		public static final String ACTION_CMD_MUSIC_TRACKS = "com.uniits.voice.assistant.cmd.music.TRACKS";

		/**
		 * 音量控制 广播
		 */
		public static final String ACTION_CMD_VOLUME = "com.uniits.voice.assistant.cmd.VOLUME";

		/**
		 * 设置声音
		 */
		public static final String CMD_SET_VOLUME = "set_volume";

		public static final String CMD_MUSIC_PLAY = "play";

	}

	/**
	 * 语音助理发送操作电话广播定义
	 * 
	 * @author ping
	 *
	 */
	public class TelContral {

		/**
		 * 语音助理发送 到电话 广播
		 */
		public static final String ACTION_TEL = "com.uniits.voice.assistant.PHONE";

		/**
		 * 
		 * 打电话给谁
		 */
		public static final String EXTRA_NAME = "name";
		/**
		 * 拨打该电话号码
		 */
		public static final String EXTRA_PHONENUMBER = "phonenumber";
	}

	public class NewsControl {
		/**
		 * 语音助理新闻的广播 
		 *  {@link #EXTRA_CATEGORY} {@link #EXTRA_KEYWORDS}
		 *  {@link #EXTRA_OPERATION} {@link #EXTRA_LOCATION}
		 *  {@link #EXTRA_MEDIA} {@link #EXTRA_DATE}
		 *  {@link #EXTRA_TIME}
		 */
		public static final String ACTION_NEWS = "com.uniits.voice.NEWS";

		/**
		 * 新闻分类
		 *  军事新闻 体育新闻...
		 * 	可选
		 */
		public static final String EXTRA_CATEGORY = "category";
		/**
		 * 关键字 
		 * 	房姐、表哥...
		 *  可选
		 */
		public static final String EXTRA_KEYWORDS = "keyword";
		/**
		 * 播放新闻
		 * 	执行的动作 play
		 * 	必选
		 */
		public static final String EXTRA_OPERATION = "operation";// peration
		/**
		 * 新闻的发生的地域
		 * 	国内、 国际、 安逸….  
		 * 	可选
		 */
		public static final String EXTRA_LOCATION = "location";
		/**
		 * 来源媒体 
		 * 	新浪、 网易、 凤凰….
		 *  可选
		 */
		public static final String EXTRA_MEDIA = "media";
		/**
		 * 日期
		 *  格式：YYYY-MM-DD
		 *  可选
		 */
		public static final String EXTRA_DATE = "date";
		/**
		 * 时间
		 * 	格式：HH:MM:SS
		 * 可选
		 */
		public static final String EXTRA_TIME = "time";
	}

	/**
	 * 当前城市改变的广播
	 * 
	 * Intent 传递参数的名称 {@link #EXTRA_DISTRICTNAME  } and {@link #EXTRA_DITRICTCODE}
	 * 
	 */
	public static final String ACTION_DISTRICT_CHANGED = "com.uniits.district.CHANGED";

	/**
	 * 获取当前行政区划的城市名称 {@link #ACTION_DISTRICT_CHANGED}
	 */
	public static final String EXTRA_DISTRICTNAME = "districtname";

	/**
	 * 获取当前行政区划代码 {@link #ACTION_DISTRICT_CHANGED}
	 */
	public static final String EXTRA_DITRICTCODE = "districtcode";

	// ******************************************************************************************************
	/**
	 * 天气预报保存的tag
	 */
	public static final String WEATHERSAVE = "weatherSave";
	/**
	 * 天气预报的行政区划代码
	 */
	public final static String WEATHERDISTRICT = "weatherDistrict";

	public final static String LASTWEATHERDISTRICT = "lastWeatherDistrict";

	/**
	 * 气温
	 */
	public final static String WEATHERTEMP = "temperature";

	/**
	 * 天气
	 */
	public final static String WEATHERTYPE = "weather";
	/**
	 * 天气图片
	 */
	public final static String WEATHERIMG = "weatherimg";

	public final static String CURDAY = "curday";

	// ******************************************************************************************************

	/**
	 * launcher apk path
	 */
	public final static String updateFilePath = Environment
			.getExternalStorageDirectory()
			+ File.separator
			+ "launcher"
			+ File.separator;

	public final static String updateFileName = "XxBrightBeanLauncher.apk";
	
	
	
	public final class WeatherContral{
		public static final String ACTION_WEATHER = "com.uniits.voice.WEATHER";
		
		/**
		 * 动作
		 * 	{@value "query"}
		 */
		public static final String EXTRA_OPERATION = "operation";
		/**
		 * 具体日期
		 *  今天|明天|昨天|前天|后天|大后天 
		 */
		public static final String EXTRA_DATE = "date";
		/**
		 * 城市（若没有城市默认当前 GPS 定位城市） 
		 */
		public static final String EXTRA_CITY = "city";
		/**
		 * 城市
		 * 	查询的城市 （当查询如“桂林”这样的城市 ）
		 */
		public static final String EXTRA_ADDRESS = "address";
		
		/**
		 * 服务器返回的时间
		 */
		public static final String EXTRA_CURTIME = "curtime";
		
	}
	
	public final  class  MessageContral{
		/**
		 * 短信的广播
		 * {@link #EXTRA_OPERATION} {@link #EXTRA_CATEGORY}
		 */
		public static final String ACTION_MESSAGE = "com.uniits.voice.MESSAGE";
		/**
		 * 短信的操作
		 *  view 查看
		 *  synth 朗读
		 */
		public static final String EXTRA_OPERATION = "operation";
		/**
		 * 短信的类型
		 *  可选
		 *	unread: 未读短信 
		 *	read: 已读短信 
		 *	sent: 已发短信 
		 */
		public static final String EXTRA_CATEGORY = "category";
	}
	/**
	 * 蓝牙电话广播
	 * @author ping
	 *
	 */
	public final class BluetoothPhoneContral{
		
		/**
		 * 正在使用蓝牙电话
		 */
		public static final String ACTION_BLUETOOTHPHONE_USING = "com.uniits.bluetooth.phone.USING";
		
		/**
		 * 空闲状态
		 */
		public static final String ACTION_BLUETOOTHPHONE_FREE ="com.uniits.bluetooth.phone.FREE";
	}
	

	
	public static final String APP_MODULE_MUSIC = "music";
	
	public static final String APP_MODULE_VEDIO = "video";
	
	
}
