% AGENT
% Zain ul Hassan
%
% The awsomest agent ever
% enhance cluster size


function L2 = agentEnhance(integerMap, clusterNum, enhacePx, direction); 

BW = integerMap;
BW(BW==0) = NaN;


%Enhance
[L, num] = label(BW,8); %8 ways
L2=zeros(size(L));


for(i=1:num)
	%get new values for bigger pig locations
	[r,c,v]=find(L==i);miny=min(r);minx=min(c);maxy=max(r);maxx=max(c); 
    if (strcmp(direction,'top'))
        fromX = minx;
        fromY = miny-enhacePx;
        toX = maxx;
        toY = maxy;
    else                                            %else expand in all directions
        fromX = minx-enhacePx;
        fromY = miny-enhacePx;
        toX = minx+(maxx-minx)+enhacePx;
        toY = miny+(maxy-miny)+enhacePx;        
    end
	
	%mark new values for pigs in L2
	L2(uint64(fromY):uint64(toY),uint64(fromX):uint64(toX))=1;
end

