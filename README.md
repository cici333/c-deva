#C-DEVA


##Clusters of biological networks Detection, Evaluation, Visualization and Annotation

###Introduction

Clustering in protein interaction network is an important means for the identification of functional modules, which not only helps to understand the organization of biological systems, but also is of great significance to predict protein function. According to the fact that the clustering algorithms of protein interaction network are lack of effective analysis software, we design and implement a new analysis platform, which is named C-DEVA. C-DEVA is a scalable platform, in which a series of evaluation methods, such as recall, precision, sensitivity, specificity, p-value, and function enrichment, are implemented. Moreover, nine clustering algorithms MCODE, Coach, Monet, DPClus, FAG-EC, HC-PIN, IPC-MCE, IPCIPG-o, IPCIPG-n are developed in this platform. Even more important, not only the clustering results are visualized, but also the comparison results of multiple algorithms can be shown in this platform. This platform has a good scalability for the clustering algorithms and evaluation methods are implemented as plug-ins.

[Please download the manual.](https://drive.google.com/file/d/0B13gZNlVofBmU3FjWTl4d2Y2OXc/view?usp=sharing) 
New feature of version 2.0

C-DEVA integrates new multi-source biological information, such as biology-functional annotations, and gold standard complex sets, which are collected from latest datasets in major databases or related papers. The gold-standard data are integrated from latest updated protein complex sets in the MIPS database, SGD database, PCDq database and the recent influential papers, covering ten species and twenty-four datasets. The Gene Ontology and functional annotation information utilized in C-DEVA comes from Gene Ontology Consortium (GO), including the lasted datasets in nineteen database, covering twenty-nine species.

###Launching C-DEVA

C-DEVA is a Java application running on Windows platform. There are two versions for either 32-bit or 64-bit Windows: C-DEVA-32, C-DEVA-64.

Since C-DEVA is a Java application, the 64-bit or 32-bit Java Runtime Environment (JRE) is required to be preinstalled according to the operating system. If you are 64-bit Windows system, 64-bit JRE would be better. For the version of JRK, Java 6, 7 and 8 have all been tested, and C-DEVA is running well on them.
C-DEVA doesn’t need any installation. After download and unzip the achieved distribution file, you just need to double-click the C-DEVA.exe file, and the application will run directly.
Contact Information

If you any problems or suggestions of C-DEVA, please contact us at Email:
tangyu333@gmail.com, limin@mail.csu.edu.cn, jxwang@mail.csu.edu.cn.
