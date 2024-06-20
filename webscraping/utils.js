const colorNames = [
  { name: "black", rgb: [0, 0, 0] },
  { name: "white", rgb: [255, 255, 255] },
  { name: "red", rgb: [255, 0, 0] },
  //   { name: "lime", rgb: [0, 255, 0] },
  { name: "blue", rgb: [0, 0, 255] },
  { name: "yellow", rgb: [255, 255, 0] },
  //   { name: "cyan", rgb: [0, 255, 255] },
  //   { name: "magenta", rgb: [255, 0, 255] },
  //   { name: "silver", rgb: [192, 192, 192] },
  { name: "gray", rgb: [128, 128, 128] },
  //   { name: "maroon", rgb: [128, 0, 0] },
  //   { name: "olive", rgb: [128, 128, 0] },
  { name: "green", rgb: [0, 255, 0] },
  { name: "purple", rgb: [128, 0, 128] },
  //   { name: "teal", rgb: [0, 128, 128] },
  //   { name: "navy", rgb: [0, 0, 128] },
  { name: "brown", rgb: [88, 57, 39] },
];

// NOT PERFECT. It's good enough to the point where small errors can be corrected manually
export const rgbToColorName = (rgb) => {
  const [r, g, b] = rgb;
  let closestColor = null;
  let closestDistance = Infinity;

  colorNames.forEach((color) => {
    const [cr, cg, cb] = color.rgb;
    const distance = Math.sqrt(
      Math.pow(r - cr, 2) + Math.pow(g - cg, 2) + Math.pow(b - cb, 2)
    );

    if (distance < closestDistance) {
      closestDistance = distance;
      closestColor = color.name;
    }
  });

  return closestColor;
};

export const rgbStringToArray = (rgbString) => {
  // Extract the values inside the parentheses
  const match = rgbString.match(/rgb\((\d+),\s*(\d+),\s*(\d+)\)/);

  // If the string matches the expected format, return an array of numbers
  if (match) {
    const r = parseInt(match[1], 10);
    const g = parseInt(match[2], 10);
    const b = parseInt(match[3], 10);
    return [r, g, b];
  } else {
    return rgbString;
  }
};

export const RGBToHSL = (rgb) => {
  let [r, g, b] = rgb;
  // Make r, g, and b fractions of 1
  r /= 255;
  g /= 255;
  b /= 255;

  // Find greatest and smallest channel values
  let cmin = Math.min(r, g, b),
    cmax = Math.max(r, g, b),
    delta = cmax - cmin,
    h = 0,
    s = 0,
    l = 0;

  // Calculate hue
  // No difference
  if (delta === 0) h = 0;
  // Red is max
  else if (cmax === r) h = ((g - b) / delta) % 6;
  // Green is max
  else if (cmax === g) h = (b - r) / delta + 2;
  // Blue is max
  else h = (r - g) / delta + 4;

  h = Math.round(h * 60);

  // Make negative hues positive behind 360°
  if (h < 0) h += 360;

  // Calculate lightness
  l = (cmax + cmin) / 2;

  // Calculate saturation
  s = delta === 0 ? 0 : delta / (1 - Math.abs(2 * l - 1));

  // Multiply l and s by 100
  s = +(s * 100).toFixed(1);
  l = +(l * 100).toFixed(1);

  return [h, s, l];
};

export const hslToColorName = (hsl) => {
  const [h, s, l] = hsl;
  // Convert hue to degrees if it's not already
  if (h > 360) h = h % 360;

  // Handle grayscale
  if (s <= 10) {
    if (l < 20) return "black";
    if (l > 80) return "white";
    return "gray";
  }
  if (l === 100) return "white";
  if (l <= 5) return "black";

  let color = "unknown";

  if (h >= 0 && h < 15) {
    color = "red";
  } else if (h >= 15 && h < 30) {
    if (s <= 55) color = "brown";
    else color = "orange";
  } else if (h >= 30 && h < 90) {
    color = "yellow";
  } else if (h >= 90 && h < 150) {
    color = "green";
  } else if (h >= 150 && h < 210) {
    color = "cyan";
  } else if (h >= 210 && h < 270) {
    color = "blue";
  } else if (h >= 270 && h < 330) {
    color = "purple";
  } else if (h >= 330 && h < 360) {
    color = "red";
  }

  return color;
};
