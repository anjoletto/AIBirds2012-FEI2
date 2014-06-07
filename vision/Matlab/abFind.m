% ABFIND
% Stephen Gould <stephen.gould@anu.edu.au>
%
% Matlab function to find pigs, red birds, blue birds and yellow birds in an Angry Birds screenshot. Returns
% bounding boxes of all pigs and an optional mask. Bounding boxes are
% returned as an array with rows containing (x, y, width, and height)
% of each bounding box.
%

function [boxes, mask] = abFind(maxNum1, maxNum2, spriteID);

global img

% first quantize colours to 3-bit
img2 = img;
img2 = double(img2);
qimg = 64 * int32(floor(img2(:, :, 1) / 32)) + ...
    8 * int32(floor(img2(:, :, 2) / 32)) + ...
    int32(floor(img2(:, :, 3) / 32));


% find colours
mask = uint8(zeros(size(qimg)));
indx = find(or(qimg == maxNum1, qimg == maxNum2));
mask(indx) = 1;

% find connected components
[L, n] = connectedComponents(mask == 1);    

% join connected components whose bounding boxes overlap
% and remove small components
L2 = zeros(size(mask));
for i = 1:n,
    [y, x] = find(L == i);
    if (max(y) - min(y) < 2) continue; end;
    if (max(x) - min(x) < 2) continue; end;  
  
    L2(max([min(y) - 1, 1]):min([max(y) + 1, size(mask, 1)]), ... %increment 1px
	max([min(x) - 1, 1]):min([max(x) + 1, size(mask, 2)])) = 1;
end;
[L, n] = connectedComponents(L2);

% increment 10% in height
L2 = zeros(size(mask));
for i = 1:n,
    [y, x] = find(L == i);
    heightI = max(y) - min(y);	%increment 10% in height
    heightI10 = uint64(floor(heightI * 0.2));
    L2(max([min(y) - heightI10, 1]):min([max(y) + heightI10, size(mask, 1)]), ...
	min(x):max(x) ) = 1;
end;
[L, n] = connectedComponents(L2);


% find bounding boxes for new connected components
L2 = zeros(size(mask));
boxes = zeros(n, 4);
for i = 1:n,
    [y, x] = find(L == i);
    boxes(i, :) = [min(x), min(y), max(x) - min(x) + 1, max(y) - min(y) + 1];
    L2(min(y):max(y), min(x):max(x)) = 1;
end;
[L, n] = connectedComponents(L2);
mask = L;


%Blue Bird's Noise Filterring
if(strcmp(spriteID,'BIRD_BLUE_1'))
	k=1;
	for j = 1:size(boxes, 1),
		checkIndx = find(qimg(boxes(j, 2):boxes(j, 2)+boxes(j, 4)-1,boxes(j, 1):boxes(j, 1)+boxes(j, 3)-1) == maxNum1);
		checkIndx2 = find(qimg(boxes(j, 2):boxes(j, 2)+boxes(j, 4)-1,boxes(j, 1):boxes(j, 1)+boxes(j, 3)-1) == maxNum2);
		if (isempty(checkIndx) || isempty(checkIndx2))
			mask(boxes(j, 2):boxes(j, 2)+boxes(j, 4),boxes(j, 1):boxes(j, 1)+boxes(j, 3)) = 0;
		else
			boxes2(k, :) = boxes(j, :);
			k=k+1;
		end
	end;
	clear boxes;
	if(k==1)
		boxes = zeros(0, 4);
	else
		boxes=boxes2;
	end
end
