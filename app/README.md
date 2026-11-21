# Seanata — прототип приложения яхтенной школы

Compose Multiplatform (`wasmJs`) на каркасе `restoapp-touch/prototype-rikka`: тема, демо-данные, компоненты, экраны.

```bash
./screenshots.sh                      # сборка + скриншоты всех экранов в screenshots/
python3 -m http.server 17741 --directory composeApp/build/dist/wasmJs/developmentExecutable   # открыть http://127.0.0.1:17741/
```

`node tools/screenshots/flow.mjs <url> <outDir>/` прокликивает запись на тренировку и ловит анимации на середине.

Экраны по адресу: `#welcome`, `#home`, `#courses`, `#course`, `#program`, `#schedule`, `#training`, `#booked`, `#logbook`, `#profile`.

- `theme/AppTheme.kt` — палитра `Sea` и шрифт; `data/SampleData.kt` — все демо-данные.
- `ui/components/` — кирпичи (текст, карточки, панели, иконки Lucide); `ui/screens/` — экраны.
- Фото сгенерированы Codex: `../tools/gen-images.sh` (исходники в `../assets/raw`, в ресурсах — сжатые JPEG).
- Логотип — с seanata.com (`../brand/`), на тёмном фоне используется его белая версия.

## iPhone (TestFlight)

Общий код — `composeApp/src/commonMain` (экраны, тема, данные), веб — `wasmJsMain/Main.kt` (экран из адреса),
iPhone — `iosMain/MainViewController.kt` и Xcode-проект `iosApp/` (как в Touch VN, `xziy/touch-vn`). Без Mac
собираются только klib-и: `./gradlew :composeApp:compileKotlinIosArm64`.

`.github/workflows/ios.yml` (macos-26): push в `main` с правками в `app/` или `.github/` (кроме `*.md`,
`screenshots/`, `tools/`), запуск вручную или метка `testflight` на PR → Release подписывается и уходит в
App Store Connect, затем `distribute` ждёт обработки. Сборка сама попадает в группу TestFlight `dev`. Номер
сборки — номер запуска, версия — `MARKETING_VERSION` в `iosApp/iosApp.xcodeproj`.

| | |
|---|---|
| Команда Apple | личная **Ivan Sizyi**, `ZK66E83UL5` (та же, что у Touch VN и RestoApp touch) |
| Bundle ID | `cx.m42.seanata`, Push не включён |
| App Store Connect | «Seanata», Apple ID 6837779492, SKU `seanata`, язык — русский |
| Группа | внутренняя `dev`: получает каждую сборку сама; другая — переменная репозитория `TESTFLIGHT_GROUP` |

Секреты репозитория: `IOS_DIST_CERT_P12_BASE64`, `IOS_DIST_CERT_PASSWORD`, `IOS_PROFILE_BASE64`, `ASC_KEY_ID`,
`ASC_ISSUER_ID`, `ASC_KEY_P8_BASE64`. Ключи — на guibot2 в `~/Documents/apple-ivan-sizyi/` (профиль
`Seanata_App_Store.mobileprovision`, до 16.11.2027), в git их нет. Шифрования своего нет — в `Info.plist`
`ITSAppUsesNonExemptEncryption = NO`, сборка не ждёт ответа в App Store Connect.

Тестировщики группы `dev` — только участники команды Apple. Добавить человека: пригласить в команду (роль
Developer, доступ к Seanata), после согласия — в группу; оба шага делает `tools/ios-testflight/asc tester add`
(и `tester sync`) из `/prj/here` на ноутбуке, гайд — `/prj/here/docs/ios-testflight.md`.

Иконка — знак логотипа (`../brand/logo-white.png`) на тёмно-синем цвете темы, PNG 1024×1024 без прозрачности
в `iosApp/iosApp/Assets.xcassets`.
