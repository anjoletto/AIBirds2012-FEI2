% SCAN_FILL
% ZAIN UL HASSAN <zainulhassan4@gmail.com>
%
% Matlab function to read the json files and their respective sprite sheets 
% from the game ANGRY BIRDS and save them in the matlab variable to be used later
%
% (x,y) = start point of flood fill
% new = new value to be filled
% old = old value to replace, zero in our case ie previously unchecked/clustered
% Returns the bodypixels of the detected object and updates the integerMap

function bodyPixels = scan_fill(x,y,new,old,width,height,detectedObject)

global objectArrayColors thresholdDistance integerMap img



% ------------- initialization ------------- 
bodyPixels=0;

AinThresholdColor=0;
DinThresholdColor=0; 
CinThresholdColor=0;

y1=0;
spanLeft=false;
spanRight=false;

stackPtr = 1;
stack(stackPtr,1:2)=[x,y];



% ------------- Modified Scanline fill ------------- 


while stackPtr>0     %while any seed still remains in the stack
    x= stack(stackPtr,1);	%get the values of the seed
    y= stack(stackPtr,2);
%        stack(stackPtr,:)	%print x,y 

    stackPtr=stackPtr-1;
    y1 = y;
    colorAbove = 1;	%check if the previous color is the same as current one

        while y1 > 0 && integerMap(y1,x) == old && colorAbove == 1	%loop vertically upwards from the seed to find the the boundry of the object ie some other color than the ones of object

				a(1) = img(y1,x,1);
				a(2) = img(y1,x,2);
				a(3) = img(y1,x,3);


				co=2;   
				colorAbove = 0;
				while co<=(objectArrayColors(detectedObject,1)+1) && colorAbove == 0
					
					b(1) = objectArrayColors(detectedObject,co,1);
					b(2) = objectArrayColors(detectedObject,co,2);
					b(3) = objectArrayColors(detectedObject,co,3);
                    
					dist = norm(double(a)-double(b)); %same as : sqrt( sum((double(a)-double(b)).^2));  
					    if dist<=thresholdDistance(detectedObject) 
						    colorAbove = 1;
					            y1=y1-1;
					    else
						    colorAbove = 0;
					    end
                    
					co=co+1;
				end %while color of the object


        end %end loop above
       
        y1=y1+1; %get above the boundry or 0 index

        spanLeft = false;
        spanRight = false;
        AinThresholdColor=1; % for initial entry in the loop
        while y1 <= height && integerMap(y1,x) == old && AinThresholdColor % Scanline loop for line y1 --- old & inThresholdColor are for boundry detection
            
    			
		AinThresholdColor = 0; % "a" is the current pixel being observed 
                CinThresholdColor = 0; % "c" is the left pixel from 'a'
		DinThresholdColor = 0; % "d" is the right pixel from 'a'
                
		a(1) = img(y1,x,1); %rgb of current pixel
		a(2) = img(y1,x,2);
		a(3) = img(y1,x,3);

                if x > 1
                    c(1) = img(y1,x-1,1); %rgb of left pixel
                    c(2) = img(y1,x-1,2);
                    c(3) = img(y1,x-1,3);
                end
                
                if x < width
                    d(1) = img(y1,x+1,1); %rgb of right pixel
                    d(2) = img(y1,x+1,2);
                    d(3) = img(y1,x+1,3);
                end


		co=2;   
		while co<=(objectArrayColors(detectedObject,1)+1) && (AinThresholdColor==0 || CinThresholdColor==0 || DinThresholdColor==0) 
			%loop through all supervised colors to see if a, c and d are in the object cluster or not
			b(1) = objectArrayColors(detectedObject,co,1); %rgb of the object's color
			b(2) = objectArrayColors(detectedObject,co,2);
			b(3) = objectArrayColors(detectedObject,co,3);
                    
			dist = sqrt( sum((double(a)-double(b)).^2));  %same as : norm(double(a)-double(b))
			if dist<=thresholdDistance(detectedObject) && AinThresholdColor==0
				%compare with last color/cluster - mark boundry	- boundry of sky not needed
				AinThresholdColor = 1;
				integerMap(y1,x) = new;
				bodyPixels=bodyPixels+1;
			end

			if x > 1
				dist = sqrt( sum((double(c)-double(b)).^2));  %same as : norm(double(a)-double(b))
				if dist<=thresholdDistance(detectedObject) 
					%compare with last color/cluster - mark boundry	- boundry of sky not needed
					CinThresholdColor = 1;
				end
			end
                    
			if x < width
				dist = sqrt( sum((double(d)-double(b)).^2));  %same as : norm(double(a)-double(b))
				if dist<=thresholdDistance(detectedObject) 
					%compare with last color/cluster - mark boundry	- boundry of sky not needed
					DinThresholdColor = 1;
				end
			end


			co=co+1;
		end %while color of the object

                
                
		if AinThresholdColor  % seed left and right pixels if required
			if ~spanLeft && x > 1 && integerMap(y1,x - 1) == old && CinThresholdColor
				stackPtr = stackPtr+1;
				stack(stackPtr,1:2)=[x-1,y1];
				spanLeft = true;
			elseif spanLeft && x > 1 && (integerMap(y1,x - 1) ~= old || ~CinThresholdColor) %or different color
				spanLeft = false;
			end

			if ~spanRight && x < width && integerMap(y1,x + 1) == old && DinThresholdColor
				stackPtr = stackPtr+1;
				stack(stackPtr,1:2)=[x+1,y1];
				spanRight = true;
			elseif spanRight && x < width && (integerMap(y1,x + 1) ~= old || ~DinThresholdColor)
				spanRight = false;
			end
		end
                
                
		y1=y1+1;  % in crement and print
	end %if while y1++ & intmap = old
     
end %while stack
