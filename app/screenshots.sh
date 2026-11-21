#!/usr/bin/env bash
# Builds the app for wasm, serves it on a local port and takes a screenshot of every screen: screenshots/<screen>.png
set -euo pipefail
cd "$(dirname "$0")"
PORT="${PORT:-8741}"
dist="composeApp/build/dist/wasmJs/developmentExecutable"
[ -d tools/screenshots/node_modules ] || (cd tools/screenshots && npm install --silent)
./gradlew ":composeApp:wasmJsBrowserDevelopmentExecutableDistribution" --console=plain -q
python3 -m http.server "$PORT" --bind 127.0.0.1 --directory "$dist" >/dev/null 2>&1 &
server=$!
trap 'kill $server 2>/dev/null || true' EXIT
sleep 1
rm -rf screenshots
node tools/screenshots/shoot.mjs "http://127.0.0.1:$PORT/" "$PWD/screenshots/"
