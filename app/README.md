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
