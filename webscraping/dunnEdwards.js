import { readFile } from "fs/promises";
import fs from "fs/promises";
import {
  createColorEntry,
  hslToColorName,
  rgbStringToArray,
  rgbToColorName,
  RGBToHSL,
} from "./utils.js";

const data = JSON.parse(
  await readFile(new URL("dunnEdwards_raw.json", import.meta.url))
);

const processedData = [];

const brand = "Dunn Edwards";
const BASE_URL = "https://www.dunnedwards.com/colors/browser/";
for (const paint of data) {
  const rgb = rgbStringToArray(paint.RGB);
  const id = paint["Color Number"];
  const name = paint["Color Name"];
  const hsl = RGBToHSL(rgb);
  const url = BASE_URL + id.toLowerCase();
  const labelHSL = hslToColorName(hsl);
  const labelRGB = rgbToColorName(rgb);
  processedData.push(
    createColorEntry(brand, url, rgb, id, name, hsl, labelHSL, labelRGB)
  );
}
const dataString = JSON.stringify(processedData, null, 2);
try {
  await fs.writeFile("./ProcessedData/dunnEdwards.json", dataString, "utf8");
  console.log(
    `Successfully Fetched data. Found ${[processedData.length]} colors`
  );
} catch (err) {
  console.error("Error writing to file", err);
}
