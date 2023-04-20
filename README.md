# JetTranslator-KMM

伴随着 Compose Multiplatform for iOS 的 Alpha 版本发布，这里将原有基于Jetpack Compose 的 JetDeepL 项目迁移至 KMM 平台，并采用统一的声明式 UI 框架 Compose  Multiplatform 快速构建跨端应用。

### 配置环境

为了能编译运行iOS平台特有代码在虚拟或真实设备上，这需要你拥有一台搭载MacOS的苹果设备。为能保证正确运行，你的苹果设备需要满足如下的环境要求。

* MacOS 13.3.1
* [Xcode](https://apps.apple.com/us/app/xcode/id497799835) 14.3
* [Android Studio Flamingo](https://developer.android.com/studio) 2022.2.1（Gradle 需要依赖内嵌的 JDK 17 运行时）
* [Kotlin Multiplatform Mobile 插件](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile)（需要在 Android Studio 插件商店中安装）
* [CocoaPods 依赖管理器](https://kotlinlang.org/docs/native-cocoapods.html)（确保 ruby 版本 >= 3.0.0）

可以采用官方提供的 kdocker 工具来检查开发环境是否配置正确。

```bash
brew install kdocker
```

运行 kdocker 可以看到环境检测结果，若出现下面字样则说明配置成功

```
Environment diagnose (to see all details, use -v option):
[✓] Operation System
[✓] Java
[✓] Android Studio
[✓] Xcode
[✓] Cocoapods

Conclusion:
  ✓ Your system is ready for Kotlin Multiplatform Mobile Development!
```

### iOS 环境配置

在配置 iOS 环境前，需要确保已经在 Android Studio 中成功安装 [Kotlin Multiplatform Mobile 插件](https://plugins.jetbrains.com/plugin/14936-kotlin-multiplatform-mobile)。在 Edit Configuration 中添加 iOS Application 的配置项。

![img](https://github.com/JetBrains/compose-multiplatform-ios-android-template/blob/main/readme_images/target_device.png)

通过 Execution Target 修改你期望部署的目标设备，默认情况下所显示的所有设备都是虚拟设备。如果你希望安装到真实的IPhone或iPad上，需要首先打开真实设备上的开发者模式并连接至开发电脑，重启 Android Studio 后，在该配置页面中即可找到你的真实设备。此外，还需要在 iosApp/Configuraion/Config.xcconfig 中填写你的TEAM_ID。

对于 Team_ID，你可以通过 Xcode 创建一个目标平台为iOS的 Hello World程序，并在终端输入下面这条命令。返回的 B5\*\*\*\*\*\*V 即为你的 Team_ID

```bash
> kdoctor --team-ids
B5*******V (Ruger McCarthy)
```

### 实际运行效果

![](https://pic-go-bed.oss-cn-beijing.aliyuncs.com/img/20230420201851.png)