function finalScore = getScore(imageName)


imgFull = imread(imageName);


% ------------- In Game ------------- 

img = imgFull(20:55,size(imgFull,2)-205:size(imgFull,2)-5,:); % for both HD & SD


offsetTemplate = rgb2gray(img);
scoreArray=0;
successCount=0;
finalScore=0;

for digit=0:9

	template = imread(strcat(int2str(digit),'inGame.png'));
	template = rgb2gray(template);

	max_cc=1;

	while max_cc > 0.9

		% ------------- Template Matching ------------- 

		cc = normxcorr2(template,offsetTemplate); 
		[max_cc, imax] = max(abs(cc(:)));
		[ypeak, xpeak] = ind2sub(size(cc),imax(1));

		% ------------- returns ------------- 

		if max_cc > 0.9
			successCount=successCount+1;
			offsetTemplate(ypeak-size(template,1):ypeak , xpeak-size(template,2):xpeak)=0; %remove from image
			scoreArray(successCount,1) = digit; %add digit and position in array
			scoreArray(successCount,2) = xpeak;
		end

	end
end

% ------------- End Screen ------------- 
if successCount == 0 
clear img

	%Sizes HD: 425:470, 525:710 -45,185 -768,1244
	%Sizes SD: 265:295, 360:475 -30,115  -480,840
	img = imgFull( floor(0.55*size(imgFull,1)):floor(0.62*size(imgFull,1)) , floor(0.42*size(imgFull,2)):floor(0.58*size(imgFull,2)) , :); % for both HD & SD


	offsetTemplate = rgb2gray(img);
	scoreArray=0;
	successCount=0;
	finalScore=0;

	for digit=0:9
		template = imread(strcat(int2str(digit),'endScreen.png'));
		template = imresize(template,size(imgFull,1)/768);
		template = rgb2gray(template);

		max_cc=1;

		while max_cc > 0.9
			% ------------- Template Matching ------------- 

			cc = normxcorr2(template,offsetTemplate); 
			[max_cc, imax] = max(abs(cc(:)));
			[ypeak, xpeak] = ind2sub(size(cc),imax(1));
			% ------------- returns ------------- 
			if max_cc > 0.9
				successCount=successCount+1;
				offsetTemplate(ypeak-size(template,1):ypeak , xpeak-size(template,2):xpeak)=0; %remove from image
				scoreArray(successCount,1) = digit; %add digit and position in array
				scoreArray(successCount,2) = xpeak;
			end

		end

	end

end

% ------------- End Screen Full HD ------------- 
if successCount == 0 && size(imgFull,1)>1000
clear img

	img = imgFull( 580:630 , 860:1040 , :); % for both HD & SD


	offsetTemplate = rgb2gray(img);
	scoreArray=0;
	successCount=0;
	finalScore=0;

	for digit=0:9
		template = imread(strcat(int2str(digit),'endScreen.png'));
		template = imresize(template,(size(template,1)+3)/size(template,1));
		template = rgb2gray(template);

		max_cc=1;

		while max_cc > 0.9
			% ------------- Template Matching ------------- 

			cc = normxcorr2(template,offsetTemplate); 
			[max_cc, imax] = max(abs(cc(:)));
			[ypeak, xpeak] = ind2sub(size(cc),imax(1));
			% ------------- returns ------------- 
			if max_cc > 0.9
				successCount=successCount+1;
				offsetTemplate(ypeak-size(template,1):ypeak , xpeak-size(template,2):xpeak)=0; %remove from image
				scoreArray(successCount,1) = digit; %add digit and position in array
				scoreArray(successCount,2) = xpeak;
			end

		end

	end

end



% ------------- Calculate Score ------------- 
for i=1:successCount
	[C, I] = max(scoreArray(:,2));
	finalScore=finalScore+scoreArray(I,1)*10.^(i-1);
	scoreArray(I,:)=[]; % delete this digit
end
