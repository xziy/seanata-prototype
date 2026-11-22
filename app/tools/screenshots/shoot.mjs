// Screenshots of every screen of the prototype, light and dark, at a phone's size.
// Usage: node shoot.mjs [baseUrl] [outDir]
// Needs a Chromium on the machine (CHROMIUM env or /usr/bin/chromium); WebGL goes through SwiftShader,
// so it works headless without a GPU.
import { chromium } from 'playwright-core';
import { mkdirSync } from 'node:fs';

const base = process.argv[2] ?? 'http://127.0.0.1:8731/';
const out = process.argv[3] ?? new URL('../../screenshots/', import.meta.url).pathname;
const screens = ['welcome', 'home', 'courses', 'course', 'program', 'schedule', 'training', 'booked', 'logbook', 'passage', 'profile'];
const themes = [['light', '']];

mkdirSync(out, { recursive: true });
const browser = await chromium.launch({
  executablePath: process.env.CHROMIUM ?? '/usr/bin/chromium',
  args: ['--no-sandbox', '--use-angle=swiftshader', '--enable-unsafe-swiftshader', '--ignore-gpu-blocklist'],
});
const page = await browser.newPage({ viewport: { width: 390, height: 844 }, deviceScaleFactor: 2 });
const errors = [];
page.on('pageerror', e => errors.push(String(e)));

for (const [theme, query] of themes) {
  for (const screen of screens) {
    await page.goto('about:blank'); // a hash-only change would not reload the app
    await page.goto(`${base}${query}#${screen}`, { waitUntil: 'networkidle' });
    await page.waitForSelector('canvas', { timeout: 30000 });
    await page.waitForTimeout(2500); // fonts and entrance animations
    const file = `${out}${screen}.png`;
    await page.screenshot({ path: file });
    console.log(file);
  }
}
await browser.close();
if (errors.length) { console.error(errors.join('\n')); process.exit(1); }
