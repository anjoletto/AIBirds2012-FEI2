% SEGMENTATION-BODY-PIXEL-LIMIT-INITIALIZATION
% ZAIN UL HASSAN <zainulhassan4@gmail.com>
%
% Matlab function to add the pixel limit each detected body can have from a given screenshot of the game ANGRY BIRDS 
% to detect supervised objects. 
%
% updates bodyPixelLimit
%
% To add another object - Copy and Paste the last line and increment the IDs. Then you may change the limits as desired
% bodyPixelsLimit(x,1) = minimum pixels; bodyPixelsLimit(x,2) =  maximum pixels

function segmentationBodyPixelLimitInitialization()

global objectArrayCount bodyPixelsLimit img


% ------------- initialization of Object Body Pixel Limits ------------- 

bodyPixelsLimit = zeros(objectArrayCount,2);
[row col dim] = size(img);

if (col<1000)
	% body pixel limits each object can have on SD
	bodyPixelsLimit(1,1) = 2; bodyPixelsLimit(1,2) = 10000000;   % min,max mass the body should have
	bodyPixelsLimit(2,1) = 0; bodyPixelsLimit(2,2) = 1000;   % min,max mass the body should have
	bodyPixelsLimit(3,1) = 10; bodyPixelsLimit(3,2) = 5000;   % min,max mass the body should have
	bodyPixelsLimit(4,1) = 2; bodyPixelsLimit(4,2) = 10000000;   % min,max mass the body should have
	bodyPixelsLimit(5,1) = 2; bodyPixelsLimit(5,2) = 30;   % min,max mass the body should have
	bodyPixelsLimit(6,1) = 30; bodyPixelsLimit(6,2) = 800;   % min,max mass the body should have
	bodyPixelsLimit(7,1) = 0; bodyPixelsLimit(7,2) = 2000000000;   % min,max mass the body should have
	bodyPixelsLimit(8,1) = 10; bodyPixelsLimit(8,2) = 5000;   % min,max mass the body should have
	bodyPixelsLimit(9,1) = 10; bodyPixelsLimit(9,2) = 5000;   % min,max mass the body should have
	bodyPixelsLimit(10,1) = 400; bodyPixelsLimit(10,2) = 10000;   % min,max mass the body should have
	bodyPixelsLimit(11,1) = 0; bodyPixelsLimit(11,2) = 1000;   % min,max mass the body should have
	bodyPixelsLimit(12,1) = 0; bodyPixelsLimit(12,2) = 1000;   % min,max mass the body should have
elseif (col<1400)

	% body pixel limits each object can have on HD
	bodyPixelsLimit(1,1) = 2; bodyPixelsLimit(1,2) = 10000000;   % min,max mass the body should have
	bodyPixelsLimit(2,1) = 0; bodyPixelsLimit(2,2) = 1000;   % min,max mass the body should have
	bodyPixelsLimit(3,1) = 10; bodyPixelsLimit(3,2) = 5000;   % min,max mass the body should have
	bodyPixelsLimit(4,1) = 2; bodyPixelsLimit(4,2) = 10000000;   % min,max mass the body should have
	bodyPixelsLimit(5,1) = 5; bodyPixelsLimit(5,2) = 30;   % min,max mass the body should have
	bodyPixelsLimit(6,1) = 60; bodyPixelsLimit(6,2) = 800;   % min,max mass the body should have
	bodyPixelsLimit(7,1) = 0; bodyPixelsLimit(7,2) = 2000000000;   % min,max mass the body should have
	bodyPixelsLimit(8,1) = 20; bodyPixelsLimit(8,2) = 5000;   % min,max mass the body should have
	bodyPixelsLimit(9,1) = 10; bodyPixelsLimit(9,2) = 5000;   % min,max mass the body should have
	bodyPixelsLimit(10,1) = 500; bodyPixelsLimit(10,2) = 10000;   % min,max mass the body should have
	bodyPixelsLimit(11,1) = 0; bodyPixelsLimit(11,2) = 1000;   % min,max mass the body should have
	bodyPixelsLimit(12,1) = 0; bodyPixelsLimit(12,2) = 1000;   % min,max mass the body should have

else
	% body pixel limits each object can have on Full HD
	bodyPixelsLimit(1,1) = 2; bodyPixelsLimit(1,2) = 10000000;   % min,max mass the body should have
	bodyPixelsLimit(2,1) = 0; bodyPixelsLimit(2,2) = 10000;   % min,max mass the body should have
	bodyPixelsLimit(3,1) = 10; bodyPixelsLimit(3,2) = 20000;   % min,max mass the body should have
	bodyPixelsLimit(4,1) = 2; bodyPixelsLimit(4,2) = 10000000;   % min,max mass the body should have
	bodyPixelsLimit(5,1) = 5; bodyPixelsLimit(5,2) = 30;   % min,max mass the body should have
	bodyPixelsLimit(6,1) = 60; bodyPixelsLimit(6,2) = 800;   % min,max mass the body should have
	bodyPixelsLimit(7,1) = 0; bodyPixelsLimit(7,2) = 2000000000;   % min,max mass the body should have
	bodyPixelsLimit(8,1) = 20; bodyPixelsLimit(8,2) = 20000;   % min,max mass the body should have
	bodyPixelsLimit(9,1) = 10; bodyPixelsLimit(9,2) = 20000;   % min,max mass the body should have
	bodyPixelsLimit(10,1) = 500; bodyPixelsLimit(10,2) = 25000;   % min,max mass the body should have
	bodyPixelsLimit(11,1) = 0; bodyPixelsLimit(11,2) = 10000;   % min,max mass the body should have
	bodyPixelsLimit(12,1) = 0; bodyPixelsLimit(12,2) = 10000;   % min,max mass the body should have

    
end
