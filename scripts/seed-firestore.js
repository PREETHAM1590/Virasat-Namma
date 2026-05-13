#!/usr/bin/env node
/**
 * Virasat Firestore Seeder
 * Run: node seed-firestore.js
 * Requires: npm install firebase-admin
 */

const admin = require('firebase-admin');

// Initialize with default credentials (requires firebase login or GOOGLE_APPLICATION_CREDENTIALS)
admin.initializeApp({
  projectId: 'virasat-20dcb'
});

const db = admin.firestore();

// Seed data for Karnataka Heritage Sites
const sites = [
  {
    id: 'hampi',
    name: 'Hampi',
    nameLocal: 'ಹಂಪಿ',
    location: 'Hampi, Ballari',
    district: 'Ballari',
    type: 'UNESCO',
    description: 'Hampi is a UNESCO World Heritage Site located in east-central Karnataka. It was the capital of the Vijayanagara Empire in the 14th century. The ruins are a collection of heritage sites depicting the fine Dravidian style of art and architecture.',
    shortDescription: 'UNESCO World Heritage Site — the ruins of the glorious Vijayanagara Empire.',
    history: 'Founded in 1336 by Harihara I and Bukka Raya I, Hampi became the epicenter of the Vijayanagara Empire. By 1500 CE, it was the world\'s second-largest medieval-era city after Beijing. The empire fell in 1565 after the Battle of Talikota.',
    architecture: 'Hampi showcases Dravidian architecture with elements of Indo-Islamic influence. The Vittala Temple complex features the iconic stone chariot and musical pillars that produce melodic tones when struck.',
    legends: 'Legend says that Hampi was the monkey kingdom of Kishkindha from the Ramayana. The boulders are believed to be thrown by Hanuman during the battle between Vali and Sugriva.',
    imageUrl: 'https://images.unsplash.com/photo-1631986683754-7d511e03864d?w=800',
    galleryImages: [
      'https://images.unsplash.com/photo-1708067225451-e7fa52d3c014?w=400',
      'https://images.unsplash.com/photo-1696239105346-4e48185eb001?w=400',
      'https://images.unsplash.com/photo-1684830235389-60a88c4e626e?w=400'
    ],
    facts: [
      { id: 'hampi-f1', title: 'Stone Chariot Mystery', description: 'The iconic stone chariot in Vittala Temple was built to resemble a wooden chariot and cannot be moved — it\'s carved from a single granite block.' },
      { id: 'hampi-f2', title: 'Musical Pillars', description: 'The 56 musical pillars at Vittala Temple produce different musical notes when tapped gently.' },
      { id: 'hampi-f3', title: 'Largest Nandi', description: 'Hampi houses the second-largest monolithic Nandi statue in India, carved from a single boulder.' }
    ],
    latitude: 15.3350,
    longitude: 76.4600,
    visitingHours: '6:00 AM - 6:00 PM',
    entryFee: '\u20B940 (Indian), \u20B9600 (Foreigner)',
    qrCodeId: 'QR-HAMPI-001',
    audioGuideUrl: '',
    isFavourite: false,
    rating: 4.8,
    reviews: 12450
  },
  {
    id: 'mysore-palace',
    name: 'Mysore Palace',
    nameLocal: '\u0CAE\u0CC8\u0CB8\u0CC2\u0CB0\u0CC1 \u0C85\u0CB0\u0CAE\u0CA8\u0CC6',
    location: 'Mysuru',
    district: 'Mysuru',
    type: 'PALACE',
    description: 'Mysore Palace is a historical palace and a royal residence. It is the official residence of the Wadiyar dynasty and the seat of the Kingdom of Mysore. The palace is in the centre of Mysore, facing the Chamundi Hills.',
    shortDescription: 'The crown jewel of Karnataka — a breathtaking Indo-Saracenic palace.',
    history: 'The current palace was commissioned in 1897 after the old palace was destroyed in a fire during the wedding of Princess Jayalakshmi. It was completed in 1912 at a cost of \u20B942 lakh (about $30 million today).',
    architecture: 'Designed by British architect Henry Irwin, the palace blends Hindu, Muslim, Rajput, and Gothic styles. Features include 145-foot five-story tower, marble domes, stained glass ceilings, and intricate woodwork.',
    legends: 'The palace is said to be protected by the goddess Chamundeshwari, whose temple sits atop the Chamundi Hills visible from the palace grounds. The royal family still performs pooja here before major events.',
    imageUrl: 'https://images.unsplash.com/photo-1600112356915-089abb8fc71a?w=800',
    galleryImages: [
      'https://images.unsplash.com/photo-1657856855186-7cf4909a4f78?w=400',
      'https://images.unsplash.com/photo-1659126574791-13313aa424bd?w=400',
      'https://images.unsplash.com/photo-1647250945832-5b5aa0f6366e?w=400'
    ],
    facts: [
      { id: 'mysore-f1', title: '97,000 Light Bulbs', description: 'During Dussehra, the palace is illuminated with 97,000 light bulbs — a sight that attracts millions.' },
      { id: 'mysore-f2', title: 'Golden Throne', description: 'The palace houses a golden throne made of 200kg of 24-carat gold, used only during Dussehra.' },
      { id: 'mysore-f3', title: 'Secret Tunnels', description: 'Rumors persist of underground tunnels connecting the palace to Srirangapatna, 20km away.' }
    ],
    latitude: 12.3052,
    longitude: 76.6551,
    visitingHours: '10:00 AM - 5:30 PM',
    entryFee: '\u20B9100 (Indian), \u20B9400 (Foreigner)',
    qrCodeId: 'QR-MYSORE-001',
    isFavourite: false,
    rating: 4.7,
    reviews: 18900
  },
  {
    id: 'badami',
    name: 'Badami Cave Temples',
    nameLocal: '\u0CAC\u0CBE\u0CA6\u0CBE\u0CAE\u0CBF \u0C97\u0CC1\u0CB9\u0CBE\u0CB2\u0CAF\u0C97\u0CB3\u0CC1',
    location: 'Badami, Bagalkote',
    district: 'Bagalkote',
    type: 'CAVE',
    description: 'Badami Cave Temples are a complex of four Hindu and Jain cave temples located in Badami, a town in the Bagalkot district. They are considered an example of Indian rock-cut architecture, especially Badami Chalukya architecture.',
    shortDescription: 'Ancient rock-cut cave temples carved into red sandstone cliffs.',
    history: 'Built between the 6th and 8th centuries by the Chalukya dynasty, these caves represent some of the earliest examples of Deccan temple architecture. The Chalukyas made Badami their capital from 540 to 757 CE.',
    architecture: 'The four caves are carved into a sandstone cliff on the edge of a ravine. Caves 1-3 are dedicated to Shiva and Vishnu, while Cave 4 is a Jain temple. The architecture features elaborate pillar formations and ceiling panels.',
    legends: 'Local legend says the Agastya Lake below the caves was created when the sage Agastya prayed to Shiva to provide water. The lake never dries up even during severe droughts.',
    imageUrl: 'https://images.unsplash.com/photo-1695454140232-aee0c9310127?w=800',
    galleryImages: [],
    facts: [
      { id: 'badami-f1', title: '18-Armed Nataraja', description: 'Cave 1 features a rare 18-armed Nataraja statue with 81 dance poses depicted around it.' },
      { id: 'badami-f2', title: 'Vishnu on Snake', description: 'Cave 3 contains a massive 6-foot statue of Vishnu seated on Adishesha, carved from a single rock.' },
      { id: 'badami-f3', title: 'Precision Engineering', description: 'The caves maintain a constant temperature of 22\u00B0C year-round due to the sandstone\'s thermal properties.' }
    ],
    latitude: 15.9187,
    longitude: 75.6768,
    visitingHours: '9:00 AM - 5:30 PM',
    entryFee: '\u20B935 (Indian), \u20B9550 (Foreigner)',
    qrCodeId: 'QR-BADAMI-001',
    isFavourite: false,
    rating: 4.6,
    reviews: 7800
  },
  {
    id: 'belur-halebidu',
    name: 'Belur & Halebidu',
    nameLocal: '\u0CAC\u0CC7\u0CB2\u0CC2\u0CB0\u0CC1 \u0CAE\u0CA4\u0CCD\u0CA4\u0CC1 \u0CB9\u0CB3\u0CC6\u0CAC\u0CC0\u0CA1\u0CC1',
    location: 'Hassan District',
    district: 'Hassan',
    type: 'TEMPLE',
    description: 'Belur and Halebidu are temple towns famous for their Hoysala architecture. The Chennakeshava Temple at Belur and Hoysaleshwara Temple at Halebidu are masterpieces of intricate stone carving.',
    shortDescription: 'Twin temple towns with the finest Hoysala architecture in existence.',
    history: 'Built in the 12th century during Hoysala rule, these temples took over 100 years to complete. The Hoysalas were patrons of art and architecture, competing with the Cholas and Chalukyas.',
    architecture: 'The temples feature intricate soapstone carvings, with no surface left undecorated. The Darpana Sundari (lady with mirror) and the ceiling panel of Narasimha are world-famous sculptures.',
    legends: 'It is said that the sculptors worked with soapstone because it softens when quarried and hardens over time, allowing them to carve impossibly detailed sculptures that last forever.',
    imageUrl: 'https://images.unsplash.com/photo-1673779376455-b203cef903d1?w=800',
    galleryImages: [],
    facts: [
      { id: 'belur-f1', title: 'Infinite Pillar', description: 'One of the pillars in Belur temple was turned on a lathe — something considered impossible for the 12th century.' },
      { id: 'belur-f2', title: 'No Two Alike', description: 'There are 650 elephant carvings at the base of the Halebidu temple — no two are identical.' },
      { id: 'belur-f3', title: 'Floating Figures', description: 'Some sculptures appear to float because they are attached by only 1cm of stone to the background.' }
    ],
    latitude: 13.1631,
    longitude: 75.8650,
    visitingHours: '7:30 AM - 7:30 PM',
    entryFee: 'Free (Donations welcome)',
    qrCodeId: 'QR-BELUR-001',
    isFavourite: false,
    rating: 4.9,
    reviews: 9200
  }
];

async function seed() {
  console.log('Seeding Firestore sites...');
  const batch = db.batch();

  for (const site of sites) {
    const ref = db.collection('sites').doc(site.id);
    batch.set(ref, site);
    console.log(`  -> ${site.id}: ${site.name}`);
  }

  await batch.commit();
  console.log(`\nSuccessfully seeded ${sites.length} sites to Firestore.`);
  process.exit(0);
}

seed().catch(err => {
  console.error('Seeding failed:', err);
  process.exit(1);
});
