function gameState = gameState(imageName)


imgFull = imread(imageName);
gameState = -1; %-1 = not found, 0 = failed, 1 = win, 2 = playing


% ------------- Playing ------------- 

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

if successCount > 0 
    gameState = 2; %playing
end
% ------------- Win ------------- 
if gameState == -1; 
    clear img

    img = imgFull( 1:floor(0.5*size(imgFull,1)) , floor(0.25*size(imgFull,2)):floor(0.75*size(imgFull,2)) , :); % for FHD, HD & SD - top half
	offsetTemplate = rgb2gray(img);

         if size(imgFull,2)<1000
                template = imread('levelWinSD.png');
         elseif size(imgFull,2)<1400
                template = imread('levelWinHD.png');
         else
                template = imread('levelWinFHD.png');
         end   

 		template = rgb2gray(template);
			cc = normxcorr2(template,offsetTemplate); 
			[max_cc, imax] = max(abs(cc(:)));
			[ypeak, xpeak] = ind2sub(size(cc),imax(1));
			% ------------- returns ------------- 
			if max_cc > 0.9
                gameState = 1;
            end
end

% ------------- Loose ------------- 
if gameState == -1; 
    clear img

    img = imgFull( 1:floor(0.5*size(imgFull,1)) , floor(0.25*size(imgFull,2)):floor(0.75*size(imgFull,2)) , :); % for FHD, HD & SD - top half
	offsetTemplate = rgb2gray(img);

         if size(imgFull,2)<1000
                template = imread('levelFailedSD.png');
         elseif size(imgFull,2)<1400
                template = imread('levelFailedHD.png');
         else
                template = imread('levelFailedFHD.png');
         end   

 		template = rgb2gray(template);
			cc = normxcorr2(template,offsetTemplate); 
			[max_cc, imax] = max(abs(cc(:)));
			[ypeak, xpeak] = ind2sub(size(cc),imax(1));
			% ------------- returns ------------- 
			if max_cc > 0.9
                gameState = 0;
            end


end

