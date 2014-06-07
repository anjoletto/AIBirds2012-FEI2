% SEGMENTATION-OBJECT-NOISE-REMOVAL
% ZAIN UL HASSAN <zainulhassan4@gmail.com>
%
% Matlab function to remove the noise pixels with respect to body-pixel-limits from a given screenshot of the game ANGRY BIRDS 
% to detect supervised objects. 
%
% Returns: the Final Noise cleaned Reference Array of Objects - objectInstanceReferenceFinal
%

function objectInstanceReferenceFinal = segmentationObjectNoiseRemoval()

global objectsInstancesFoundTillNow objectInstanceReference bodyPixelsLimit integerMap img




% ------------- Noise Removal ------------- 

[row col dim] = size(img);

if (col<1000) 

	x=1;
	for j=1:objectsInstancesFoundTillNow
		if( objectInstanceReference(j,2)>bodyPixelsLimit(objectInstanceReference(j,1),1) && objectInstanceReference(j,2)<bodyPixelsLimit(objectInstanceReference(j,1),2) )  %if within body px limit
			if (size(find(integerMap==j))>0) %if object is found in integerMap
				[r,c,v]=find(integerMap==j);miny=min(r);minx=min(c);maxy=max(r);maxx=max(c);
				if ( miny>100 || (minx>200 && minx<620)) %ignoring buttons and score
    				if ( miny<(row-110) || minx>45 ) %ignoring zoom buttons
                        objectInstanceReferenceFinal(x,1)=j;  %integerMap id
                        objectInstanceReferenceFinal(x,2)=objectInstanceReference(j,1); %detected object
                        objectInstanceReferenceFinal(x,3)=objectInstanceReference(j,2); %body pixels
                        x=x+1;
                    end
				end
			end
		end
	end

elseif (col<1400)

	x=1;
	for j=1:objectsInstancesFoundTillNow
		if( objectInstanceReference(j,2)>bodyPixelsLimit(objectInstanceReference(j,1),1) && objectInstanceReference(j,2)<bodyPixelsLimit(objectInstanceReference(j,1),2) )  %if within body px limit
			if (size(find(integerMap==j))>0) %if object is found in integerMap
				[r,c,v]=find(integerMap==j);miny=min(r);minx=min(c);maxy=max(r);maxx=max(c);
				if ( miny>100 || (minx>315 && minx<1025)) %ignoring buttons and score
    				if ( miny<630 || minx>60 ) %ignoring zoom buttons
                        objectInstanceReferenceFinal(x,1)=j;  %integerMap id
                        objectInstanceReferenceFinal(x,2)=objectInstanceReference(j,1); %detected object
                        objectInstanceReferenceFinal(x,3)=objectInstanceReference(j,2); %body pixels
                        x=x+1;
                    end
				end
			end
		end
    end

    
elseif (col<2000)

	x=1;
	for j=1:objectsInstancesFoundTillNow
		if( objectInstanceReference(j,2)>bodyPixelsLimit(objectInstanceReference(j,1),1) && objectInstanceReference(j,2)<bodyPixelsLimit(objectInstanceReference(j,1),2) )  %if within body px limit
			if (size(find(integerMap==j))>0) %if object is found in integerMap
				[r,c,v]=find(integerMap==j);miny=min(r);minx=min(c);maxy=max(r);maxx=max(c);
				if ( miny>100 || (minx>315 && minx<1700)) %ignoring buttons and score
    				if ( miny<905 || minx>80 ) %ignoring zoom buttons
                        objectInstanceReferenceFinal(x,1)=j;  %integerMap id
                        objectInstanceReferenceFinal(x,2)=objectInstanceReference(j,1); %detected object
                        objectInstanceReferenceFinal(x,3)=objectInstanceReference(j,2); %body pixels
                        x=x+1;
                    end
				end
			end
		end
	end

end
