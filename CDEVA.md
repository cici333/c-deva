C-DEVA: Clusters of biological networks Detection, Evaluation, Visualization and Annotation

# Introduction #

<p>Clustering in protein interaction network is an important means for the identification of functional modules, which not only helps to understand the organization of biological systems, but also is of great significance to predict protein function. According to the fact that the clustering algorithms of protein interaction network are lack of effective analysis software, we design and implement a new analysis platform, which is named C-DEVA. C-DEVA is a scalable platform, in which a series of evaluation methods, such as recall, precision, sensitivity, specificity, p-value, and function enrichment, are implemented. Moreover, nine clustering algorithms MCODE<a href='1.md'>1</a>, Coach<a href='2.md'>2</a>, Monet<a href='3.md'>3</a>, DPClus<a href='4.md'>4</a>, FAG-EC<a href='5.md'>5</a>, HC-PIN<a href='6.md'>6</a>, IPC-MCE<a href='7.md'>7</a>, IPCIPG-o<a href='8.md'>8</a>, and IPCIPG-n<a href='8.md'>8</a> are developed in this platform. Even more important, not only the clustering results are visualized, but also the comparison results of multiple algorithms can be shown in this platform. This platform has a good scalability for the clustering algorithms and evaluation methods are implemented as plug-ins.</p>


# Launching C-DEVA #

<p>C-DEVA is a Java application running on Windows platform. There are two versions for either 32-bit or 64-bit Windows: C-DEVA-32, C-DEVA-64. </p>

## System Requirements ##
<p>The system requirements for C-DEVA depend on the size of the networks the user wants to load, view and manipulate.</p>

CPU: 1GHz ~ as fast as possible <br>
Memory: 512M ~ 4GB+ <br>
Graphic Card: integrated graphics ~ discrete graphics <br>

<h2>Getting Started</h2>
<p>Since C-DEVA is a Java application, the 64-bit or 32-bit Java Runtime Environment (JRE) is required to be preinstalled according to the operating system. If you are 64-bit Windows system, 64-bit JRE would be better. For the version of JRE, Java 6, 7 and 8 have all been tested, and C-DEVA is running well on them. </p>
<p>C-DEVA doesn’t need any installation. After download and unzip the achieved distribution file, you just need to double-click the C-DEVA.exe file, and the application will run directly. </p>