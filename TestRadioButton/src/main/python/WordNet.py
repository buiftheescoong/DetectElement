 
from nltk.corpus import wordnet
 
syn1 = wordnet.synsets('hello')[0]
syn2 = wordnet.synsets('hi')[0]
print(syn1.wup_similarity(syn2))
 
sorted(syn1.common_hypernyms(syn2))
ref = syn1.hypernyms()[0]
print ("Self comparison : ", 
       syn1.shortest_path_distance(ref))
 
print ("Distance of hello from greeting : ", 
       syn1.shortest_path_distance(syn2))
 
print ("Distance of greeting from hello : ", 
       syn2.shortest_path_distance(syn1))
 
