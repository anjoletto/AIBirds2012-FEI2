% SEGMENTATION
% ZAIN UL HASSAN <zainulhassan4@gmail.com>
%
% Matlab function to segment a given screenshot from the game ANGRY BIRDS 
% to detect supervised objects. 
%
% imageName = name of the image file; in string format
%
% Returns:
% img - the image
% integerMap - 2D matrix (size of the image), containing each entry (unique objectID) corresponding to the object that is on the given pixel 
% objectInstanceReferenceFinal - contains for each detected object their: objectID, clusterID (which group they belong to), body pixels that the particular object has

function [objectInstanceReferenceFinal, integerMap, img] = segmentation(imageName)

global objectArrayCount objectNames objectArrayColors objectArrayBaseColors thresholdDistance integerMap img objectsInstancesFoundTillNow objectInstanceReference bodyPixelsLimit birdDetails pigDetails blockDetails

% ------------- Image Read ------------- 

img = imread(imageName); %read the required image from java class

% check input type
if (~isa(img, 'uint8')), error('image must be of type uint8'); end;
if (size(img, 3) ~= 3), error('image must be 3-channel'); end;


% ------------- initialization + Supervised Values ------------- 

[row col dim] = size(img);
integerMap = zeros(row,col);	%input image sized array to store the objectIDs for the detected objects
objectsInstancesFoundTillNow = 0; %number of objects detected
integerMap10 = zeros(row,col);	
objectsInstancesFoundTillNow10 = 0; 


objectArrayCount = 12; %total objects being detected - increment if adding another object
segmentationObjectNamesInitialization(); %add object names
segmentationColorInitialization();  %add supervised colors of each objects 
segmentationThresholdInitialization(); %add threshold distances of each object
segmentationBodyPixelLimitInitialization(); %add body pixel limit of each object


load('spriteVariables.mat'); %read variables - 'birdDetails', 'pigDetails', 'blockDetails'

% ------------- Segmentation ------------- 

disp ('Please Wait For Segmentation');

o=10;
maxUW=0;
		%replicate object colors to image size matrix
		objectColorMatrix = repmat(objectArrayBaseColors(o,1), [row,col]);
		objectColorMatrix(:,:,2) = repmat(objectArrayBaseColors(o,2), [row,col]);
		objectColorMatrix(:,:,3) = repmat(objectArrayBaseColors(o,3), [row,col]);

		%calculate distance from object color to image pixels
		distancePartial = (double(img)-double(objectColorMatrix)).^2; %norm( double(img) - double(xx) );
		distancePartial = distancePartial(:,:,1)+distancePartial(:,:,2)+distancePartial(:,:,3);
		D(:,:,o) = sqrt( distancePartial ); %norm( double(img) - double(xx) );
	
		%scanfill to detect connected body of the object 
		[j,i]=find(D(:,:,o)<thresholdDistance(o));
		for c=1:size(i,1);
			if integerMap10(j(c),i(c))==0  %if pixel not traversed already
				objectsInstancesFoundTillNow10=objectsInstancesFoundTillNow10 +1;
				bodyPixels = 0;
				bodyPixels = scan_fill(i(c),j(c),objectsInstancesFoundTillNow10,0,col,row,o); %scanfill to get the complete object
				objectInstanceReference10(objectsInstancesFoundTillNow10,1)=o; %ie the number found is the objected noted
				objectInstanceReference10(objectsInstancesFoundTillNow10,2)=bodyPixels; %update body pixels
                if (maxUW<bodyPixels)
                    maxUW=bodyPixels;
                end
			end
        end

        
        integerMap10 = integerMap;
        integerMap = zeros(row,col);


        %basic noise removal + connection of slings
        for( i =1:size(objectInstanceReference10,1) )
            [r,c,v]=find(integerMap10==i);miny=min(r);minx=min(c);maxy=max(r);maxx=max(c);
            h=(maxy-miny);
            w=(maxx-minx);

            if(objectInstanceReference10(i,2)==maxUW)
                   SlingBW=zeros(size(integerMap10));
                   SlingBW(integerMap10==i) = 1;
                   SlingBW = agentEnhance(SlingBW, 10, h/4, 'top'); %connect the max UW with top parts
            end
            
            if( (objectInstanceReference10(i,2)<10) || (h<3) || (w<3))
                integerMap10( integerMap10 == i) = 0;
            end
            
        end
           integerMap10( SlingBW == 1) = i;

		%%%%%%remove sling band colors - which is same as brown ground too
		%%%%%%- this is for fixing sling height
%        slingBandBW = zeros(row,col);        
%         		%replicate object colors to image size matrix
%                 objectColorMatrix = repmat(objectArrayColors(o,4,1), [row,col]);
%                 objectColorMatrix(:,:,2) = repmat(objectArrayColors(o,4,2), [row,col]);
%                 objectColorMatrix(:,:,3) = repmat(objectArrayColors(o,4,3), [row,col]);
% 
%                 %calculate distance from object color to image pixels
%                 distancePartial = (double(img)-double(objectColorMatrix)).^2; %norm( double(img) - double(xx) );
%                 distancePartial = distancePartial(:,:,1)+distancePartial(:,:,2)+distancePartial(:,:,3);
%                 D(:,:,o) = sqrt( distancePartial ); %norm( double(img) - double(xx) );
% 
%                 %detect and remove band color > 100
%                 slingBandBW(find(D(:,:,o)<thresholdDistance(o)))=1;
%                 slingBandBW (slingBandBW==0) = NaN;
%                 [slingBandL, objectsInstancesFoundTillNow] = label(slingBandBW, 8);
%                 for i=1:objectsInstancesFoundTillNow
%                     x=0; x = find(slingBandL==i);
%                     bodyPixels = size(x,1)
%                     if bodyPixels<100
%                         slingBandBW (x)=0; 
%                     end
%                 end
% 
%                 integerMap10(slingBandBW==1)=0; %remove which are not part of sling
%                 






