package com.example.virasat.data.source

/**
 * Verified Google Street View panorama IDs for Karnataka heritage sites.
 * All IDs confirmed via Street View Metadata API on 2026-05-11.
 * Using panoramaId() instead of position() guarantees the exact viewpoint loads.
 */
object StreetViewData {

    /**
     * Maps heritage site ID → verified Street View panorama ID.
     * TalkingToursScreen uses this to start Street View at the exact monument entrance.
     */
    val panoramaIds: Map<String, String> = mapOf(
        // === UNESCO / Major monuments ===
        "hampi"                  to "CAoSFkNJSE0wb2dLRUlDQWdJQ0czX3VDWUE.",  // Hampi, 2020-02
        "mysore-palace"          to "CAoSFkNJSE0wb2dLRUlDQWdJRHlvT2FDTHc.",  // Mysore Palace, 2021-02
        "badami"                 to "T5PRgN0_y6bHkvXUJ_HYiQ",                 // Badami Caves, 2022-11 (Google)
        "belur-halebidu"         to "z5oX7tpLuvkQx_RfM1EMiw",                 // Belur, 2023-01 (Google)
        "gol-gumbaz"             to "m3axa9pCXLxKxxHQTMKCMw",                 // Gol Gumbaz, 2022-11 (Google)
        "coorg"                  to "CAoSFkNJSE0wb2dLRUlDQWdJRGFtS2UxQ3c.",  // Madikeri Fort, 2021-08
        "pattadakal"             to "CAoSFENJSE0wb2dLRUlDQWdJQzRfTnRC",      // Pattadakal, 2018-10
        "aihole"                 to "CAoSF0NJSE0wb2dLRUlDQWdJQzFsOGJldHdF",  // Aihole, 2023-12
        "srirangapatna"          to "QAHOVPf2AK89Wtfs3EWiHg",                 // Srirangapatna, 2025-11 (Google)
        "somanathapura"          to "j1s2I4N5N3ybemUY63m_uQ",                 // Somanathapura Temple, 2025-06 (Google)

        // === Palaces ===
        "bangalore-palace"       to "CAoSF0NJSE0wb2dLRUlDQWdJRDRvNXF3d2dF",  // Bangalore Palace, 2016-03
        "tipu-summer-palace"     to "CAoSF0NJSE0wb2dLRUlDQWdJRDY5Wi01eXdF",  // Tipu Summer Palace, 2021-10
        "lalitha-mahal"          to "gPS1OXMB2JivYQ9oLh5o2w",                 // Lalitha Mahal, 2025-06 (Google)

        // === Temples ===
        "iskcon-bangalore"       to "e7Q6zRLqqUsTCl87qdMZdw",                 // ISKCON Bangalore, 2026-02 (Google)
        "bull-temple"            to "CAoSFkNJSE0wb2dLRUlDQWdJQ2NySXl3Ync.",  // Bull Temple, 2019-12
        "shravanabelagola"       to "rCo6YPhWbyFTsOmMSC1Xzg",                 // Shravanabelagola, 2023-04 (Google)
        "amruthapura"            to "qcJCebES2wcvHljYH8uGaQ",                 // Amruthapura, 2025-11 (Google)
        "belavadi"               to "pmfjbaD-fAtsl6ynDyu3Pw",                 // Belavadi, 2022-12 (Google)
        "hoysala-temples-mosale" to "L6pcyDNqgfeKqTn6Hf1AnQ",                 // Mosale Hoysala, 2022-11 (Google)
        "halebeedu-museum"       to "z5oX7tpLuvkQx_RfM1EMiw",                 // Same area as Belur
        "nanjangudu"             to "CAoSFkNJSE0wb2dLRUlDQWdJREV2X0N0RFE.",  // Nanjangud, 2019-01
        "avani"                  to "5g1ynFih_JzQ8L16hYi5lQ",                 // Avani, 2023-10 (Google)
        "suthoor-matha"          to "6qb33dFsBfe5wqociaoNog",                 // Suthoor Matha, 2024-05 (Google)
        "kolar"                  to "UuTXfdpbvb65RwrDxCp9VA",                 // Kolar, 2023-01 (Google)
        "lakkundi"               to "CAoSFkNJSE0wb2dLRUlDQWdJRDI1T19XTUE.",  // Lakkundi, 2022-05
        "gadag"                  to "qmfxYWreN1eL6mZAtJF4qw",                 // Gadag, 2022-11 (Google)

        // === Forts ===
        "chitradurga"            to "CAoSF0NJSE0wb2dLRUlDQWdJRFR6TlQwZ1FF",  // Chitradurga Fort, 2024-05
        "bidar-fort"             to "CAoSFkNJSE0wb2dLRUlDQWdJRFZwNWk3TFE.",  // Bidar Fort, 2023-12
        "kittur-fort"            to "TabsTQzYbqoOy8O45ShkbA",                 // Kittur Fort, 2014-11
        "mirjan-fort"            to "CAoSFkNJSE0wb2dLRUlDQWdJRHVrdXpRWVE.",  // Mirjan Fort, 2022-08
        "gajendragad-fort"       to "yYfln7FcsD62BusC3Tmiew",                 // Gajendragad Fort, 2025-11 (Google)
        "manjarabad-fort"        to "CAoSFkNJSE0wb2dLRUlDQWdJRHFqWXJ0Ymc.",  // Manjarabad Fort, 2021-01
        "bellary-fort"           to "gydUH2w85KnQegvrX4gynA",                 // Bellary Fort, 2022-10 (Google)
        "gudibande-fort"         to "Iatfzvp2FksKHJmP_URriQ",                 // Gudibande Fort, 2023-11 (Google)
        "nagara-fort"            to "CAoSFkNJSE0wb2dLRUlDQWdJRHE1ZW05YlE.",  // Nagara Fort, 2017-11
        "medigeshi-fort"         to "pBXpmHRnafuGjjEd7ZtKGg",                 // Medigeshi Fort, 2024-05 (Google)
        "harihara"               to "CAoSF0NJSE0wb2dLRUlDQWdJQzRnNF9hM0FF",  // Harihara, 2018-12
        "sanganakallu"           to "6GGejCjQoV-cy7ZoSoaWgg",                 // Sanganakallu, 2025-12 (Google)
        "magadi"                 to "Jai8cd-35g_txsONPUZb5w",                 // Magadi, 2025-12 (Google)

        // === Bijapur / Vijayapura monuments ===
        "ibrahim-roza"           to "CAoSFkNJSE0wb2dLRUlDQWdJRGU3TGptRnc.",  // Ibrahim Roza, 2022-10
        "jumma-masjid-bijapur"   to "e9OZ_SVh6FkeK6v5eLIBMA",                 // Jumma Masjid, 2025-11 (Google)

        // === Hampi cluster ===
        "stone-chariot"          to "V7B1utf9qDILPqZhxVD_iA",                 // Vittala Temple / Stone Chariot, 2015-07 (Google)
        "anegundi"               to "CAoSFkNJSE0wb2dLRUlDQWdJRFd0TURRSEE.",  // Anegundi, 2022-03
        "vijayanagara"           to "CAoSFkNJSE0wb2dLRUlDQWdJQ0czX3VDWUE.",  // Same panorama area as Hampi

        // === Coastal / Western Karnataka ===
        "barkur"                 to "il18aeDcx_7AAX4y6FVKJw",                 // Barkur, 2023-09 (Google)
        "sonda"                  to "wIeQXtX45D6LB1xjOcGk2w",                 // Sonda, 2022-12 (Google)

        // === Keladi kingdom ===
        "keladi"                 to "lqOxjMm1zyIuVJBwVu7QoA",                 // Keladi, 2023-08 (Google)
        "ikkeri"                 to "CAoSFkNJSE0wb2dLRUlDQWdJQ21zNVNER3c.",  // Ikkeri, 2021-11
        "sagara"                 to "BZJ8po945cGc9zWarTlfQg",                 // Sagara, 2023-01 (Google)

        // === Religious / spiritual sites ===
        "bylakuppe"              to "4I04Fnxl5YsSSyd4mnxAYg",                 // Golden Temple Bylakuppe, 2024-06 (Google)
        "basavakalyana"          to "AfSnGZudx9g7paka3_VXkw",                 // Basavakalyana, 2022-11 (Google)
        "sri-sogala-kshetra"     to "GS95CSH0gATO_6l4337kcQ",                 // Sri Sogala Kshetra, 2024-04 (Google)

        // === Natural / unique sites ===
        "shettihalli-church"     to "8-AL8rlGiaKSKFDDkNBmXA",                 // Shettihalli Church, 2025-12 (Google)
        "big-banyan"             to "CAoSFkNJSE0wb2dLRUlDQWdJRG0ySUxXWUE.",  // Big Banyan Tree, 2022-01
    )
}
