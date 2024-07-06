import { readFile } from "fs/promises";
import { initializeApp } from "firebase/app";
import {
  getFirestore,
  connectFirestoreEmulator,
  writeBatch,
  doc,
} from "firebase/firestore";
const credentials = JSON.parse(
  await readFile(new URL("./firebase-auth.json", import.meta.url))
);

const app = initializeApp(credentials);
const db = getFirestore(app);

// debug
connectFirestoreEmulator(db, "127.0.0.1", 8080);

const files = [
  "benjaminMoore.json",
  "ppg.json",
  "behr.json",
  "kilz.json",
  "dunnEdwards.json",
  "valspar.json",
];

const paint_data = [];
for (const file of files) {
  const data = JSON.parse(
    await readFile(new URL(`./ProcessedData/${file}`, import.meta.url))
  );
  paint_data.push(data);
}
const merged_data = {};
for (const dataset of paint_data) {
  for (const paint of dataset) {
    const rgb = paint.rgb;
    const key = `${rgb[0]},${rgb[1]},${rgb[2]}`;
    const brand = paint.brand;
    const brand_data = {
      brand,
      url: paint.url,
      name: paint.name,
      id: paint.id,
    };
    if (!merged_data[key]) {
      merged_data[key] = {
        rgb,
        hsl: paint.hsl,
        labelHSL: paint.labelHSL,
        labelRGB: paint.labelRGB,
        companies: [],
      };
    }
    merged_data[key].companies.push(brand_data);
  }
}

let batch = writeBatch(db);
const collectionName = "paints";
let numDocuments = 0;
for (const [key, value] of Object.entries(merged_data)) {
  numDocuments += 1;
  const paintRef = doc(db, collectionName, key);
  batch.set(paintRef, value);
  if (numDocuments === 500) {
    await batch.commit();
    batch = writeBatch(db);
    numDocuments = 0;
  }
}

if (numDocuments > 0) {
  await batch.commit(db);
}
process.exit(0);