integerMap10(integerMap10>0)=1;
integerMap10(integerMap10==0) = NaN;



SlingBW = agentEnhance(integerMap10, 10, 1, 'all');
SlingBW (SlingBW==0) = NaN;
[integerMap, objectsInstancesFoundTillNow] = label(SlingBW, 8);

for i=1:objectsInstancesFoundTillNow
	x=0; x = find(integerMap==i);
	bodyPixels = size(x,1);
		objectInstanceReference(i,1)=o; %ie the number found is the objected noted
		objectInstanceReference(i,2)=bodyPixels; %update body pixels
end



%remaining segmentation
for o= 1:size(objectArrayBaseColors,1)

   if(o~=10)
	%separate segmentation for o = 2,7,11,12
	if (o==2)%red birds
		[boxes, mask] = abFind(birdDetails(15+1).maxColor1, birdDetails(15+1).maxColor2, birdDetails(15+1).id);
	elseif (o==7)%pigs
		[boxes, mask] = abFind(pigDetails(29+1).maxColor1, pigDetails(29+1).maxColor2, pigDetails(29+1).id);
	elseif (o==11)%blue birds
		[boxes, mask] = abFind(birdDetails(9+1).maxColor1, birdDetails(9+1).maxColor2, birdDetails(9+1).id);
	elseif (o==12)%yellow birds
		[boxes, mask] = abFind(birdDetails(28+1).maxColor1, birdDetails(28+1).maxColor2, birdDetails(28+1).id);
	else %for every other object

		%replicate object colors to image size matrix
		objectColorMatrix = repmat(objectArrayBaseColors(o,1), [row,col]);
		objectColorMatrix(:,:,2) = repmat(objectArrayBaseColors(o,2), [row,col]);
		objectColorMatrix(:,:,3) = repmat(objectArrayBaseColors(o,3), [row,col]);

		%calculate distance from object color to image pixels
		distancePartial = (double(img)-double(objectColorMatrix)).^2; %norm( double(img) - double(xx) );
		distancePartial = distancePartial(:,:,1)+distancePartial(:,:,2)+distancePartial(:,:,3);
		D(:,:,o) = sqrt( distancePartial ); %norm( double(img) - double(xx) );
	
		%scanfill to detect connected body of the object 
		[j,i]=find(D(:,:,o)<thresholdDistance(o));
		for c=1:size(i,1);
			if integerMap(j(c),i(c))==0  %if pixel not traversed already
				objectsInstancesFoundTillNow=objectsInstancesFoundTillNow +1;
				bodyPixels = 0;
				bodyPixels = scan_fill(i(c),j(c),objectsInstancesFoundTillNow,0,col,row,o); %scanfill to get the complete object


				[r,c,v]=find(integerMap==objectsInstancesFoundTillNow);miny=min(r);minx=min(c);maxy=max(r);maxx=max(c);

				%add if greater than 2px square, else remove trace
				if ( ( ((maxy-miny)>2) && ((maxx-minx)>2) )|| o==5)
					objectInstanceReference(objectsInstancesFoundTillNow,1)=o; %ie the number found is the objected noted
					objectInstanceReference(objectsInstancesFoundTillNow,2)=bodyPixels; %update body pixels
				else
					integerMap(integerMap==objectsInstancesFoundTillNow)=0;
					objectsInstancesFoundTillNow=objectsInstancesFoundTillNow - 1;

				end
			end
		end

	end



	if (o==2 || o==7 || o==11 || o==12)  %mark objects from the returned boxes and mask
		for boxi = 1:size(boxes, 1)
			objectsInstancesFoundTillNow=objectsInstancesFoundTillNow +1;
			objectInstanceReference(objectsInstancesFoundTillNow,1)=o; %ie the number found is the objected noted
			objectInstanceReference(objectsInstancesFoundTillNow,2)=boxes(boxi, 3)*boxes(boxi, 4); %size in pixels
			integerMap(boxes(boxi, 2):boxes(boxi, 2)+boxes(boxi, 4),boxes(boxi, 1):boxes(boxi, 1)+boxes(boxi, 3)) = objectsInstancesFoundTillNow; %integerMap Updation
		end
		clear mask boxes
	end

   end %end o=10

end




% ------------- Noise Removal ------------- 

objectInstanceReferenceFinal = segmentationObjectNoiseRemoval();

% ------------- Preparing Return Values ------------- 

if (~exist('objectInstanceReferenceFinal','var') || isempty(objectInstanceReferenceFinal))
	for j=1:objectsInstancesFoundTillNow
		objectInstanceReferenceFinal(j,1)=j;  %integerMap id
		objectInstanceReferenceFinal(j,2)=objectInstanceReference(j,1); %detected object
		objectInstanceReferenceFinal(j,3)=objectInstanceReference(j,2); %body pixels
	end
end
