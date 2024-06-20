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
