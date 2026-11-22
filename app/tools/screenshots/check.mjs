// Checks of the frame of a screen: the photo stays under a scrolled page, the safe area, the transitions frame by frame.
// Usage: node check.mjs <baseUrl> <outDir>/
import { chromium } from 'playwright-core';
const base = process.argv[2], out = process.argv[3];
const browser = await chromium.launch({ executablePath: process.env.CHROMIUM ?? '/usr/bin/chromium', args: ['--no-sandbox', '--use-angle=swiftshader', '--enable-unsafe-swiftshader', '--ignore-gpu-blocklist'] });
const page = await browser.newPage({ viewport: { width: 390, height: 844 }, deviceScaleFactor: 1 });
const errors = []; page.on('pageerror', e => errors.push(String(e)));
const shot = n => page.screenshot({ path: `${out}check-${n}.png` });
const open = async hash => { await page.goto('about:blank'); await page.goto(`${base}?safe=47,34#${hash}`, { waitUntil: 'networkidle' }); await page.waitForSelector('canvas'); await page.waitForTimeout(3000); };
for (const screen of ['home', 'course', 'welcome', 'schedule', 'training', 'booked', 'profile']) {
  await open(screen); await shot(`${screen}-0`);
  await page.mouse.move(195, 500); await page.mouse.wheel(0, 150); await page.waitForTimeout(500); await shot(`${screen}-1`);
  await page.mouse.wheel(0, 400); await page.waitForTimeout(500); await shot(`${screen}-2`);
}
const frames = async (name, x, y) => { await page.mouse.click(x, y); for (let i = 0; i < 8; i++) { await shot(`${name}-${i}`); await page.waitForTimeout(25); } await page.waitForTimeout(600); await shot(`${name}-end`); };
await open('home');
await frames('tab-courses', 117, 790);
await frames('tab-schedule', 195, 790);
await frames('push', 200, 470);
await frames('back', 30, 90);
await browser.close();
if (errors.length) { console.error(errors.join('\n')); process.exit(1); }
