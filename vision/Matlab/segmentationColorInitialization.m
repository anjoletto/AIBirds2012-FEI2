% SEGMENTATION-COLOR-INITIALIZATION
% ZAIN UL HASSAN <zainulhassan4@gmail.com>
%
% Matlab function to add body colors each detected body can have from a given screenshot of the game ANGRY BIRDS 
% to detect supervised objects. 
%
% updates objectArrayColors objectArrayBaseColors 
%
% To add another object - Add another base color and increment id
% copy one object's color and follow it

function segmentationColorInitialization()

global objectArrayCount objectArrayColors objectArrayBaseColors 

% ------------- initialization of Supervised Colors ------------- 

objectArrayColors = zeros(objectArrayCount,8,3);


%base colors of all objects
objectArrayBaseColors(1,1:3) = [148,206,222];   %initial color of sky 
objectArrayBaseColors(2,1:3) = [214,0,45];   %initial color of red birds red
objectArrayBaseColors(3,1:3) = [226,145,38];   %initial color of wood
objectArrayBaseColors(4,1:3) = [10,19,57];   %initial color of blue ground
objectArrayBaseColors(5,1:3) = [255,255,255];   %initial color of trajectory
objectArrayBaseColors(6,1:3) = [243,243,243];   %initial color of tap
objectArrayBaseColors(7,1:3) = [109,226,73];   %initial color of pig
objectArrayBaseColors(8,1:3) = [99,194,245];   %initial color of ice
objectArrayBaseColors(9,1:3) = [160,160,160];   %initial color of stone
objectArrayBaseColors(10,1:3) = [202,151,94];   %initial color of sling/unbreakable wood
objectArrayBaseColors(11,1:3) = [99,170,197];   %initial color of blue bird
objectArrayBaseColors(12,1:3) = [241,219,32];   %initial color of yellow bird

%remaining colors information of all objects

objectArrayColors(1,1) = 6;   %total initial colors of the first object ie sky
objectArrayColors(1,2,1:3) = [148,206,222];   %initial color of sky
objectArrayColors(1,3,1:3) = [169,215,229];   %initial color of sky
objectArrayColors(1,4,1:3) = [206,233,242];   %initial color of sky
objectArrayColors(1,5,1:3) = [220,240,255];   %initial color of sky
objectArrayColors(1,6,1:3) = [187,203,241];   %initial color of sky
objectArrayColors(1,7,1:3) = [243,250,255];   %initial color of sky

objectArrayColors(2,1) = 11;   %total initial colors of the object ie red birds
objectArrayColors(2,2,1:3) = [214,0,45];   %initial color of red birds red
objectArrayColors(2,3,1:3) = [226,196,168];   %initial color of red birds bottom
objectArrayColors(2,4,1:3) = [206,179,153];   %initial color of red birds bottom
objectArrayColors(2,5,1:3) = [202,131,26];   %initial color of red birds dark beak
objectArrayColors(2,6,1:3) = [85,0,18];   %initial color of red birds borders
objectArrayColors(2,7,1:3) = [255,255,255];   %initial color of red birds eyes
objectArrayColors(2,8,1:3) = [30,25,30];   %initial color of red birds borders
objectArrayColors(2,9,1:3) = [0,0,0];   %initial color of red birds black border
objectArrayColors(2,10,1:3) = [251,186,31];   %initial color of red birds black beak
objectArrayColors(2,11,1:3) = [51,8,17];   %initial color of red birds black border
objectArrayColors(2,12,1:3) = [204,196,197];   %initial color of red birds eye shade grey

objectArrayColors(3,1) = 4;   %total initial colors of the object ie wood
objectArrayColors(3,2,1:3) = [226,145,38];   %initial color of wood
objectArrayColors(3,3,1:3) = [210,127,31];   %initial color of wood
objectArrayColors(3,4,1:3) = [255,190,99];   %initial color of wood
objectArrayColors(3,5,1:3) = [183,106,38];   %initial color of wood
%objectArrayColors(3,6,1:3) = [144,72,19];   %initial color of wood - border

