===================================================
System Requirements: Hardware and Software
===================================================

Following System Requirements are needed for this algorithm to work:

1. Hardware which may support latest Operating System and Software execution apart from the smooth running of the web based game.
2. JVM and JRE installed and supported
3. Matlab installed
4. Eclipse IDE or any other java debugger installed
5. Latest version of a Web Browser Installed
6. Internet connection to run the website http://chrome.angrybirds.com/
7. Flash Player Installed to run the flash based game

===================================================
HOW TO:
===================================================

1)The zip file extracts the folder Matlab containing matlab code, javaAction Code and the javaMatlab Code for integration with the Matlab. Reference lib is also included inside
2) Add image 'im.png' in the Matlab Folder (sample images already added).
3) Change the path to the Matlab folder in the below line in mainFile.java (first line in the main function):
CreateVision myVision = new CreateVision("/students/u4923569/Downloads/AngryBirds/Matlab");  //directory path with matlab code files - for windows double backslashes are required instead of forward slash

4) Do point 3) similarly for javaAction's mainFile.java
5) The java folders contain a ref library, import it in your project. 


There is a Create Vision and Create Action class, which basically manages interaction. Sample Main Files have been added for your convenience for function calling. (you dont need toworry about the Create Vision Class or the Create Action files, just see the Main File.java to see how make the function calls)

// comments are placed, however for detailed explaination, see the documentation for return objects in java and the Matlab algorithm. 

Currently it is showing the bounding boxes around all the basic objects

Documentation is also provided. Please see the documentation if needed. 


===================================================
NOTES:
===================================================
1) Ground is turned off, Kindly increase the color distance in the "segmentation.m" file in the matlab folder as required if the ground segmentation is required:
thresholdDistance(4) = -1; % color difference ie: sphere radius

Same goes for other objects if you want to open or close some segmentation.

2) You need to have the have a game window open in a separate maximised browser (not TAB) with chrome.angrybirds.com running and a level displayed in HD in order to make a move successfully using the createAction class

3) If you want to change the noise criteria, ie the min or the max mass the object should have, then kindly change the following lines in the segmentation.m
bodyPixelsLimit(cluster,1) = min mass; bodyPixelsLimit(clusterID,2) = max mass;

Cheers,
Zain
