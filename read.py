import os

# define path of paste
pastePos = 'archive/aclImdb_v1/pos'
pasteNeg = 'archive/aclImdb_v1/neg'

# read all images
pathsPos = [ os.path.join(pastePos, name) for name in os.listdir(pastePos) ] 
pathsNeg = [ os.path.join(pasteNeg, name) for name in os.listdir(pasteNeg) ] 

paths = pathsPos + pathsNeg


f = open("archive/forRead.txt","w+")
for i in range(len(paths)):
     f.write(paths[i]+"\n")

f.close()