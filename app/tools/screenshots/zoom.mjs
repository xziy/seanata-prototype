// Close-ups at 3x: the glass of the bars over a scrolled page, and the tracks of the logbook's entries.
// Usage: node zoom.mjs <baseUrl> <outDir>/
import { chromium } from 'playwright-core';
const base = process.argv[2], out = process.argv[3];
const browser = await chromium.launch({ executablePath: process.env.CHROMIUM ?? '/usr/bin/chromium', args: ['--no-sandbox', '--use-angle=swiftshader', '--enable-unsafe-swiftshader', '--ignore-gpu-blocklist'] });
const page = await browser.newPage({ viewport: { width: 390, height: 844 }, deviceScaleFactor: 3 });
const open = async hash => { await page.goto('about:blank'); await page.goto(`${base}?safe=47,34#${hash}`, { waitUntil: 'networkidle' }); await page.waitForSelector('canvas'); await page.waitForTimeout(3500); };
const shot = (n, clip) => page.screenshot({ path: `${out}zoom-${n}.png`, clip });
await open('home'); await page.mouse.move(195, 500); await page.mouse.wheel(0, 330); await page.waitForTimeout(600);
await shot('nav', { x: 0, y: 640, width: 390, height: 204 });
await open('training'); await shot('training', { x: 0, y: 40, width: 390, height: 380 });
await open('logbook'); await page.mouse.move(195, 500);
await page.mouse.wheel(0, 420); await page.waitForTimeout(600); await shot('tracks-1', { x: 0, y: 100, width: 390, height: 640 });
await page.mouse.wheel(0, 500); await page.waitForTimeout(600); await shot('tracks-2', { x: 0, y: 100, width: 390, height: 640 });
// The second entry: the long passage round the island.
await page.mouse.wheel(0, -500); await page.waitForTimeout(600); await page.mouse.click(200, 330); await page.waitForTimeout(3500);
await page.screenshot({ path: `${out}zoom-passage.png` });
await browser.close();
