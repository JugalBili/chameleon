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
  await readFile(new URL("kilz_raw.json", import.meta.url))
);

const processedData = [];
const BASE_URL = "https://www.kilz.com/color/";
const brand = "Kilz";
for (const paint of data) {
  const rgb = rgbStringToArray(paint.RGB);
  const id = paint["Color Number"];
  const name = paint["Color Name"];
  const url = BASE_URL + name.toLowerCase().split(" ").join("-") + "-" + id;
  const hsl = RGBToHSL(rgb);
  const labelHSL = hslToColorName(hsl);
  const labelRGB = rgbToColorName(rgb);
  processedData.push(
    createColorEntry(brand, url, rgb, id, name, hsl, labelHSL, labelRGB)
  );
}
const dataString = JSON.stringify(processedData, null, 2);
try {
  await fs.writeFile("./ProcessedData/kilz.json", dataString, "utf8");
  console.log(
    `Successfully Fetched data. Found ${[processedData.length]} colors`
  );
} catch (err) {
  console.error("Error writing to file", err);
}
