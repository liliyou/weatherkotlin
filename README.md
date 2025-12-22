目前都還沒有做

天氣預報：
===============================================
一個簡單的天氣應用程序，可以獲取您的位置並顯示當天以及之後幾天的天氣預報。

資料來源
===============================================
[openweathermap](https://openweathermap.org/api/one-call-3)

安裝/設定
===============================================

1. 克隆該倉庫並在Android Studio 中開啟。
2. 將您的 OpenWeather API 金鑰新增至專案根local.properties檔案（如果不存在，請建立該檔案）。加入以下這行程式碼（無需加引號）：

```text
OPENWEATHER_API_KEY=your_actual_api_key_here
```
該模組build.gradle.kts讀取此屬性並將其註入到BuildConfig.OPENWEATHER_API_KEY. 更改後local.properties，運行清理/重建操作，以便重新生成 BuildConfig。
1. 建置並運行（從專案根目錄）：
```text
./gradlew clean assembleDebug
# install with adb if needed:
# adb install -r app/build/outputs/apk/debug/app-debug.apk
```

運行時權限
===============================================
* 該應用程式會ACCESS_FINE_LOCATION在運行時請求權限。請在提示時授予權限，以便應用程式可以自動取得位置資訊和天氣資料。
