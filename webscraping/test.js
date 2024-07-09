import { readFile } from "fs/promises";
import { initializeApp } from "firebase/app";
import {
  getFirestore,
  connectFirestoreEmulator,
  collection,
  and,
  where,
  query,
  getDocs,
  limit,
} from "firebase/firestore";
const credentials = JSON.parse(
  await readFile(new URL("./firebase-auth.json", import.meta.url))
);

const app = initializeApp(credentials);
const db = getFirestore(app);

// debug
connectFirestoreEmulator(db, "127.0.0.1", 8080);

const queryRGBObject = async (db, r, g, b) => {
  const collectionRef = collection(db, "paints");
  const q = query(
    collectionRef,
    and(
      where("rgb.r", "==", r),
      where("rgb.g", "==", g),
      where("rgb.b", "==", b)
    ),
    limit(10)
  );
  //   const q = query(collectionRef, where("labelHSL", "==", "cyan"), limit(10));
  const snapshot = await getDocs(q);
  snapshot.forEach((doc) => {
    console.log(`Document ${doc.id} data:`, doc.data());
  });
};

// Call the function with specific RGB values
await queryRGBObject(db, 0, 100, 119);

process.exit(0);
