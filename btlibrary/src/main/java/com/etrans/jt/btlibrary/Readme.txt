1,用户执行 播放 暂停 停止 上一曲 下一曲 快进 快退时;回调到onPlaybackStatusChanged()接口中
2,当执行这个接口里的方法执行时   会调用到  getPlayStatus 去获取歌曲的信息  在onGetPlayStatusRsp 接口中进行调用
  然后去刷新　歌曲的信息　progress timer 以及其他按键的UI
3,private boolean isStatusPlaying(long playStatus) {
          return (playStatus == BluetoothAvrcpController.PLAY_STATUS_PLAYING ||
                  playStatus == BluetoothAvrcpController.PLAY_STATUS_FWD_SEEK ||
                  playStatus == BluetoothAvrcpController.PLAY_STATUS_REV_SEEK) ? true : false;
      }
      判断是否是播放着的    播放中 快进中 快退中 都是属于Playing状态
4,更新UI状态接口设计
    1>更新进度条
    2>更新播放时间
    3>更新歌曲信息
    4>更新按钮状态

5,可能的情况

<1>连接成功  播放音乐  车机无界面  手机去控制 上一曲 下一曲 播放 暂停 等操作
<2>在<1>的情况下去启动车机蓝牙音乐界面 歌曲信息的展示
    解决方案:当界面启动时 去判断当前是否是连接状态的 获取歌曲信息 刷新界面
<3>车机蓝牙音乐启动 连接成功 控制 上一首  下一首 等操作

6,当用户点击停止播放时  将所有信息恢复成默认状态

7,拖动进度条时 没有可调用的接口



蓝牙电话支持hold(int type)
挂断/保存 切换通话 电话会议 接听/挂断




该应用是单独的jar包  只是亿程把包打入sdk中




DEMO问题:
1,手机连接不上车机 必须车机连接才能连接上;

2,蓝牙电话接通后程序崩溃;
  01-02 09:10:16.120 3076-3076/com.broadcom.bt.app.hfdevice E/AndroidRuntime: FATAL EXCEPTION: main
                                                                            Process: com.broadcom.bt.app.hfdevice, PID: 3076
                                                                            java.lang.NullPointerException
                                                                                at android.os.Parcel.readException(Parcel.java:1471)
                                                                                at android.os.Parcel.readException(Parcel.java:1419)
                                                                                at com.broadcom.bt.hfdevice.IBluetoothHfDevice$Stub$Proxy.registerEventHandler(IBluetoothHfDevice.java:1042)
                                                                                at com.broadcom.bt.hfdevice.BluetoothHfDevice.registerEventHandler(BluetoothHfDevice.java:698)
                                                                                at com.broadcom.bt.app.hfdevice.BRCMHfDeviceConnectedActivity.onServiceConnected(BRCMHfDeviceConnectedActivity.java:1719)
                                                                                at com.broadcom.bt.hfdevice.BluetoothHfDevice$1.onServiceConnected(BluetoothHfDevice.java:488)
                                                                                at android.app.LoadedApk$ServiceDispatcher.doConnected(LoadedApk.java:1110)
                                                                                at android.app.LoadedApk$ServiceDispatcher$RunConnection.run(LoadedApk.java:1127)
                                                                                at android.os.Handler.handleCallback(Handler.java:733)
                                                                                at android.os.Handler.dispatchMessage(Handler.java:95)
                                                                                at android.os.Looper.loop(Looper.java:136)
                                                                                at android.app.ActivityThread.main(ActivityThread.java:5017)
                                                                                at java.lang.reflect.Method.invokeNative(Native Method)
                                                                                at java.lang.reflect.Method.invoke(Method.java:515)
                                                                                at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:851)
                                                                                at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:667)
                                                                                at dalvik.system.NativeStart.main(Native Method)


3,电话本下载是将手机的联系人导入到系统的通讯录上



2016 7 14 15:34

1,两个回调接口更新播放进度
 1>onGetPlayStatusRsp 当蓝牙连接后进行获取当前播放的歌曲信息
 2>onPlaybackPositionChanged 专业更新进度


