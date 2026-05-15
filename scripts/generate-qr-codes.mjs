#!/usr/bin/env node
/**
 * Batch-generates QR code PNGs for all heritage sites.
 * Each QR encodes the site's qrCodeId (e.g. "QR-HAMPI-001").
 *
 * Usage: node generate-qr-codes.mjs
 * Output: ./qr-output/<siteId>.png
 */
import { initializeApp, cert } from 'firebase-admin/app';
import { getFirestore } from 'firebase-admin/firestore';
import QRCode from 'qrcode';
import { mkdir } from 'fs/promises';
import { join, dirname } from 'path';
import { fileURLToPath } from 'url';
import { createRequire } from 'module';

const __dirname = dirname(fileURLToPath(import.meta.url));
const OUTPUT_DIR = join(__dirname, 'qr-output');

const require = createRequire(import.meta.url);
let serviceAccount;
try {
  serviceAccount = require('./serviceAccountKey.json');
} catch {
  console.error('ERROR: scripts/serviceAccountKey.json not found.');
  process.exit(1);
}

initializeApp({ credential: cert(serviceAccount) });
const db = getFirestore();

async function main() {
  await mkdir(OUTPUT_DIR, { recursive: true });

  const snapshot = await db.collection('sites').get();
  if (snapshot.empty) {
    console.log('No sites found in Firestore.');
    return;
  }

  console.log(`Generating QR codes for ${snapshot.size} sites...`);

  for (const doc of snapshot.docs) {
    const { name, qrCodeId } = doc.data();
    const siteId = doc.id;
    const qrValue = qrCodeId || siteId;
    const outPath = join(OUTPUT_DIR, `${siteId}.png`);

    await QRCode.toFile(outPath, qrValue, {
      width: 512,
      margin: 2,
      color: { dark: '#000000', light: '#ffffff' },
    });
    console.log(`  ✓ ${name} → ${outPath} (encodes: ${qrValue})`);
  }

  console.log(`\nDone! ${snapshot.size} QR codes saved to ${OUTPUT_DIR}`);
}

main().catch((err) => {
  console.error('Error:', err);
  process.exit(1);
});
