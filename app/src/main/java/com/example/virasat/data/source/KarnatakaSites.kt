package com.example.virasat.data.source

import com.example.virasat.data.model.*

object KarnatakaSites {
    val allSites = listOf(
        HeritageSite(
            id = "hampi",
            name = "Hampi",
            nameLocal = "ಹಂಪಿ",
            location = "Hampi, Ballari",
            district = "Ballari",
            type = SiteType.UNESCO,
            shortDescription = "UNESCO World Heritage Site — the ruins of the glorious Vijayanagara Empire.",
            description = "Hampi is a UNESCO World Heritage Site located in east-central Karnataka. It was the capital of the Vijayanagara Empire in the 14th century. The ruins are a collection of heritage sites depicting the fine Dravidian style of art and architecture.",
            history = "Founded in 1336 by Harihara I and Bukka Raya I, Hampi became the epicenter of the Vijayanagara Empire. By 1500 CE, it was the world's second-largest medieval-era city after Beijing. The empire fell in 1565 after the Battle of Talikota.",
            architecture = "Hampi showcases Dravidian architecture with elements of Indo-Islamic influence. The Vittala Temple complex features the iconic stone chariot and musical pillars that produce melodic tones when struck.",
            legends = "Legend says that Hampi was the monkey kingdom of Kishkindha from the Ramayana. The boulders are believed to be thrown by Hanuman during the battle between Vali and Sugriva.",
            imageUrl = "https://images.unsplash.com/photo-1631986683754-7d511e03864d?w=800",
            galleryImages = listOf(
                "https://images.unsplash.com/photo-1708067225451-e7fa52d3c014?w=400",
                "https://images.unsplash.com/photo-1696239105346-4e48185eb001?w=400",
                "https://images.unsplash.com/photo-1684830235389-60a88c4e626e?w=400"
            ),
            facts = listOf(
                Fact("hampi-f1", "Stone Chariot Mystery", "The iconic stone chariot in Vittala Temple was built to resemble a wooden chariot and cannot be moved — it's carved from a single granite block."),
                Fact("hampi-f2", "Musical Pillars", "The 56 musical pillars at Vittala Temple produce different musical notes when tapped gently."),
                Fact("hampi-f3", "Largest Nandi", "Hampi houses the second-largest monolithic Nandi statue in India, carved from a single boulder.")
            ),
            latitude = 15.3350,
            longitude = 76.4600,
            visitingHours = "6:00 AM - 6:00 PM",
            entryFee = "₹40 (Indian), ₹600 (Foreigner)",
            qrCodeId = "QR-HAMPI-001",
            audioGuideUrl = "",
            rating = 4.8f,
            reviews = 12450
        ),
        HeritageSite(
            id = "mysore-palace",
            name = "Mysore Palace",
            nameLocal = "ಮೈಸೂರು ಅರಮನೆ",
            location = "Mysuru",
            district = "Mysuru",
            type = SiteType.PALACE,
            shortDescription = "The crown jewel of Karnataka — a breathtaking Indo-Saracenic palace.",
            description = "Mysore Palace is a historical palace and a royal residence. It is the official residence of the Wadiyar dynasty and the seat of the Kingdom of Mysore. The palace is in the centre of Mysore, facing the Chamundi Hills.",
            history = "The current palace was commissioned in 1897 after the old palace was destroyed in a fire during the wedding of Princess Jayalakshmi. It was completed in 1912 at a cost of ₹42 lakh (about $30 million today).",
            architecture = "Designed by British architect Henry Irwin, the palace blends Hindu, Muslim, Rajput, and Gothic styles. Features include 145-foot five-story tower, marble domes, stained glass ceilings, and intricate woodwork.",
            legends = "The palace is said to be protected by the goddess Chamundeshwari, whose temple sits atop the Chamundi Hills visible from the palace grounds. The royal family still performs pooja here before major events.",
            imageUrl = "https://images.unsplash.com/photo-1600112356915-089abb8fc71a?w=800",
            galleryImages = listOf(
                "https://images.unsplash.com/photo-1657856855186-7cf4909a4f78?w=400",
                "https://images.unsplash.com/photo-1659126574791-13313aa424bd?w=400",
                "https://images.unsplash.com/photo-1647250945832-5b5aa0f6366e?w=400"
            ),
            facts = listOf(
                Fact("mysore-f1", "97,000 Light Bulbs", "During Dussehra, the palace is illuminated with 97,000 light bulbs — a sight that attracts millions."),
                Fact("mysore-f2", "Golden Throne", "The palace houses a golden throne made of 200kg of 24-carat gold, used only during Dussehra."),
                Fact("mysore-f3", "Secret Tunnels", "Rumors persist of underground tunnels connecting the palace to Srirangapatna, 20km away.")
            ),
            latitude = 12.3052,
            longitude = 76.6551,
            visitingHours = "10:00 AM - 5:30 PM",
            entryFee = "₹100 (Indian), ₹400 (Foreigner)",
            qrCodeId = "QR-MYSORE-001",
            rating = 4.7f,
            reviews = 18900
        ),
        HeritageSite(
            id = "badami",
            name = "Badami Cave Temples",
            nameLocal = "ಬಾದಾಮಿ ಗುಹಾಲಯಗಳು",
            location = "Badami, Bagalkote",
            district = "Bagalkote",
            type = SiteType.CAVE,
            shortDescription = "Ancient rock-cut cave temples carved into red sandstone cliffs.",
            description = "Badami Cave Temples are a complex of four Hindu and Jain cave temples located in Badami, a town in the Bagalkot district. They are considered an example of Indian rock-cut architecture, especially Badami Chalukya architecture.",
            history = "Built between the 6th and 8th centuries by the Chalukya dynasty, these caves represent some of the earliest examples of Deccan temple architecture. The Chalukyas made Badami their capital from 540 to 757 CE.",
            architecture = "The four caves are carved into a sandstone cliff on the edge of a ravine. Caves 1-3 are dedicated to Shiva and Vishnu, while Cave 4 is a Jain temple. The architecture features elaborate pillar formations and ceiling panels.",
            legends = "Local legend says the Agastya Lake below the caves was created when the sage Agastya prayed to Shiva to provide water. The lake never dries up even during severe droughts.",
            imageUrl = "https://images.unsplash.com/photo-1695454140232-aee0c9310127?w=800",
            facts = listOf(
                Fact("badami-f1", "18-Armed Nataraja", "Cave 1 features a rare 18-armed Nataraja statue with 81 dance poses depicted around it."),
                Fact("badami-f2", "Vishnu on Snake", "Cave 3 contains a massive 6-foot statue of Vishnu seated on Adishesha, carved from a single rock."),
                Fact("badami-f3", "Precision Engineering", "The caves maintain a constant temperature of 22°C year-round due to the sandstone's thermal properties.")
            ),
            latitude = 15.9187,
            longitude = 75.6768,
            visitingHours = "9:00 AM - 5:30 PM",
            entryFee = "₹35 (Indian), ₹550 (Foreigner)",
            qrCodeId = "QR-BADAMI-001",
            rating = 4.6f,
            reviews = 7800
        ),
        HeritageSite(
            id = "belur-halebidu",
            name = "Belur & Halebidu",
            nameLocal = "ಬೇಲೂರು ಮತ್ತು ಹಳೆಬೀಡು",
            location = "Hassan District",
            district = "Hassan",
            type = SiteType.TEMPLE,
            shortDescription = "Twin temple towns with the finest Hoysala architecture in existence.",
            description = "Belur and Halebidu are temple towns famous for their Hoysala architecture. The Chennakeshava Temple at Belur and Hoysaleshwara Temple at Halebidu are masterpieces of intricate stone carving.",
            history = "Built in the 12th century during Hoysala rule, these temples took over 100 years to complete. The Hoysalas were patrons of art and architecture, competing with the Cholas and Chalukyas.",
            architecture = "The temples feature intricate soapstone carvings, with no surface left undecorated. The Darpana Sundari (lady with mirror) and the ceiling panel of Narasimha are world-famous sculptures.",
            legends = "It is said that the sculptors worked with soapstone because it softens when quarried and hardens over time, allowing them to carve impossibly detailed sculptures that last forever.",
            imageUrl = "https://images.unsplash.com/photo-1673779376455-b203cef903d1?w=800",
            facts = listOf(
                Fact("belur-f1", "Infinite Pillar", "One of the pillars in Belur temple was turned on a lathe — something considered impossible for the 12th century."),
                Fact("belur-f2", "No Two Alike", "There are 650 elephant carvings at the base of the Halebidu temple — no two are identical."),
                Fact("belur-f3", "Floating Figures", "Some sculptures appear to float because they are attached by only 1cm of stone to the background.")
            ),
            latitude = 13.1631,
            longitude = 75.8650,
            visitingHours = "7:30 AM - 7:30 PM",
            entryFee = "Free (Donations welcome)",
            qrCodeId = "QR-BELUR-001",
            rating = 4.9f,
            reviews = 9200
        ),
        HeritageSite(
            id = "gol-gumbaz",
            name = "Gol Gumbaz",
            nameLocal = "ಗೋಲ ಗುಂಬಜ್",
            location = "Vijayapura",
            district = "Vijayapura",
            type = SiteType.MONUMENT,
            shortDescription = "The world's second-largest dome — an acoustic marvel of the Deccan.",
            description = "Gol Gumbaz is the mausoleum of Mohammed Adil Shah, Sultan of Bijapur. The dome is 44 meters in external diameter, making it the second-largest dome ever built, next only to St. Peter's Basilica.",
            history = "Completed in 1656, this monument took 20 years to build. The whispering gallery under the dome is famous — even the faintest sound is echoed 7-10 times.",
            architecture = "The cube structure supports a hemispherical dome with a diameter of 38 meters internally. The four minarets at the corners act as staircases to the gallery. The building covers an area of 18,000 sq ft.",
            legends = "The Sultan wanted his tomb to surpass all others. Legend says the architect was buried alive inside the monument so he could never replicate the design elsewhere.",
            imageUrl = "https://images.unsplash.com/photo-1707063880573-b15e1f533d9f?w=800",
            facts = listOf(
                Fact("gol-f1", "Whispering Gallery", "A sound made at one side of the gallery echoes 7-10 times, making it one of the finest acoustic marvels in the world."),
                Fact("gol-f2", "No Wooden Beams", "The massive dome is supported without any wooden or iron beams — pure structural engineering genius."),
                Fact("gol-f3", "Sultan's Curse", "Legend says the architect was buried alive to prevent him from creating a similar masterpiece for another ruler.")
            ),
            latitude = 16.8302,
            longitude = 75.7230,
            visitingHours = "6:00 AM - 6:00 PM",
            entryFee = "₹25 (Indian), ₹300 (Foreigner)",
            qrCodeId = "QR-GOL-001",
            rating = 4.5f,
            reviews = 6500
        ),
        HeritageSite(
            id = "coorg",
            name = "Madikeri Fort",
            nameLocal = "ಮಡಿಕೇರಿ ಕೋಟೆ",
            location = "Madikeri, Kodagu",
            district = "Kodagu",
            type = SiteType.FORT,
            shortDescription = "A hill fort in the Scotland of India with panoramic Western Ghats views.",
            description = "Madikeri Fort is an imposing structure in the heart of Coorg district. Originally built in mud by Mudduraja in the 17th century, it was rebuilt in granite by Tipu Sultan who renamed the fort Jaffarabad.",
            history = "The fort changed hands between the Kodavas, Tipu Sultan, and the British. The British added a church and a museum within its walls. The fort stands as a testament to the region's tumultuous history.",
            architecture = "The fort features a two-story palace inside, a church built by the British, and a museum. The walls offer panoramic views of the misty Western Ghats and coffee plantations.",
            legends = "The nearby Raja's Seat is where kings watched sunsets. Legend says the Kodava kings could communicate with spirits of ancestors during the monsoon mist.",
            imageUrl = "https://images.unsplash.com/photo-1570168007204-dfb528c6958f?w=800",
            facts = listOf(
                Fact("coorg-f1", "Tipu's Secret Tunnel", "A secret tunnel from the fort is believed to lead to the Straits of Malabar, 100km away."),
                Fact("coorg-f2", "Church in a Mosque", "The British converted Tipu Sultan's mosque inside the fort into a church — still used today."),
                Fact("coorg-f3", "Kodava Weapons", "The museum displays ancient Kodava weapons including the traditional peechekathi dagger.")
            ),
            latitude = 12.4244,
            longitude = 75.7382,
            visitingHours = "9:00 AM - 5:00 PM",
            entryFee = "₹20",
            qrCodeId = "QR-COORG-001",
            rating = 4.3f,
            reviews = 4200
        )
    )

    val districts = allSites.map { it.district }.distinct().sorted()
    val siteTypes = SiteType.values().toList()
}
