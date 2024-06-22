import puppeteer from "puppeteer";
import {
  RGBToHSL,
  hslToColorName,
  rgbStringToArray,
  rgbToColorName,
} from "./utils.js";
import fs from "fs/promises";

const browser = await puppeteer.launch();
const page = await browser.newPage();
await page.goto(
  "https://www.ppgpaints.com/color/color-families/browse-all-colors"
);

const colors = await page.$eval(
  "section.article-section:nth-child(2)",
  (el) => {
    const anchorElements = Array.from(el.querySelectorAll("a"));

    return anchorElements.map((a) => {
      const painturl = a.href;
      const colorName = a.querySelector("h3").innerText ?? "undefined";
      const rgb = window.getComputedStyle(a).backgroundColor;
      // genericLabel = rgbToColorName(rgb[0], rgb[1], rgb[2]);
      const id = a.querySelector("span").innerText ?? "undefined";
      return {
        brand: "PPG",
        url: painturl,
        rgb,
        name: colorName,
        id,
      };
    });
    // return anchorElements.map((a) => a.href);
  }
);
if (colors) {
  // pre-processing
  const processedColors = colors.map((c) => {
    const rgb = rgbStringToArray(c.rgb);
    const hsl = RGBToHSL(rgb);
    const genericLabelHSL = hslToColorName(hsl);
    const genericLabelRGB = rgbToColorName(rgb);
    return {
      ...c,
      rgb,
      hsl,
      labelHSL: genericLabelHSL,
      labelRGB: genericLabelRGB,
    };
  });
  const dataString = JSON.stringify(processedColors, null, 2);
  try {
    await fs.writeFile("ppg.json", dataString, "utf8");
    console.log(
      `Successfully Fetched data. Found ${[processedColors.length]} colors`
    );
  } catch (err) {
    console.error("Error writing to file", err);
  }
} else {
  console.error("Something went wrong. Could not parse the page");
}

await browser.close();