objectArrayColors(4,1) = 7;   %total initial colors of the object ie ground
objectArrayColors(4,2,1:3) = [10,19,57];   %initial color of blue ground
objectArrayColors(4,3,1:3) = [21,32,83];   %initial color of blue ground
objectArrayColors(4,4,1:3) = [14,25,17];   %initial color of blue ground
objectArrayColors(4,5,1:3) = [31,45,103];   %initial color of blue ground
objectArrayColors(4,6,1:3) = [51,110,21];   %initial color of green ground
objectArrayColors(4,7,1:3) = [82,153,11];   %initial color of green ground
objectArrayColors(4,8,1:3) = [194,254,13];   %initial color of green ground

objectArrayColors(5,1) = 2;   %total initial colors of the object ie trajectory
objectArrayColors(5,2,1:3) = [255,255,255];   %initial color of trajectory
objectArrayColors(5,3,1:3) = [239,239,239];   %initial color of trajectory

objectArrayColors(6,1) = 2;   %total initial colors of the object ie tap
objectArrayColors(6,2,1:3) = [243,243,243];   %initial color of tap
objectArrayColors(6,3,1:3) = [220,220,220];   %initial color of tap

objectArrayColors(7,1) = 10;   %total initial colors of the object ie Pig
objectArrayColors(7,2,1:3) = [109,226,73];   %initial color of pig
objectArrayColors(7,3,1:3) = [165,233,0];   %initial color of pig nose
objectArrayColors(7,4,1:3) = [202,251,16];   %initial color of pig nose
objectArrayColors(7,5,1:3) = [116,182,6];   %initial color of pig nose
objectArrayColors(7,6,1:3) = [92,189,48];   %initial color of pig around eyes
objectArrayColors(7,7,1:3) = [138,202,0];   %initial color of pig border
objectArrayColors(7,8,1:3) = [111,228,74];   %initial color of pig border
objectArrayColors(7,9,1:3) = [0,0,0];   %initial color of pig border black
objectArrayColors(7,10,1:3) = [5,39,17];   %initial color of pig border black
objectArrayColors(7,11,1:3) = [135,190,107];   %initial color of pig border black

objectArrayColors(8,1) = 4;   %total initial colors of the object ie Ice
objectArrayColors(8,2,1:3) = [99,194,245];   %initial color of ice
objectArrayColors(8,3,1:3) = [113,206,248];   %initial color of ice
objectArrayColors(8,4,1:3) = [148,218,250];   %initial color of ice
objectArrayColors(8,5,1:3) = [130,209,248];   %initial color of ice
%objectArrayColors(8,3,1:3) = [231,247,254];   %initial color of ice-white shine, same as sky

objectArrayColors(9,1) = 3;   %total initial colors of the object ie stone
objectArrayColors(9,2,1:3) = [160,160,160];   %initial color of stone
objectArrayColors(9,3,1:3) = [130,130,130];   %initial color of stone
objectArrayColors(9,4,1:3) = [148,148,148];   %initial color of stone

objectArrayColors(10,1) = 4;   %total initial colors of the object ie sling/unbreakable wood
objectArrayColors(10,2,1:3) = [127,65,32];   %initial color of sling
objectArrayColors(10,3,1:3) = [166,112,53];   %initial color of sling
objectArrayColors(10,4,1:3) = [166,112,53];%[48,23,8];   %initial color of sling band
objectArrayColors(10,5,1:3) = [202,151,94];   %initial color of sling

objectArrayColors(11,1) = 2;   %total initial colors of the object ie Blue Birds
objectArrayColors(11,2,1:3) = [99,170,197];   %initial color of Blue Birds
objectArrayColors(11,3,1:3) = [255,174,0];   %initial color of Blue Birds

objectArrayColors(12,1) = 2;   %total initial colors of the object ie Yellow Birds
objectArrayColors(12,2,1:3) = [241,219,32];   %initial color of Yellow Birds
objectArrayColors(12,3,1:3) = [241,219,32];   %initial color of Yellow Birds
