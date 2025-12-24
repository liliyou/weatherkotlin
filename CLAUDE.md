# WeatherKotlin 專案指南

Kotlin + Jetpack Compose 天氣應用程式，採用 Clean Architecture + MVVM 架構。

## User Stories

### 已完成

- [x] 查看當前位置天氣（需定位權限）
- [x] 查看預設城市（台北）天氣（未授權定位時）
- [x] 搜尋城市並加入追蹤清單（自動防重複）
- [x] 透過建議城市快速新增（高雄、桃園、台中、新竹）
- [x] 查看城市天氣（當前溫度、狀態、最高/最低溫）
- [x] 查看逐時天氣預報（8 小時）
- [x] 查看 5 日天氣預報
- [x] 下拉更新天氣資料
- [x] 刪除已追蹤的城市

### 待開發

- [ ] 重新排序城市列表
- [ ] 設定溫度單位（攝氏/華氏）
- [ ] 查看更多天氣詳情（濕度、風速、氣壓）
- [ ] 惡劣天氣警報通知

## 架構規範

### Clean Architecture 分層

```text
Domain Layer (純 Kotlin)
├── model/      → Domain Models
├── repository/ → Repository Interfaces
└── usecase/    → Business Logic (city/, weather/)

Data Layer
├── local/      → Room Database
├── remote/     → Retrofit API + DTO
├── repository/ → Repository Implementations
└── util/       → DateTimeFormatter

Presentation Layer (ui/)
├── theme/      → Theme, Colors, CardModifiers
├── util/       → WeatherIconUrl
├── components/ → 共用 UI 元件
├── home/       → 首頁
└── detail/     → 詳情頁
```

### Feature Module

```text
feature/search/
├── domain/     → model, repository, usecase
└── presentation/ → Screen, ViewModel, component, theme
```

## 開發規範

### ViewModel

- `StateFlow` 暴露 UI 狀態
- `SharedFlow` 處理一次性事件（錯誤訊息）
- `viewModelScope` 管理協程
- 透過 Use Cases 存取資料

### Compose

- 每個 Screen/Component 必須有 `@Preview`
- `collectAsStateWithLifecycle()` 收集 Flow
- 結構順序：參數 → 狀態 → 副作用 → UI

### 層級依賴

- UI Layer 禁止直接導入 Data Layer
- 圖示 URL 使用 `ui/util/WeatherIconUrl`
- Domain Layer 只定義 Interface，Data Layer 實作

## 常用指令

```bash
./gradlew compileDebugKotlin  # 編譯檢查
./gradlew assembleDebug       # 建置 APK
./gradlew clean               # 清理
```

## API 設定

在 `local.properties` 加入：

```text
OPENWEATHER_API_KEY=your_api_key_here
```
