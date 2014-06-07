% CONNECTEDCOMPONENTS
% Stephen Gould <stephen.gould@anu.edu.au>
%

function [L, n] = connectedComponents(mask);

% run internal Matlab code if available
if (exist('bwlabel', 'file')),
    [L, n] = bwlabel(mask, 4);
    return;
end;

% find connected components by breadth first search
n = 0;
L = zeros(size(mask));
indx = find(mask);
while (~isempty(indx)),
    n = n + 1;
    frontier = indx(1);
    mask(indx(1)) = false;
    while (~isempty(frontier)),
        head = frontier(1);
        frontier = frontier(2:end);
        L(head) = n;
        [y, x] = ind2sub(size(mask), head);
        if ((y > 1) && mask(y - 1, x)),
            frontier = [frontier; sub2ind(size(mask), y - 1, x)];
            mask(y - 1, x) = false;
        end;
        if ((y < size(mask, 1)) && mask(y + 1, x)),
            frontier = [frontier; sub2ind(size(mask), y + 1, x)];
            mask(y + 1, x) = false;
        end;
        if ((x > 1) && mask(y, x - 1)),
            frontier = [frontier; sub2ind(size(mask), y, x - 1)];
            mask(y, x - 1) = false;
        end;
        if ((x < size(mask, 2)) && mask(y, x + 1)),
            frontier = [frontier; sub2ind(size(mask), y, x + 1)];
            mask(y, x + 1) = false;
        end;
    end;
    indx = find(mask);
end;