2016 7 22
1,电话本数据库定义
增
    insertContact(Map<,> params);
删
改
查
    根据电话号码查询 联系人的信息
    根据首字母查询联系人
    根据拼音查询联系人
    根据首字母上的数字查询
    根据拼音的数字查询

电话本保存步骤
  1,监听蓝牙连接 下载电话本
  2,将下载的电话本的姓名进行拼音转化
  3,将拼音的转化成T9数字保存到数据库

  4,查询

电话本数据库接口
  1,插入数据
  2,根据电话查询联系人
  3,根据姓名查询联系人
  4,根据电话号码查询 18513834993
  5,根据全拼        FUXIAOLEI       PY_KEY
  6,根据首字母       FXL            INITIAL_KEY
  7,根据T9查询       395            INITIAL_KEY_T9
  8,根据拼音首字母T9   389426534      PY_KEY_T9


  2016/9/1
  当手机拨打电话时 连上蓝牙后  如何启动通话界面???
  2016/9/9
  屏幕dpi应该是160dpi
  2016/9/12
  问题:拨打电话中 切换声道 导致蓝牙共享崩溃


  2016/9/19
  问题:

  1,在车机上进行打电话或者重新拨打电话 蓝牙共享会出现崩溃现象;
  2,在通话过程中 频繁切换车机输出声音和手机输出声音会出现蓝牙共享崩溃;
  3,没有获取重拨电话号码的接口
  4,电话本下载 下载不全      例如: 手机有12个联系人   下载成功的个数是10个




  2016/9/22
  拨打蓝牙电话没有声音
  蓝牙音乐有声音
  在打开蓝牙连接时 会随机出现关闭wifi
  2016/9/27
  09-27 14:55:51.336 16972-16972/com.etrans.jt.bluetooth E/BluetoothAdapter: android.os.DeadObjectException
                                                                                 at android.os.BinderProxy.transact(Native Method)
                                                                                 at android.bluetooth.IBluetooth$Stub$Proxy.getState(IBluetooth.java:725)
                                                                                 at android.bluetooth.BluetoothAdapter.getState(BluetoothAdapter.java:500)
                                                                                 at com.broadcom.bt.hfdevice.BluetoothHfDevice.isEnabled(BluetoothHfDevice.java:1932)
                                                                                 at com.broadcom.bt.hfdevice.BluetoothHfDevice.hangup(BluetoothHfDevice.java:1224)
                                                                                 at com.etrans.jt.btlibrary.module.BluetoothPhoneModule.hangup(BluetoothPhoneModule.java:476)
                                                                                 at com.etrans.jt.bluetooth.presenter.PhoneCallPresenter.hangup(PhoneCallPresenter.java:56)
                                                                                 at java.lang.reflect.Method.invokeNative(Native Method)
                                                                                 at java.lang.reflect.Method.invoke(Method.java:515)
                                                                                 at com.etrans.jt.bluetooth.base.BaseProxy$Invocation.invoke(BaseProxy.java:41)
                                                                                 at $Proxy1.hangup(Native Method)
                                                                                 at com.etrans.jt.bluetooth.view.PhoneCallExActivity.onClick(PhoneCallExActivity.java:174)
                                                                                 at android.view.View.performClick(View.java:4438)
                                                                                 at android.view.View$PerformClick.run(View.java:18422)
                                                                                 at android.os.Handler.handleCallback(Handler.java:733)
                                                                                 at android.os.Handler.dispatchMessage(Handler.java:95)
                                                                                 at android.os.Looper.loop(Looper.java:136)
                                                                                 at android.app.ActivityThread.main(ActivityThread.java:5017)
                                                                                 at java.lang.reflect.Method.invokeNative(Native Method)
                                                                                 at java.lang.reflect.Method.invoke(Method.java:515)
                                                                                 at com.android.internal.os.ZygoteInit$MethodAndArgsCaller.run(ZygoteInit.java:851)
                                                                                 at com.android.internal.os.ZygoteInit.main(ZygoteInit.java:667)
                                                                                 at dalvik.system.NativeStart.main(Native Method)











