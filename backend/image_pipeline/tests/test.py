import cv2
import numpy as np


# mask = cv2.imread("mask.jpg", cv2.IMREAD_GRAYSCALE)
# print(mask)
# mask = cv2.threshold(mask, 128, 255, cv2.THRESH_BINARY)[1]
# print(mask)
# print(type(mask))


# arr = np.array([[0,0,1,1], [1,1,0,0]])
# print(arr)
# arr = np.logical_not(arr).astype(int)
# print(arr)
# arr = (arr * 255).astype(np.uint8)
# print(type(arr))
# print(arr)


import cv2
import numpy as np
import skimage.exposure
import numpy
import sys

numpy.set_printoptions(threshold=sys.maxsize)

# specify desired bgr color for new face and make into array
desired_color = (228, 201, 159)
desired_color = np.asarray(desired_color, dtype=np.float64)

# create swatch
swatch = np.full((11, 9, 3), desired_color, dtype=np.uint8)

# read image
img = cv2.imread("img.jpg").astype(np.int16)
# print(img)

# read face mask as grayscale and threshold to binary
facemask = cv2.imread("img_greyscale_mask.jpg", cv2.IMREAD_GRAYSCALE)
facemask = cv2.threshold(facemask, 128, 255, cv2.THRESH_BINARY)[1]
# print(facemask)

# get average bgr color of face
ave_color = cv2.mean(img, mask=facemask)[:3]
print(ave_color)

# compute difference colors and make into an image the same size as input
diff_color = desired_color - ave_color
print(diff_color)
diff_color = np.full_like(img, diff_color, dtype=np.int16)
# print(diff_color)

# shift input image color
# cv2.add clips automatically
new_img = cv2.add(img, diff_color)
# print(new_img)

# antialias mask, convert to float in range 0 to 1 and make 3-channels
facemask = cv2.GaussianBlur(
    facemask, (0, 0), sigmaX=3, sigmaY=3, borderType=cv2.BORDER_DEFAULT
)
# print(type(facemask))
# facemask.tofile('facemask.txt', sep=", ", )
# np.savetxt('facemask.txt', facemask, delimiter=', ')
# print(facemask)
facemask = skimage.exposure.rescale_intensity(
    facemask, in_range=(100, 150), out_range=(0, 1)
).astype(np.float32)
# np.savetxt('facemask2.txt', facemask, delimiter=', ')
# print(facemask)
facemask = cv2.merge([facemask, facemask, facemask])
# print(facemask)

# combine img and new_img using mask
result = img * (1 - facemask) + new_img * facemask
result = result.clip(0, 255).astype(np.uint8)
# print(result)

# save result
# cv2.imwrite('test2_swatch.png', swatch)
cv2.imwrite("img_recolor_test.png", result)

# cv2.imshow('swatch', swatch)
# cv2.imshow('result', result)
# cv2.waitKey(0)
# cv2.destroyAllWindows()
