/**
 * Import function triggers from their respective submodules:
 *
 * import {onCall} from "firebase-functions/v2/https";
 * import {onDocumentWritten} from "firebase-functions/v2/firestore";
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

import {setGlobalOptions} from "firebase-functions";
import {onRequest} from "firebase-functions/https";
import * as admin from "firebase-admin";
import QRCode from "qrcode";

admin.initializeApp();
const db = admin.firestore();

setGlobalOptions({maxInstances: 10});

/**
 * GET /generateQr?siteId=hampi
 * Returns a QR code PNG encoding the site's qrCodeId.
 */
export const generateQr = onRequest(async (req, res) => {
  const siteId = req.query.siteId as string;
  if (!siteId || typeof siteId !== "string" || siteId.length > 100) {
    res.status(400).send("Missing or invalid siteId parameter");
    return;
  }

  const doc = await db.collection("sites").doc(siteId).get();
  if (!doc.exists) {
    res.status(404).send("Site not found");
    return;
  }

  const qrValue = doc.data()?.qrCodeId || siteId;
  const pngBuffer = await QRCode.toBuffer(qrValue, {
    width: 512,
    margin: 2,
    type: "png",
  });

  res.set("Content-Type", "image/png");
  res.set("Cache-Control", "public, max-age=86400");
  res.send(pngBuffer);
});
