# Android Activity详解

## 1.什么是Activity

`Activity`是一个Android的应用组件，它提供屏幕进行交互。每个Activity都会获得一个用于绘制其用户界面的窗口，窗口可以充满哦屏幕也可以小于屏幕并浮动在其他窗口之上。

一个应用通常是由多个彼此松散联系的Activity组成，一般会指定应用中的某个Activity为主活动，也就是说首次启动应用时给用户呈现的Activity。将Activity设为主活动的方法，如下面代码所示需要在AndroidManifest文件中<intent-filter>添加以下内容

```xml
<application>
     ....
	<activity android:name=".MainActivity">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
     </activity>
     ....
</application>      
```

当然`Activity`之间可以进行互相跳转，以便执行不同的操作。每当新Activity启动时，旧的Activity便会停止，但是系统会在堆栈也就是返回栈中保留该Activity。当新Activity启动时，系统也会将其推送到返回栈上，并取得用户的操作焦点。当用户完成当前Activity并按返回按钮是，系统就会从堆栈将其弹出销毁，然后回复前一Activity

当一个`Activity`因某个新Activity启动而停止时，系统会通过该Activity的生命周期回调方法通知其这一状态的变化。Activity因状态变化每个变化可能有若干种，每一种回调都会提供执行与该状态相应的特定操作的机会

下面开始讲创建和使用Activity的基础知识（包括对Activity生命周期工作方式的全面阐述）

## 2.创建Activity

要创建Activity，必须创建Activity的子类。在子类中实现Activity在生命周期的各种状态之间转变时例如创建 Activity、停止 Activity、恢复 Activity 或销毁 Activity 时）系统调用的回调方法。Android Studio中新建项目默认创建的代码为

```java
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
```

onCreate()方法：必须实现的方法，系统在创建Activity时调用此方法。您应该在实现内初始化Activity的必要组件，必须在此方法调用setContentView(),用来定义Activity用户界面布局（XML文件）

## 3.在清单文件中声明Activity

每次新建的Activity都需要在AndroidManifest文件中添加如下内容，并将<activity>元素添加为<application>元素的子项

```xml
<manifest ... >
  <application ... >
      <activity android:name=".ExampleActivity" />
      ...
  </application ... >
  ...
</manifest >
```

## 4.启动Activity

该部分用于描述如何启动Activity。作为主活动，在应用开启的时候就会系统创建，而用户不仅仅只需要主活动界面，用户需要界面的跳转，而界面的跳转也是其他活动界面（Activity）启动。

在该部分仅仅只提及利用显示Intent方式跳转活动，代码如下

```java
Intent intent = new Intent(this, SignInActivity.class);
startActivity(intent);
//this,为本Acitivity的上下文；第二个参数为你要跳转的目的Activity.class
```

如果需要了解更多的跳转方式可以看我后续的关于应用跳转与Intent使用博客.

## 5.结束Activity

通过调用Activity的`finish()`方法来结束Activity还可以通过调用`finishActivity()`结束之前启动的活动

关于`finishActivity()`的理解：

你通过  MainActivity 来启动  ActivityA   （使用  startActivityForResult  方法），那么你在  MainActivity  这个类中需要重写  onActivityResult()  这个方法， 
然后，你可以在  onActivityResult() 中通过  finishActivity() 方法去结束掉  ActivityA  

## 6.管理Activity生命周期

周期即活动从开始到结束所经历的各种状态。生命周期即活动从开始到结束所经历的各个状态。从一个状态到另一个状态的转变，从无到有再到无，这样一个过程中所经历的状态就叫做生命周期。

Activity本质上有四种状态：

1.运行（Active/Running）:Activity处于活动状态，此时Activity处于栈顶，是可见状态，可以与用户进行交互

2.暂停（Paused）:当Activity失去焦点时，或被一个新的非全面屏的Activity，或被一个透明的Activity放置在栈顶时，Activity就转化为Paused状态。此刻并不会被销毁，只是失去了与用户交互的能力，其所有的状态信息及其成员变量都还在，只有在系统内存紧张的情况下，才有可能被系统回收掉

3.停止（Stopped）:当Activity被系统完全覆盖时，被覆盖的Activity就会进入Stopped状态，此时已不在可见，但是资源还是没有被收回

4.系统回收（Killed）:当Activity被系统回收掉，Activity就处于Killed状态

