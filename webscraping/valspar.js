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
  await readFile(new URL("valspar_raw.json", import.meta.url))
);

const processedData = [];

const brand = "Valspar";
const url = "https://www.valspar.com/en/colors/browse-colors";
for (const paint of data) {
  const name = paint["Color Name"];
  if (processedData.some((obj) => obj.name === name)) {
    continue;
  }
  const rgb = rgbStringToArray(paint.RGB);
  const id = paint["Color Number"];
  const hsl = RGBToHSL(rgb);
  const labelHSL = hslToColorName(hsl);
  const labelRGB = rgbToColorName(rgb);
  processedData.push(
    createColorEntry(brand, url, rgb, id, name, hsl, labelHSL, labelRGB)
  );
}
const dataString = JSON.stringify(processedData, null, 2);
try {
  await fs.writeFile("./ProcessedData/valspar.json", dataString, "utf8");
  console.log(
    `Successfully Fetched data. Found ${[processedData.length]} colors`
  );
} catch (err) {
  console.error("Error writing to file", err);
}
