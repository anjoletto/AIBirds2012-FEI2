% SEGMENTATION-OBJECT-NAMES-INITIALIZATION
% ZAIN UL HASSAN <zainulhassan4@gmail.com>
%
% Matlab function to add body names each detected body has from a given screenshot of the game ANGRY BIRDS 
% to detect supervised objects. 
%
% updates objectNames
%
% To add another object - Add another object Name and increment id, then edit the string

function segmentationObjectNamesInitialization()

global objectNames




% ------------- initialization of Object Names ------------- 


% Names of the objects being detected

objectNames{1} = 'Sky';
objectNames{2} = 'Red Birds';
objectNames{3} = 'Wood';
objectNames{4} = 'Ground';
objectNames{5} = 'Trajectory';
objectNames{6} = 'Tap';
objectNames{7} = 'Pig';
objectNames{8} = 'Ice';
objectNames{9} = 'Stone';
objectNames{10} = 'Unbreakable Wood';
objectNames{11} = 'Blue Birds';
objectNames{12} = 'Yellow Birds';