如果一个活动在处于停止或者暂停的状态下，系统内存缺乏时会将其结束（finish）或者杀死（kill）。这种非正常情况下，系统在杀死或者结束之前会调用onSaveInstance()方法来保存信息，同时，当Activity被移动到前台时，重新启动该Activity并调用onRestoreInstance()方法加载保留的信息，以保持原有的状态。

在上面的四中常有的状态之间，还有着其他的生命周期来作为不同状态之间的过度，用于在不同的状态之间进行转换，生命周期的具体说明见下。

![https://github.com/MartinZou0/Android_EXP_2/blob/master/ScreenShot/Activity%E7%94%9F%E5%91%BD%E5%91%A8%E6%9C%9F.png]()

|    方法     |                             说明                             | 是否能事后终止？ |           后接            |
| :---------: | :----------------------------------------------------------: | :--------------: | :-----------------------: |
| onCreate()  | 首次创建 Activity 时调用。您应该在此方法中执行所有正常的静态设置 — 创建视图、将数据绑定到列表等等。始终后接onStart() |        否        |         onStart()         |
|  onStart()  | 此方法被回调时表示Activity正在启动，此时Activity已处于可见状态，只是还没有在前台显示，因此无法与用户进行交互。可以简单理解为Activity已显示而我们无法看见罢了。onStart()之后如果Activity转入前台，则后接 onResume() 如果 Activity 转入隐藏状态，则后接 onStop()。 |        否        |  onResume()或   onStop()  |
| onResume()  | 当此方法回调时，则说明Activity已在前台可见，可与用户交互了（处于前面所说的Active/Running形态），onResume方法与onStart的相同点是两者都表示Activity可见，只不过onStart回调时Activity还是后台无法与用户交互，而onResume则已显示在前台，可与用户交互。始终后接onPause() |        否        |         onPause()         |
|  onPause()  | 当前 Activity正在停止（Paused形态），系统即将开始继续另一个Activity时会调用此方法。 它应该非常迅速地执行所需操作，因为它返回后，下一个 Activity 才能继续执行。 如果 Activity 返回前台，则后接 onResume()，如果 Activity 转入对用户不可见状态，则后接 onStop(）。 |        是        | onResume()或 onDestroy()  |
|  onStop()   | 在 Activity 对用户不再可见时调用。如果 Activity 被销毁或另一个Activity 继续执行并将其覆盖，就可能发生这种情况。如果Activity恢复与用户的交互，则后接onRestart(),如果Activity被销毁则后接onDestroy() |        是        | onRestart()或 onDestroy() |
| onDestroy() | 在 Activity 被销毁前调用。这是 Activity 将收到的最后调用。当 Activity 结束（有人对 Activity 调用了 `finish()`），或系统为节省空间而暂时销毁该 Activity 实例时，可能会调用它。 |        是        |            无             |
| onRestart() |           在 Activity 已停止并即将再次启动前调用。           |        否        |         onStart()         |

名为“是否能事后终止？”的列表示系统是否能在不执行另一行 Activity 代码的情况下，在*方法返回后*随时终止承载 Activity 的进程。

这些方法共同定义 Activity 的整个生命周期。您可以通过实现这些方法监控 Activity 生命周期中的三个嵌套循环： 

- Activity 的**整个生命周期**发生在 `onCreate()` 调用与 `onDestroy()` 调用之间。您的 Activity 应在 `onCreate()`中执行“全局”状态设置（例如定义布局），并释放 `onDestroy()`中的所有其余资源。例如，如果您的 Activity 有一个在后台运行的线程，用于从网络上下载数据，它可能会在`onCreate()` 中创建该线程，然后在`onDestroy()` 中停止该线程。
- Activity 的**可见生命周期**发生在 `onStart()` 调用与 `onStop()` 调用之间。在这段时间，用户可以在屏幕上看到 Activity 并与其交互。例如，当一个新 Activity 启动，并且此 Activity 不再可见时，系统会调用`onStop()`。您可以在调用这两个方法之间保留向用户显示 Activity 所需的资源。例如，您可以在`onStart()` 中注册一个 `BroadcastReceiver` 以监控影响 UI的变化，并在用户无法再看到您显示的内容时在 `onStop()`中将其取消注册。在 Activity 的整个生命周期，当 Activity 在对用户可见和隐藏两种状态中交替变化时，系统可能会多次调用 `onStart()` 和 `onStop()`。
- Activity 的**前台生命周期**发生在 `onResume()` 调用与 `onPause()` 调用之间。在这段时间，Activity 位于屏幕上的所有其他 Activity 之前，并具有用户输入焦点。Activity 可频繁转入和转出前台 — 例如，当设备转入休眠状态或出现对话框时，系统会调用 `onPause()`。由于此状态可能经常发生转变，因此这两个方法中应采用适度轻量级的代码，以避免因转变速度慢而让用户等待。

## 7.例子验证Activity活动周期

接下来就用一个小例子进行函数回调的验证

代码很简单创建两个Activity以及相应的界面，每个界面只有一个按钮和TextView,按钮设置用来进行页面跳转的点击事件。每个回调函数内都有一个Log输出，所以验证的结果也是通过查看Logcat来进行查看，下面贴出MainActivity代码,所跳转的SecondActivity代码与之类似，故不贴出，只需在MainActivity代码上进行修改。

```java
package com.example.martinzou.android_exp_2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("MainActivity","onCreate()被调用");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=(Button)findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,SecondActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("MainActivity","onStart()被调用");
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.d("MainActivity","onResume()被调用");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("MainActivity","onPause()被调用");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("MainActivity","onStop()被调用");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("MainActivity","onDestroy()被调用");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("MainActivity","onRestart()被调用");
    }
}
```

设计的两个布局文件如下所示

1.在手机上运行起来打开，查看Logcat如下图所示

![](https://github.com/MartinZou0/Android_EXP_2/blob/master/ScreenShot/Activity%E6%88%AA%E5%9B%BE4.png)

手机加载应用至显示界面时，Activity启动–>onCreate()–>onStart()–>onResume()依次被调用。此时MainActivity处于可交互的状态。

2.当我按下Home键时回到主界面时Logcat为

![](https://github.com/MartinZou0/Android_EXP_2/blob/master/ScreenShot/Activity%E6%88%AA%E5%9B%BE2.png)

点击Home键回到主界面(Activity不可见)–>onPause()–>onStop()依次被调用。

3.当点击Home键回到主界面后，再次点击App回到Activity时，Logcat结果如下

![](https://github.com/MartinZou0/Android_EXP_2/blob/master/ScreenShot/Activity%E6%88%AA%E5%9B%BE3.png)

我们可以发现重新回到Activity时，调用了onRestart方法，onStart方法，onResume方法。因此， 

当我们再次回到原Activity时–>onRestart()–>onStart()–>onResume()依次被调用。

4.当我按下返回键时，应用退出，MainActivity被销毁

![](https://github.com/MartinZou0/Android_EXP_2/blob/master/ScreenShot/Activty%E5%AE%9E%E9%AA%8C%E6%88%AA%E5%9B%BE2.png)

onPause()->onStop()->onDestroy()依次被调用，MainActivity被销毁。

5.当加载应用进入主界面，并点击按钮进行页面跳转时，查看Logcat为

![](https://github.com/MartinZou0/Android_EXP_2/blob/master/ScreenShot/Activity%E6%88%AA%E5%9B%BE5.png)

在原Activity调用了onPause()和onStop()方法，同时我们也发现了，在进行MainActivity进行完onPause()之后SecondActivity的生命周期方法才能被回调，所以这就是为什么onPause()方法不能操作耗时任务的原因了。

6.当我们点击Back键回退时，Logcat结果为

![](https://github.com/MartinZou0/Android_EXP_2/blob/master/ScreenShot/Activity%E6%88%AA%E5%9B%BE6.png)

点击之后SecondActivity的onPause()方法，onStop()方法，onDestroy()方法依次调用，MainActivity的onRestart(),onStart(),onResume()会依次调用。在进行SecondActivity进行完onPause()之后MainActivity的生命周期方法才能被回调。

7.当我们点击SecondActivity界面中的按钮跳到MainActivity，Logcat为下图所示

![](https://github.com/MartinZou0/Android_EXP_2/blob/master/ScreenShot/Activity%E6%88%AA%E5%9B%BE7.png)

会发现MainActivity并不会调用onRestart(),而是直接进行onCreate(),onStart(),onResume()的调用，跳转完毕之后SecondActivity并没有被销毁卫视处于onStop()状态，这表明SecondActivity()并没有被销毁。



**小结**:当Activity启动时，依次会调用onCreate(),onStart(),onResume()，而当Activity退居后台时（不可见，点击Home或者被新的Activity完全覆盖），onPause()和onStop()会依次被调用。当Activity重新回到前台（从桌面回到原Activity或者被覆盖后又回到原Activity）时，onRestart()，onStart()，onResume()会依次被调用。当Activity退出销毁时（点击back键），onPause()，onStop()，onDestroy()会依次被调用，到此Activity的整个生命周期方法回调完成。