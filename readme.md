1.CCEL -Consistent Collective Entity Linking Algorithm
CCEL is the named entity disambiguation Algorithm,It identifies mentions of named entities (persons, organizations, locations, songs, products, ...)
 in English language text and links them to a unique identifier. Most names are ambiguous, especially family names, and CCEL resolves this ambiguity. 

2.Requirements
(1)Java 8
(2)A Mysql database to run. Might work with previous version but is untested.The machine CCEL runs on should have a reasonable amount of main memory. 
should run on a machine with more than 4GB of main memory.
(3)at the sametime ,you should download the wikipedia dump(http://download.wikipedia.org), and use the JWPL(http://www.boyunjian.com/v/softd/JWPL.html) to 
operate it, inlude import the wikidump to mysql database.
(4)stanford NER

3.Setting up the Entity Repository
 the detail process see the code of packet WikiDataBase,chage the path of datebase for yourself
 
4.The model of CCEL
(1) entity Recognition entityRecognize.java
 use the stanford NER to recognition the mentions in text, and use the rule-base method to Coreference Resolution
 
(2) Candidate Select
 use the JWPL tool to operate the database,and select the candidates refer to mention, which rank the top five,detail see Candidate.java
 
(3) referent graph construct(non-direct graph)
 use the candidates to construct the entities referent graph, using the google distance to computer the weight between them 
 
(4)Prior Confidence computation
 the model include three method:context similary-based, name similary-based, entity population.respective to packet Similarity,EditDistance and Prior
 
(5)PageRank Algorithm
 we use the pagerank algorithm to computer the confidence of node in referent graph,which the inital confidence is in model(4).
 
(6)Entity Linking
 see the packet EntityLinking, we achieve the entity linking algorithm base different prior confidence,and linking them to knowledge base.

 5.Example of CCEL
 Text:Radwanska isn't done, thanks to her win and Pennetta not claiming a set versus Sharapova, who topped the group with a perfect 3-0 record. Pennetta led 4-2 in the first.
 Mention:[Radwanska, Pennetta, Sharapova]
 Linking result:{Radwanska=https://en.wikipedia.org/wiki/Agnieszka Radwa¨½ska, Pennetta=https://en.wikipedia.org/wiki/Pennetta, Sharapova=https://en.wikipedia.org/wiki/Sharapova}

