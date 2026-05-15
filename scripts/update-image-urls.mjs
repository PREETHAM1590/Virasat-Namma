#!/usr/bin/env node
/**
 * Updates imageUrl for ALL sites in Firestore with verified working Unsplash URLs.
 * Run: node update-image-urls.mjs
 */
import { initializeApp, cert } from 'firebase-admin/app';
import { getFirestore } from 'firebase-admin/firestore';
import { createRequire } from 'module';

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

const imageMap = {
  'hampi': 'https://images.unsplash.com/photo-1631986683754-7d511e03864d?w=800',
  'mysore-palace': 'https://images.unsplash.com/photo-1600112356915-089abb8fc71a?w=800',
  'badami': 'https://images.unsplash.com/photo-1695453628451-247b9478707c?w=800',
  'belur-halebidu': 'https://images.unsplash.com/photo-1601815264039-67c8ba1a7f98?w=800',
  'gol-gumbaz': 'https://images.unsplash.com/photo-1707063880573-b15e1f533d9f?w=800',
  'coorg': 'https://images.unsplash.com/photo-1547908771-05259d82e2df?w=800',
  'pattadakal': 'https://images.unsplash.com/photo-1590050752117-238cb0fb12b1?w=800',
  'aihole': 'https://images.unsplash.com/photo-1561361058-c24cecae35ca?w=800',
  'srirangapatna': 'https://images.unsplash.com/photo-1566132127697-4524fea60007?w=800',
  'somanathapura': 'https://images.unsplash.com/photo-1626010448982-0d629a5c0223?w=800',
  'bangalore-palace': 'https://images.unsplash.com/photo-1569158222040-3a4fab1bc6f0?w=800',
  'tipu-summer-palace': 'https://images.unsplash.com/photo-1566132127697-4524fea60007?w=800',
  'iskcon-bangalore': 'https://images.unsplash.com/photo-1524492412937-b28074a5d7da?w=800',
  'bull-temple': 'https://images.unsplash.com/photo-1626010448982-0d629a5c0223?w=800',
  'shravanabelagola': 'https://images.unsplash.com/photo-1602670884688-abb78db59cf0?w=800',
  'chitradurga': 'https://images.unsplash.com/photo-1586293926075-69a00febf780?w=800',
  'bidar-fort': 'https://images.unsplash.com/photo-1696239108269-448ce8d0bacc?w=800',
  'kittur-fort': 'https://images.unsplash.com/photo-1696239108269-448ce8d0bacc?w=800',
  'lalitha-mahal': 'https://images.unsplash.com/photo-1600112356915-089abb8fc71a?w=800',
  'mirjan-fort': 'https://images.unsplash.com/photo-1586293926075-69a00febf780?w=800',
  'gajendragad-fort': 'https://images.unsplash.com/photo-1696239108269-448ce8d0bacc?w=800',
  'manjarabad-fort': 'https://images.unsplash.com/photo-1696239108269-448ce8d0bacc?w=800',
  'bellary-fort': 'https://images.unsplash.com/photo-1684830235389-60a88c4e626e?w=800',
  'ibrahim-roza': 'https://images.unsplash.com/photo-1707063880573-b15e1f533d9f?w=800',
  'jumma-masjid-bijapur': 'https://images.unsplash.com/photo-1707063880573-b15e1f533d9f?w=800',
  'stone-chariot': 'https://images.unsplash.com/photo-1631986683754-7d511e03864d?w=800',
  'karkala': 'https://images.unsplash.com/photo-1602670884688-abb78db59cf0?w=800',
  'gudibande-fort': 'https://images.unsplash.com/photo-1684830235389-60a88c4e626e?w=800',
  'nagara-fort': 'https://images.unsplash.com/photo-1696239108269-448ce8d0bacc?w=800',
  'medigeshi-fort': 'https://images.unsplash.com/photo-1696239108269-448ce8d0bacc?w=800',
  'bylakuppe': 'https://images.unsplash.com/photo-1524492412937-b28074a5d7da?w=800',
  'shettihalli-church': 'https://images.unsplash.com/photo-1586293926075-69a00febf780?w=800',
  'big-banyan': 'https://images.unsplash.com/photo-1586293926075-69a00febf780?w=800',
  'sanganakallu': 'https://images.unsplash.com/photo-1684830235389-60a88c4e626e?w=800',
  'belavadi': 'https://images.unsplash.com/photo-1626010448982-0d629a5c0223?w=800',
  'hoysala-temples-mosale': 'https://images.unsplash.com/photo-1601815264039-67c8ba1a7f98?w=800',
  'vijayanagara': 'https://images.unsplash.com/photo-1631986683754-7d511e03864d?w=800',
  'barkur': 'https://images.unsplash.com/photo-1561361058-c24cecae35ca?w=800',
  'sagara': 'https://images.unsplash.com/photo-1590050752117-238cb0fb12b1?w=800',
  'sri-sogala-kshetra': 'https://images.unsplash.com/photo-1561361058-c24cecae35ca?w=800',
  'sonda': 'https://images.unsplash.com/photo-1524492412937-b28074a5d7da?w=800',
  'halebeedu-museum': 'https://images.unsplash.com/photo-1626010448982-0d629a5c0223?w=800',
  'basavakalyana': 'https://images.unsplash.com/photo-1696239108269-448ce8d0bacc?w=800',
  'keladi': 'https://images.unsplash.com/photo-1561361058-c24cecae35ca?w=800',
  'ikkeri': 'https://images.unsplash.com/photo-1561361058-c24cecae35ca?w=800',
  'sringeri': 'https://images.unsplash.com/photo-1524492412937-b28074a5d7da?w=800',
  'udupi': 'https://images.unsplash.com/photo-1524492412937-b28074a5d7da?w=800',
  'kadri-temple': 'https://images.unsplash.com/photo-1524492412937-b28074a5d7da?w=800',
  'dharwad-fort': 'https://images.unsplash.com/photo-1696239108269-448ce8d0bacc?w=800',
  'belagavi-fort': 'https://images.unsplash.com/photo-1696239108269-448ce8d0bacc?w=800',
  'gulbarga-fort': 'https://images.unsplash.com/photo-1696239108269-448ce8d0bacc?w=800',
  'nanjangud-temple': 'https://images.unsplash.com/photo-1590050752117-238cb0fb12b1?w=800',
  'melukote-temple': 'https://images.unsplash.com/photo-1590050752117-238cb0fb12b1?w=800',
  'bhoga-nandeeshwara': 'https://images.unsplash.com/photo-1561361058-c24cecae35ca?w=800',
  'talakadu': 'https://images.unsplash.com/photo-1590050752117-238cb0fb12b1?w=800',
  'banavasi': 'https://images.unsplash.com/photo-1561361058-c24cecae35ca?w=800',
  'raichur-fort': 'https://images.unsplash.com/photo-1684830235389-60a88c4e626e?w=800',
};

async function main() {
  const snapshot = await db.collection('sites').get();
  console.log(`Found ${snapshot.size} sites in Firestore. Updating imageUrls...`);

  let updated = 0;
  for (const doc of snapshot.docs) {
    const siteId = doc.id;
    if (imageMap[siteId]) {
      await doc.ref.update({ imageUrl: imageMap[siteId] });
      console.log(`  ✓ ${siteId}`);
      updated++;
    } else {
      // For any site not in our map, set a generic heritage image
      const data = doc.data();
      if (!data.imageUrl || data.imageUrl.includes('wikimedia') || data.imageUrl.includes('Special:FilePath')) {
        await doc.ref.update({ imageUrl: 'https://images.unsplash.com/photo-1524492412937-b28074a5d7da?w=800' });
        console.log(`  ✓ ${siteId} (generic)`);
        updated++;
      }
    }
  }

  console.log(`\nDone! Updated ${updated} sites.`);
}

main().catch((err) => { console.error('Error:', err); process.exit(1); });
