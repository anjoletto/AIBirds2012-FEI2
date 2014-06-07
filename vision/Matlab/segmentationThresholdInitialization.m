% SEGMENTATION-OBJECT-THRESHOLD-INITIALIZATION
% ZAIN UL HASSAN <zainulhassan4@gmail.com>
%
% Matlab function to add color threshold each detected body can have from a given screenshot of the game ANGRY BIRDS 
% to detect supervised objects. ie how much difference in color is regarded as the shade of the same object
%
% updates thresholdDistance
%
% To add another object - Copy the last line and increment id, then edit the color difference you want to place in terms of radius of the volume of sphere of colors 
% that you want to include in the same object

function segmentationThresholdInitialization()

global objectArrayCount thresholdDistance img




% ------------- initialization of Object Threshold ------------- 

thresholdDistance = zeros(objectArrayCount);

bodyPixelsLimit = zeros(objectArrayCount,2);
[row col dim] = size(img);

if (col<1000)

% Color Threshold, ie how much difference in color is regarded as the shade of the same object on SD

thresholdDistance(1) = -20; % color difference ie: sphere radius
thresholdDistance(2) = 20; % color difference ie: sphere radius
thresholdDistance(3) = 17; % color difference ie: sphere radius
thresholdDistance(4) = -10; % color difference ie: sphere radius
thresholdDistance(5) = 3; % color difference ie: sphere radius
thresholdDistance(6) = 6; % color difference ie: sphere radius
thresholdDistance(7) = 28; %26; % color difference ie: sphere radius
thresholdDistance(8) = 15; % color difference ie: sphere radius
thresholdDistance(9) = 5; % color difference ie: sphere radius
thresholdDistance(10) = 25; % color difference ie: sphere radius
thresholdDistance(11) = 20; % color difference ie: sphere radius
thresholdDistance(12) = 20; % color difference ie: sphere radius

else


% Color Threshold, ie how much difference in color is regarded as the shade of the same object on HD

thresholdDistance(1) = -20; % color difference ie: sphere radius
thresholdDistance(2) = 20; % color difference ie: sphere radius
thresholdDistance(3) = 17; % color difference ie: sphere radius
thresholdDistance(4) = -10; % color difference ie: sphere radius
thresholdDistance(5) = 3; % color difference ie: sphere radius
thresholdDistance(6) = 6; % color difference ie: sphere radius
thresholdDistance(7) = 28; %26; % color difference ie: sphere radius
thresholdDistance(8) = 15; % color difference ie: sphere radius
thresholdDistance(9) = 5; % color difference ie: sphere radius
thresholdDistance(10) = 20; % color difference ie: sphere radius
thresholdDistance(11) = 20; % color difference ie: sphere radius
thresholdDistance(12) = 20; % color difference ie: sphere radius


end
