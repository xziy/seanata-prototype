// Clicks through the booking scenario and catches the transitions half-way: screenshots/flow-*.png
import { chromium } from 'playwright-core';
const base = process.argv[2], out = process.argv[3];
const browser = await chromium.launch({ executablePath: process.env.CHROMIUM ?? '/usr/bin/chromium', args: ['--no-sandbox', '--use-angle=swiftshader', '--enable-unsafe-swiftshader', '--ignore-gpu-blocklist'] });
const page = await browser.newPage({ viewport: { width: 390, height: 844 }, deviceScaleFactor: 1 });
const errors = []; page.on('pageerror', e => errors.push(String(e)));
await page.goto(`${base}#schedule`, { waitUntil: 'networkidle' });
await page.waitForSelector('canvas'); await page.waitForTimeout(3000);
const shot = n => page.screenshot({ path: `${out}flow-${n}.png` });
await page.mouse.click(245, 245); await page.waitForTimeout(140); await shot('1-day-mid');
await page.waitForTimeout(800); await shot('2-day-done');
await page.mouse.click(200, 470); await page.waitForTimeout(160); await shot('3-open-mid');
await page.waitForTimeout(800); await shot('4-training');
await page.mouse.click(280, 795); await page.waitForTimeout(900); await shot('5-booked');
console.log(await page.evaluate(() => location.hash));
await browser.close();
if (errors.length) { console.error(errors.join('\n')); process.exit(1); }
