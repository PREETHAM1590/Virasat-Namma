/**
 * Virasat Namma — Firestore Seeder Script
 *
 * Seeds all Karnataka heritage sites into Firestore using Firebase Admin SDK.
 * Run with: node scripts/seed-firestore.mjs
 *
 * Prerequisites:
 *   1. Download service account key from Firebase Console:
 *      Project Settings → Service Accounts → Generate new private key
 *      Save as: scripts/serviceAccountKey.json
 *   2. npm install firebase-admin  (run from project root)
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
  console.error('Download it from: Firebase Console → Project Settings → Service Accounts → Generate new private key');
  process.exit(1);
}

initializeApp({ credential: cert(serviceAccount) });

const db = getFirestore();
db.settings({ ignoreUndefinedProperties: true });

const sites = [
  {
    id: 'hampi',
    name: 'Hampi',
    nameLocal: 'ಹಂಪಿ',
    location: 'Hampi, Ballari',
    district: 'Ballari',
    type: 'UNESCO',
    shortDescription: 'UNESCO World Heritage Site — the ruins of the glorious Vijayanagara Empire.',
    description: 'Hampi is a UNESCO World Heritage Site located in east-central Karnataka. It was the capital of the Vijayanagara Empire in the 14th century. The ruins depict the finest Dravidian style of art and architecture.',
    history: 'Founded in 1336 by Harihara I and Bukka Raya I, Hampi became the epicenter of the Vijayanagara Empire. By 1500 CE, it was the world\'s second-largest medieval-era city after Beijing.',
    architecture: 'Hampi showcases Dravidian architecture. The Vittala Temple complex features the iconic stone chariot and musical pillars that produce melodic tones when struck.',
    legends: 'Legend says that Hampi was the monkey kingdom of Kishkindha from the Ramayana. The boulders are believed to be thrown by Hanuman during the battle between Vali and Sugriva.',
    imageUrl: 'https://images.unsplash.com/photo-1708067225451-e7fa52d3c014?w=800',
    galleryImages: ['https://images.unsplash.com/photo-1708067225451-e7fa52d3c014?w=400', 'https://images.unsplash.com/photo-1696239105346-4e48185eb001?w=400'],
    facts: [
      { id: 'hampi-f1', title: 'Stone Chariot Mystery', description: 'The iconic stone chariot in Vittala Temple is carved from a single granite block — it cannot be moved.' },
      { id: 'hampi-f2', title: 'Musical Pillars', description: 'The 56 musical pillars at Vittala Temple produce different musical notes when tapped gently.' },
      { id: 'hampi-f3', title: 'Largest Nandi', description: 'Hampi houses the second-largest monolithic Nandi statue in India, carved from a single boulder.' },
    ],
    latitude: 15.335, longitude: 76.46,
    visitingHours: '6:00 AM - 6:00 PM', entryFee: '₹40 (Indian), ₹600 (Foreigner)',
    qrCodeId: 'QR-HAMPI-001', rating: 4.8, reviews: 12450, isFavourite: false,
  },
  {
    id: 'mysore-palace',
    name: 'Mysore Palace', nameLocal: 'ಮೈಸೂರು ಅರಮನೆ',
    location: 'Mysuru', district: 'Mysuru', type: 'PALACE',
    shortDescription: 'The crown jewel of Karnataka — a breathtaking Indo-Saracenic palace.',
    description: 'Mysore Palace is a historical palace and a royal residence. It is the official residence of the Wadiyar dynasty and the seat of the Kingdom of Mysore.',
    history: 'The current palace was commissioned in 1897 after the old palace was destroyed in a fire. Completed in 1912 at a cost of ₹42 lakh.',
    architecture: 'Designed by British architect Henry Irwin, the palace blends Hindu, Muslim, Rajput, and Gothic styles. Features 145-foot five-story tower, marble domes, stained glass ceilings.',
    legends: 'The palace is said to be protected by the goddess Chamundeshwari, whose temple sits atop the Chamundi Hills visible from the palace.',
    imageUrl: 'https://images.unsplash.com/photo-1600112356915-089abb8fc71a?w=800',
    galleryImages: ['https://images.unsplash.com/photo-1657856855186-7cf4909a4f78?w=400'],
    facts: [
      { id: 'mysore-f1', title: '97,000 Light Bulbs', description: 'During Dussehra, the palace is illuminated with 97,000 light bulbs.' },
      { id: 'mysore-f2', title: 'Golden Throne', description: 'The palace houses a golden throne made of 200kg of 24-carat gold, used only during Dussehra.' },
      { id: 'mysore-f3', title: 'Secret Tunnels', description: 'Rumors persist of underground tunnels connecting the palace to Srirangapatna, 20km away.' },
    ],
    latitude: 12.3052, longitude: 76.6551,
    visitingHours: '10:00 AM - 5:30 PM', entryFee: '₹100 (Indian), ₹400 (Foreigner)',
    qrCodeId: 'QR-MYSORE-001', rating: 4.7, reviews: 18900, isFavourite: false,
  },
  {
    id: 'badami',
    name: 'Badami Cave Temples', nameLocal: 'ಬಾದಾಮಿ ಗುಹಾಲಯಗಳು',
    location: 'Badami, Bagalkote', district: 'Bagalkote', type: 'CAVE',
    shortDescription: 'Ancient rock-cut cave temples carved into red sandstone cliffs.',
    description: 'Badami Cave Temples are a complex of four Hindu and Jain cave temples. They are considered an example of Indian rock-cut architecture, especially Badami Chalukya architecture.',
    history: 'Built between the 6th and 8th centuries by the Chalukya dynasty. The Chalukyas made Badami their capital from 540 to 757 CE.',
    architecture: 'The four caves are carved into a sandstone cliff. Caves 1-3 are dedicated to Shiva and Vishnu, while Cave 4 is a Jain temple.',
    legends: 'The Agastya Lake below the caves was created when the sage Agastya prayed to Shiva to provide water. The lake never dries up even during severe droughts.',
    imageUrl: 'https://images.unsplash.com/photo-1695453628451-247b9478707c?w=800',
    galleryImages: [],
    facts: [
      { id: 'badami-f1', title: '18-Armed Nataraja', description: 'Cave 1 features a rare 18-armed Nataraja statue with 81 dance poses depicted around it.' },
      { id: 'badami-f2', title: 'Vishnu on Snake', description: 'Cave 3 contains a massive 6-foot statue of Vishnu seated on Adishesha, carved from a single rock.' },
      { id: 'badami-f3', title: 'Constant Temperature', description: 'The caves maintain a constant temperature of 22°C year-round due to the sandstone\'s thermal properties.' },
    ],
    latitude: 15.9187, longitude: 75.6768,
    visitingHours: '9:00 AM - 5:30 PM', entryFee: '₹35 (Indian), ₹550 (Foreigner)',
    qrCodeId: 'QR-BADAMI-001', rating: 4.6, reviews: 7800, isFavourite: false,
  },
  {
    id: 'belur-halebidu',
    name: 'Belur & Halebidu', nameLocal: 'ಬೇಲೂರು ಮತ್ತು ಹಳೆಬೀಡು',
    location: 'Hassan District', district: 'Hassan', type: 'TEMPLE',
    shortDescription: 'Twin temple towns with the finest Hoysala architecture in existence.',
    description: 'The Chennakeshava Temple at Belur and Hoysaleshwara Temple at Halebidu are masterpieces of intricate stone carving.',
    history: 'Built in the 12th century during Hoysala rule over 100 years.',
    architecture: 'Intricate soapstone carvings with no surface left undecorated. The Darpana Sundari and ceiling panel of Narasimha are world-famous.',
    legends: 'Soapstone softens when quarried and hardens over time, allowing impossibly detailed sculptures that last forever.',
    imageUrl: 'https://images.unsplash.com/photo-1601815264039-67c8ba1a7f98?w=800',
    galleryImages: [],
    facts: [
      { id: 'belur-f1', title: 'Infinite Pillar', description: 'One of the pillars in Belur temple was turned on a lathe — impossible for the 12th century.' },
      { id: 'belur-f2', title: 'No Two Alike', description: 'There are 650 elephant carvings at the base of Halebidu temple — no two are identical.' },
      { id: 'belur-f3', title: 'Floating Figures', description: 'Some sculptures appear to float because they are attached by only 1cm of stone to the background.' },
    ],
    latitude: 13.1631, longitude: 75.865,
    visitingHours: '7:30 AM - 7:30 PM', entryFee: 'Free',
    qrCodeId: 'QR-BELUR-001', rating: 4.9, reviews: 9200, isFavourite: false,
  },
  {
    id: 'gol-gumbaz',
    name: 'Gol Gumbaz', nameLocal: 'ಗೋಲ ಗುಂಬಜ್',
    location: 'Vijayapura', district: 'Vijayapura', type: 'MONUMENT',
    shortDescription: "The world's second-largest dome — an acoustic marvel of the Deccan.",
    description: "Gol Gumbaz is the mausoleum of Mohammed Adil Shah. The dome is 44 meters in external diameter, the second-largest dome ever built.",
    history: 'Completed in 1656. The whispering gallery under the dome is famous — even the faintest sound is echoed 7-10 times.',
    architecture: 'Cube structure supports a hemispherical dome with 38m internal diameter. The four minarets act as staircases to the gallery.',
    legends: 'Legend says the architect was buried alive inside the monument so he could never replicate the design.',
    imageUrl: 'https://images.unsplash.com/photo-1707063880573-b15e1f533d9f?w=800',
    galleryImages: [],
    facts: [
      { id: 'gol-f1', title: 'Whispering Gallery', description: 'A sound made at one side echoes 7-10 times — one of the finest acoustic marvels in the world.' },
      { id: 'gol-f2', title: 'No Wooden Beams', description: 'The massive dome is supported without any wooden or iron beams.' },
      { id: 'gol-f3', title: "Sultan's Curse", description: 'The architect was reportedly buried alive to prevent him creating a similar masterpiece elsewhere.' },
    ],
    latitude: 16.8302, longitude: 75.723,
    visitingHours: '6:00 AM - 6:00 PM', entryFee: '₹25 (Indian), ₹300 (Foreigner)',
    qrCodeId: 'QR-GOL-001', rating: 4.5, reviews: 6500, isFavourite: false,
  },
  {
    id: 'pattadakal',
    name: 'Pattadakal', nameLocal: 'ಪಟ್ಟದಕಲ್',
    location: 'Pattadakal, Bagalkote', district: 'Bagalkote', type: 'UNESCO',
    shortDescription: 'UNESCO World Heritage Site — coronation temple of Chalukya kings.',
    description: 'Pattadakal is a UNESCO World Heritage Site featuring monuments built in the 8th century during the Chalukya dynasty.',
    history: 'The Chalukyas built Pattadakal as their alternate capital and coronation site between 700-750 CE.',
    architecture: 'Features both Nagara (curvilinear shikhara) and Dravidian (pyramidal) temple styles side by side — unique in India.',
    legends: "The Pattadakal inscription describes the temple as a 'work of gods' — carved by divine architects.",
    imageUrl: 'https://images.unsplash.com/photo-1764426382627-8090f6e43395?w=800',
    galleryImages: [],
    facts: [
      { id: 'pattadakal-f1', title: 'Two Styles One Site', description: 'Only place in South India with Nagara-style temples alongside Dravidian temples.' },
      { id: 'pattadakal-f2', title: 'Queen Lokamahadevi', description: 'The Virupaksha Temple was commissioned by Queen Lokamahadevi — rare for ancient India.' },
      { id: 'pattadakal-f3', title: 'Storytelling in Stone', description: 'The temples have over 100 panels depicting stories from the Ramayana and Mahabharata.' },
    ],
    latitude: 15.9497, longitude: 75.8181,
    visitingHours: '6:00 AM - 6:00 PM', entryFee: '₹35 (Indian), ₹550 (Foreigner)',
    qrCodeId: 'QR-PATTA-001', rating: 4.7, reviews: 5800, isFavourite: false,
  },
  {
    id: 'shravanabelagola',
    name: 'Shravanabelagola', nameLocal: 'ಶ್ರವಣಬೆಳಗೊಳ',
    location: 'Hassan District', district: 'Hassan', type: 'JAIN',
    shortDescription: "World's largest monolithic stone statue — Gommateshwara Bahubali.",
    description: 'Shravanabelagola is home to the 18.8-meter tall monolithic statue of Bahubali (Gommateshwara), the tallest free-standing statue in ancient India.',
    history: 'The statue was built in 983 CE under Ganga dynasty minister Chavundaraya. Jain pilgrimage site for over 1,000 years.',
    architecture: 'Carved from a single granite rock on Vindyagiri Hill, the statue shows Bahubali in deep meditation (kayotsarga) with vines growing around him.',
    legends: "Every 12 years, the Mahamastakabhisheka festival involves anointing the statue with thousands of pots of milk, curd, saffron, and gold. Visible from 25km away.",
    imageUrl: 'https://images.unsplash.com/photo-1602670884688-abb78db59cf0?w=800',
    galleryImages: [],
    facts: [
      { id: 'shravan-f1', title: "World's Tallest Ancient Statue", description: 'At 18.8 meters, Gommateshwara was the world\'s tallest free-standing statue when built in 983 CE.' },
      { id: 'shravan-f2', title: 'Mahamastakabhisheka', description: 'Every 12 years, the statue is anointed with 1,000 pots of milk, saffron, and gold — witnessed by millions.' },
      { id: 'shravan-f3', title: 'Barefoot Pilgrimage', description: 'Pilgrims must remove shoes at the base of the hill and climb 614 rock-cut steps barefoot.' },
    ],
    latitude: 12.8561, longitude: 76.4897,
    visitingHours: '6:30 AM - 5:30 PM', entryFee: 'Free',
    qrCodeId: 'QR-SHRAVAN-001', rating: 4.8, reviews: 8700, isFavourite: false,
  },
  {
    id: 'srirangapatna',
    name: 'Srirangapatna', nameLocal: 'ಶ್ರೀರಂಗಪಟ್ಟಣ',
    location: 'Srirangapatna, Mandya', district: 'Mandya', type: 'FORT',
    shortDescription: "Island fortress — the last capital of Tipu Sultan, the Tiger of Mysore.",
    description: 'Srirangapatna is a historic island-town on the Kaveri river, best known as the capital of Tipu Sultan and the site of the Battle of Seringapatam in 1799.',
    history: 'In 1799, British forces defeated Tipu Sultan here, ending the Kingdom of Mysore.',
    architecture: 'Features massive fort walls, Daria Daulat Bagh (summer palace), and Gumbaz (mausoleum of Tipu Sultan and Hyder Ali).',
    legends: "Tipu Sultan died fighting at the fort walls. His last words were reportedly: 'Better to live one day as a tiger than a thousand years as a sheep.'",
    imageUrl: 'https://images.unsplash.com/photo-1566132127697-4524fea60007?w=800',
    galleryImages: [],
    facts: [
      { id: 'srir-f1', title: "Tipu's Last Stand", description: 'Tipu Sultan died fighting at the breach in the fort walls on 4 May 1799, outnumbered 3 to 1.' },
      { id: 'srir-f2', title: 'Dungeon of Death', description: 'The fort contains a notorious dungeon where British officers were imprisoned during Tipu\'s reign.' },
      { id: 'srir-f3', title: 'Rocket Pioneer', description: "Tipu Sultan invented iron-cased rockets — the precursor to modern military rockets." },
    ],
    latitude: 12.4186, longitude: 76.6953,
    visitingHours: '8:00 AM - 6:00 PM', entryFee: '₹5',
    qrCodeId: 'QR-SRIR-001', rating: 4.4, reviews: 7100, isFavourite: false,
  },
  {
    id: 'aihole',
    name: 'Aihole', nameLocal: 'ಐಹೊಳೆ',
    location: 'Aihole, Bagalkote', district: 'Bagalkote', type: 'TEMPLE',
    shortDescription: "The cradle of Indian temple architecture — 125 temples from the 4th to 12th century.",
    description: 'Aihole is an ancient village with over 125 temples representing the earliest experiments in Hindu temple architecture. Called the "birthplace of Indian architecture."',
    history: 'Built by the early Chalukyas from 450 CE onwards. Architects experimented with every style before standardizing Dravidian and Nagara forms.',
    architecture: 'The Durga Temple features an apsidal plan borrowed from Buddhist structures. The Lad Khan Temple is among the earliest stone temples in the Deccan.',
    legends: "The Aihole inscription written by court poet Ravikirti in 634 CE is one of India's most important historical documents.",
    imageUrl: 'https://images.unsplash.com/photo-1592543572024-c0b7abd70e47?w=800',
    galleryImages: [],
    facts: [
      { id: 'aihole-f1', title: "Temple Laboratory", description: 'Aihole is called the "laboratory of Indian architecture" — 125 temples testing different styles over 600 years.' },
      { id: 'aihole-f2', title: "Durga Temple", description: "The Durga Temple's apsidal plan is unique — it looks like a Buddhist chaitya but is dedicated to Shiva." },
      { id: 'aihole-f3', title: "634 CE Inscription", description: "Ravikirti's Aihole inscription mentions Kalidasa and provides key dates for ancient Indian history." },
    ],
    latitude: 15.9283, longitude: 75.8845,
    visitingHours: '6:00 AM - 6:00 PM', entryFee: '₹35 (Indian), ₹550 (Foreigner)',
    qrCodeId: 'QR-AIHOL-001', rating: 4.5, reviews: 4300, isFavourite: false,
  },
  {
    id: 'somanathapura',
    name: 'Somanathapura Kesava Temple', nameLocal: 'ಸೋಮನಾಥಪುರ ಕೇಶವ ದೇವಾಲಯ',
    location: 'Somanathapura, Mysuru', district: 'Mysuru', type: 'TEMPLE',
    shortDescription: "The most perfectly preserved Hoysala temple — a jewel box in stone.",
    description: 'The Kesava Temple at Somanathapura is considered the most complete and best-preserved Hoysala temple. Built in 1268 CE by general Somnath, it is dedicated to Vishnu.',
    history: 'Built in 1268 CE by Somnath, a general under Hoysala king Narasimha III. It is a trikuta (three-towered) temple on a stellate platform.',
    architecture: 'Three towers (shikharas) rise above the three sanctuaries. The outer walls have 6 horizontal bands of carvings — elephants, horses, scrolling foliage, geese, and friezes of mythological scenes.',
    legends: "The temple was built to house three forms of Vishnu: Keshava, Janardhana, and Venugopala. Legend says the sculptures were so lifelike that animals tried to eat the carved fruits.",
    imageUrl: 'https://images.unsplash.com/photo-1588416498932-6d4e3560d4b6?w=800',
    galleryImages: [],
    facts: [
      { id: 'soma-f1', title: 'Six Horizontal Friezes', description: 'The outer walls have 6 bands of carvings — elephants, horses, foliage, geese, scenes, and deities.' },
      { id: 'soma-f2', title: "No Mortar Used", description: 'The massive stone blocks are held together without any mortar — precision interlocking only.' },
      { id: 'soma-f3', title: 'Triple Sanctum', description: 'Three connected sanctuaries dedicated to three forms of Vishnu — unique in Hoysala architecture.' },
    ],
    latitude: 12.2654, longitude: 76.8976,
    visitingHours: '8:30 AM - 5:30 PM', entryFee: '₹35 (Indian), ₹550 (Foreigner)',
    qrCodeId: 'QR-SOMANATH-001', rating: 4.7, reviews: 6100, isFavourite: false,
  },
  {
    id: 'bangalore-palace',
    name: 'Bangalore Palace', nameLocal: 'ಬೆಂಗಳೂರು ಅರಮನೆ',
    location: 'Bengaluru', district: 'Bengaluru Urban', type: 'PALACE',
    shortDescription: "A Tudor-style palace inspired by Windsor Castle, built in 1887.",
    description: "Bangalore Palace is a royal palace featuring Tudor Revival architecture inspired by England's Windsor Castle. It was the private residence of the Wadiyar dynasty.",
    history: "Built in 1887 by Chamaraja Wadiyar. The grounds cover 454 acres and are used for concerts and cultural events today.",
    architecture: "Features Gothic style turrets, wood-paneled interiors with floral motifs, and a turreted structure reminiscent of Windsor Castle. Floors are tiled with English and Belgian tiles.",
    legends: "The Wadiyar family still legally owns the palace. The Indian government attempted to take over the palace, leading to a decades-long legal battle.",
    imageUrl: 'https://images.unsplash.com/photo-1569158222040-3a4fab1bc6f0?w=800',
    galleryImages: [],
    facts: [
      { id: 'bangp-f1', title: 'Windsor of Bangalore', description: "Built to resemble Windsor Castle — complete with Tudor-style turrets and Gothic windows." },
      { id: 'bangp-f2', title: 'Concert Venue', description: 'The palace grounds hosted international concerts including Rolling Stones and Elton John.' },
      { id: 'bangp-f3', title: 'Legal Battle', description: "The Wadiyar family's legal battle to retain ownership of the palace lasted over 30 years." },
    ],
    latitude: 12.9989, longitude: 77.5924,
    visitingHours: '10:00 AM - 5:30 PM', entryFee: '₹230 (Indian), ₹460 (Foreigner)',
    qrCodeId: 'QR-BANGP-001', rating: 4.3, reviews: 11200, isFavourite: false,
  },
  {
    id: 'tipu-summer-palace',
    name: "Tipu Sultan's Summer Palace", nameLocal: 'ಟಿಪ್ಪು ಸುಲ್ತಾನ್ ಬೇಸಿಗೆ ಅರಮನೆ',
    location: 'Bengaluru', district: 'Bengaluru Urban', type: 'PALACE',
    shortDescription: "An exquisite teak wood palace adorned with painted arches — the Tiger's retreat.",
    description: "Tipu Sultan's Summer Palace is a masterwork of Indo-Islamic architecture and fine wood carving. Built in 1791, it served as Tipu Sultan's summer retreat in Bengaluru.",
    history: "Built by Hyder Ali and completed by Tipu Sultan in 1791. The palace is also called Rash-e-Jannat — 'Abode of the Garden of Envy.'",
    architecture: "Constructed almost entirely of teak wood. Features ornate floral motifs, arched compartments, and gilded paintings of Tipu Sultan's military victories.",
    legends: "The palace walls depict Tipu Sultan's battles with the British East India Company. One painting shows British officers being flogged — evidence of Tipu's deep hatred for British colonialism.",
    imageUrl: 'https://images.unsplash.com/photo-1566132127697-4524fea60007?w=800',
    galleryImages: [],
    facts: [
      { id: 'tipup-f1', title: "Entire Teak Construction", description: "The palace is built almost entirely of teak — no stone walls. The wood has lasted 230+ years." },
      { id: 'tipup-f2', title: "Rash-e-Jannat", description: "Tipu Sultan named it 'Abode of the Garden of Envy' — his summer retreat from the heat of Mysore." },
      { id: 'tipup-f3', title: "War Paintings", description: "The walls depict Tipu's battles with the British. Some paintings were defaced after British victory in 1799." },
    ],
    latitude: 12.9607, longitude: 77.5762,
    visitingHours: '8:00 AM - 5:00 PM', entryFee: '₹15 (Indian), ₹200 (Foreigner)',
    qrCodeId: 'QR-TIPUP-001', rating: 4.2, reviews: 8900, isFavourite: false,
  },
  {
    id: 'coorg',
    name: 'Madikeri Fort', nameLocal: 'ಮಡಿಕೇರಿ ಕೋಟೆ',
    location: 'Madikeri, Kodagu', district: 'Kodagu', type: 'FORT',
    shortDescription: 'A hill fort in the Scotland of India with panoramic Western Ghats views.',
    description: 'Madikeri Fort is an imposing structure in the heart of Coorg district. Originally built in mud by Mudduraja, it was rebuilt in granite by Tipu Sultan.',
    history: 'The fort changed hands between the Kodavas, Tipu Sultan, and the British. The British added a church and a museum within its walls.',
    architecture: 'The fort features a two-story palace inside, a church built by the British, and a museum. The walls offer panoramic views of the misty Western Ghats.',
    legends: "Legend says the Kodava kings could communicate with spirits of ancestors during the monsoon mist at the nearby Raja's Seat.",
    imageUrl: 'https://images.unsplash.com/photo-1547908771-05259d82e2df?w=800',
    galleryImages: [],
    facts: [
      { id: 'coorg-f1', title: "Tipu's Secret Tunnel", description: 'A secret tunnel from the fort is believed to lead to the Straits of Malabar, 100km away.' },
      { id: 'coorg-f2', title: 'Church in a Mosque', description: "The British converted Tipu Sultan's mosque inside the fort into a church — still used today." },
      { id: 'coorg-f3', title: 'Kodava Weapons', description: 'The museum displays ancient Kodava weapons including the traditional peechekathi dagger.' },
    ],
    latitude: 12.4244, longitude: 75.7382,
    visitingHours: '9:00 AM - 5:00 PM', entryFee: '₹20',
    qrCodeId: 'QR-COORG-001', rating: 4.3, reviews: 4200, isFavourite: false,
  },
];

async function seed() {
  console.log(`Seeding ${sites.length} heritage sites to Firestore project: virasat-20dcb`);
  const batch = db.batch();

  for (const site of sites) {
    const ref = db.collection('sites').doc(site.id);
    batch.set(ref, site, { merge: true });
    console.log(`  ✓ Queued: ${site.name}`);
  }

  await batch.commit();
  console.log(`\n✅ Successfully seeded ${sites.length} sites!`);
  console.log('You can verify at: https://console.firebase.google.com/project/virasat-20dcb/firestore');
}

seed().catch(err => {
  console.error('❌ Seeding failed:', err.message);
  process.exit(1);
});
