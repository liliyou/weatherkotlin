# WeatherKotlin 專案指南

## 專案概述

Kotlin + Jetpack Compose 天氣應用程式，採用 Clean Architecture + MVVM 架構。

## User Stories

### 已完成

- [x] 使用者可以查看當前位置的天氣（需定位權限）
- [x] 使用者可以查看預設城市（台北）天氣（未授權定位時）
- [x] 使用者可以搜尋城市並加入追蹤清單
- [x] 使用者點選已存在的城市時，不會重複新增
- [x] 使用者可以透過建議城市快速新增（高雄、桃園、台中、新竹）
- [x] 使用者可以查看城市當前溫度、天氣狀態、最高/最低溫
- [x] 使用者可以查看城市逐時天氣預報（8 小時）
- [x] 使用者可以查看城市 5 日天氣預報
- [x] 使用者可以下拉更新天氣資料
- [x] 使用者可以刪除已追蹤的城市

### 待開發

- [ ] 使用者可以重新排序城市列表
- [ ] 使用者可以設定溫度單位（攝氏/華氏）
- [ ] 使用者可以查看更多天氣詳情（濕度、風速、氣壓）
- [ ] 使用者可以接收惡劣天氣警報通知

## 架構規範

### Clean Architecture 分層

```text
Domain Layer (純 Kotlin)
├── model/      → Domain Models
├── repository/ → Repository Interfaces
└── usecase/    → Business Logic

Data Layer
├── local/      → Room Database
├── remote/     → Retrofit API
├── location/   → Location Service
└── repository/ → Repository Implementations

Presentation Layer
├── ui/         → Compose Screens
├── theme/      → Theme & Colors
└── navigation/ → Navigation
```

### Feature Module 結構

feature:search 模組遵循相同 Clean Architecture：

```text
feature/search/
├── domain/
│   ├── model/
│   ├── repository/
│   └── usecase/
└── presentation/
    ├── component/
    └── theme/
```

## 開發規範

### ViewModel

- 使用 `StateFlow` 暴露 UI 狀態
- 使用 `viewModelScope` 管理協程
- 透過 Use Cases 存取資料

### Compose

- 每個 Screen/Component 必須有 `@Preview`
- 使用 `collectAsStateWithLifecycle()` 收集 Flow
- 遵循 Composable 結構順序：參數 → 狀態 → 副作用 → UI

### Repository

- Domain Layer 只定義 Interface
- Data Layer 實作具體邏輯
- 使用 Hilt 注入

## 常用指令

```bash
# 編譯檢查
./gradlew compileDebugKotlin

# 建置 APK
./gradlew assembleDebug

# 清理
./gradlew clean
```

## API 設定

在 `local.properties` 加入：

```text
OPENWEATHER_API_KEY=your_api_key_here
```
