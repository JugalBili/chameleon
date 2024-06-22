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
  "https://www.benjaminmoore.com/en-ca/paint-colours/colour-preview"
);

let colors = [];

while (true) {
  // Evaluate the background color and other data of all <div> elements inside the container
  const backgroundColors = await page.evaluate(() => {
    const element = document.querySelector(
      'div[data-testid="ColorGrid_GridContainer"]'
    );
    const childDivs = Array.from(element.children).filter(
      (child) => child.tagName === "DIV"
    );

    const colors = [];
    for (const child of childDivs) {
      const rgb = window.getComputedStyle(child).backgroundColor;
      const anchor = child.querySelector("a");
      const url = anchor.href;
      const anchorChildren = anchor.querySelectorAll("p");
      const colorName = anchorChildren[0].innerText.trim() || "undefined";
      const Id = anchorChildren[1].innerText.trim() || "undefined";

      colors.push({
        brand: "Benjamin Moore",
        url,
        rgb,
        name: colorName,
        Id,
      });
    }

    // Click the next button inside the browser context
    const nextButton = document.querySelector(
      'button[aria-label="Go to next page"]:not([disabled])'
    );
    if (!nextButton.ariaDisabled) {
      nextButton.click();
    }

    return colors;
  });

  colors = colors.concat(backgroundColors);
  // Check if there's a next button and wait for it
  try {
    await page.waitForSelector(
      'button[aria-label="Go to next page"]:not([disabled])',
      { timeout: 500 }
    );
  } catch (error) {
    // If next button is not found, break the loop
    break;
  }
}

// early termination issue. brain 2 fried to solve it
const backgroundColors = await page.evaluate(() => {
  const element = document.querySelector(
    'div[data-testid="ColorGrid_GridContainer"]'
  );
  const childDivs = Array.from(element.children).filter(
    (child) => child.tagName === "DIV"
  );

  const colors = [];
  for (const child of childDivs) {
    const rgb = window.getComputedStyle(child).backgroundColor;
    const anchor = child.querySelector("a");
    const url = anchor.href;
    const anchorChildren = anchor.querySelectorAll("p");
    const colorName = anchorChildren[0].innerText.trim() || "undefined";
    const Id = anchorChildren[1].innerText.trim() || "undefined";

    colors.push({
      brand: "Benjamin Moore",
      url,
      rgb,
      name: colorName,
      Id,
    });
  }
  return colors;
});

colors = colors.concat(backgroundColors);
if (colors.length) {
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
    await fs.writeFile("benjaminMoore.json", dataString, "utf8");
    console.log(
      `Successfully Fetched data. Found ${[processedColors.length]} colors`
    );
  } catch (err) {
    console.error("Error writing to file", err);
  }
} else {
  console.error("Something went wrong. Could not parse the page");
}
// console.log(await nextButton.getProperty("disabled"));
await browser.close();
