% READSPRITECOLORS
% ZAIN UL HASSAN <zainulhassan4@gmail.com>
%
% Matlab function to read the json files and their respective sprite sheets 
% from the game ANGRY BIRDS and save them in the matlab variable to be used later
%

function readSpriteColors()




% ------------- Sprites and Json Reading ------------- 

%read spritesheets
birdsSprite = imread('INGAME_BIRDS.png'); %read the required image from java class
pigsSprite = imread('INGAME_PIGS.png'); %read the required image from java class
blocksSprite = imread('INGAME_BLOCKS_BASIC.png'); %read the required image from java class

% check input type
if (~isa(birdsSprite, 'uint8')), error('birdsSprite must be of type uint8'); end;
if (size(birdsSprite, 3) ~= 3), error('birdsSprite must be 3-channel'); end;
if (~isa(pigsSprite, 'uint8')), error('pigsSprite must be of type uint8'); end;
if (size(pigsSprite, 3) ~= 3), error('pigsSprite must be 3-channel'); end;
if (~isa(blocksSprite, 'uint8')), error('blocksSprite must be of type uint8'); end;
if (size(blocksSprite, 3) ~= 3), error('blocksSprite must be 3-channel'); end;


%read json files
birdsJson = loadjson('INGAME_BIRDS.json');
pigsJson = loadjson('INGAME_PIGS.json');
blocksJson = loadjson('INGAME_BLOCKS_BASIC.json');


birdDetails = struct([]);
pigDetails = struct([]);
blockDetails = struct([]);


%save bird sprites
for birdi=0:birdsJson.spriteCount-1
	spriteName=sprintf('sprite_%d',birdi);
	spriteDetails = getfield(birdsJson, spriteName);
	sprite = birdsSprite(spriteDetails.y:spriteDetails.y+spriteDetails.height-1,spriteDetails.x:spriteDetails.x+spriteDetails.width-1,:);
	%sprite = imresize(sprite, 0.8); %resize;

	% quantize sprite colours to 3-bit
    sprite = double(sprite);
    qsprite = 64 * int32(floor(sprite(:, :, 1) / 32)) + ...
		8 * int32(floor(sprite(:, :, 2) / 32)) + ...
		int32(floor(sprite(:, :, 3) / 32));
    
		%find max 2 colors using histogram
		[rq cq dq] = size(qsprite);
		qsprite2=reshape(qsprite',1,rq*cq); 	
		colorRange = 0:1:511;
		[n,xout] = hist(qsprite2,colorRange);
		if (birdi>8),	n(1)=0; end; %do not remove black bird sprites' black color
		if (birdi<=8),	n(1)=0.7*n(1); end; %remove average transparency black color
		if (birdi<21 || birdi>=28 ),	n(512)=0; n(511)=0; end; %remove white from all birds instead of white bird
		[cc,ic] = max(n);
		maxNum1=xout(ic);
		n(ic)=0;
		[cc,ic] = max(n);
		maxNum2=xout(ic);



	%store data in variable
	birdDetails(birdi+1).id = spriteDetails.id;
	birdDetails(birdi+1).x = spriteDetails.x;
	birdDetails(birdi+1).y = spriteDetails.y;
	birdDetails(birdi+1).width = spriteDetails.width;
	birdDetails(birdi+1).height = spriteDetails.height;
	birdDetails(birdi+1).pivotx = spriteDetails.pivotx;
	birdDetails(birdi+1).pivoty = spriteDetails.pivoty;
	birdDetails(birdi+1).maxColor1=maxNum1;
	birdDetails(birdi+1).maxColor2=maxNum2;

    
end

%save pig sprites
for pigi=0:pigsJson.spriteCount-1
	spriteName=sprintf('sprite_%d',pigi);
	spriteDetails = getfield(pigsJson, spriteName);
	sprite = pigsSprite(spriteDetails.y:spriteDetails.y+spriteDetails.height-1,spriteDetails.x:spriteDetails.x+spriteDetails.width-1,:);
	%sprite = imresize(sprite, 0.8); %resize;

	% quantize sprite colours to 3-bit
    sprite = double(sprite);
    qsprite = 64 * int32(floor(sprite(:, :, 1) / 32)) + ...
		8 * int32(floor(sprite(:, :, 2) / 32)) + ...
		int32(floor(sprite(:, :, 3) / 32));

		%find max 2 colors using histogram
		[rq cq dq] = size(qsprite);
		qsprite2=reshape(qsprite',1,rq*cq); 	
		colorRange = 0:1:511;
		[n,xout] = hist(qsprite2,colorRange);
		n(1)=0; %remove sprites' black color
		[cc,ic] = max(n);
		maxNum1=xout(ic);
		n(ic)=0;
		[cc,ic] = max(n);
		maxNum2=xout(ic);



	%store data in variable
	pigDetails(pigi+1).id = spriteDetails.id;
	pigDetails(pigi+1).x = spriteDetails.x;
	pigDetails(pigi+1).y = spriteDetails.y;
	pigDetails(pigi+1).width = spriteDetails.width;
	pigDetails(pigi+1).height = spriteDetails.height;
	pigDetails(pigi+1).pivotx = spriteDetails.pivotx;
	pigDetails(pigi+1).pivoty = spriteDetails.pivoty;
	pigDetails(pigi+1).maxColor1=maxNum1;
	pigDetails(pigi+1).maxColor2=maxNum2;



end

%save block sprites
for blocki=0:blocksJson.spriteCount-1
	spriteName=sprintf('sprite_%d',blocki);
	spriteDetails = getfield(blocksJson, spriteName);
	sprite = blocksSprite(spriteDetails.y:spriteDetails.y+spriteDetails.height-1,spriteDetails.x:spriteDetails.x+spriteDetails.width-1,:);
	%sprite = imresize(sprite, 0.8); %resize;

	% quantize sprite colours to 3-bit
    sprite = double(sprite);
    qsprite = 64 * int32(floor(sprite(:, :, 1) / 32)) + ...
		8 * int32(floor(sprite(:, :, 2) / 32)) + ...
		int32(floor(sprite(:, :, 3) / 32));

		%find max 2 colors using histogram
		[rq cq dq] = size(qsprite);
		qsprite2=reshape(qsprite',1,rq*cq); 	
		colorRange = 0:1:511;
		[n,xout] = hist(qsprite2,colorRange);
		n(1)=0; %remove sprites' black color
		[cc,ic] = max(n);
		maxNum1=xout(ic);
		n(ic)=0;
		[cc,ic] = max(n);
		maxNum2=xout(ic);



	%store data in variable
	blockDetails(blocki+1).id = spriteDetails.id;
	blockDetails(blocki+1).x = spriteDetails.x;
	blockDetails(blocki+1).y = spriteDetails.y;
	blockDetails(blocki+1).width = spriteDetails.width;
	blockDetails(blocki+1).height = spriteDetails.height;
	blockDetails(blocki+1).pivotx = spriteDetails.pivotx;
	blockDetails(blocki+1).pivoty = spriteDetails.pivoty;
	blockDetails(blocki+1).maxColor1=maxNum1;
	blockDetails(blocki+1).maxColor2=maxNum2;



end


%store data in file
save('spriteVariables', 'birdDetails', 'pigDetails', 'blockDetails');

