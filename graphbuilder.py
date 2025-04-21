import matplotlib.pyplot as plt
import numpy as np

file = open("results_CSPsimple4.txt")


times = []
size = []
mines = []

for line in file:
    info = line.strip().split(",")
    if len(info) < 3:
        break 
    times.append(int(info[0].split(" ")[0]))
    x = int(info[2].split(" = ")[1])
    y = int(info[3].split(" = ")[1])
    size.append(x*y)
    mines.append(round(int(info[4].split(" = ")[1])*100/(x*y)))


print(times[60],size[60],mines[60])

x = [[],[],[],[],[],[],[]]
y = [[],[],[],[],[],[],[]]


for i in range(len(times)):
    index = int((mines[i]/5)-1)
    x[index].append(size[i])
    y[index].append(times[i])
    

xr = [[],[],[],[],[],[],[]]
yr = [[],[],[],[],[],[],[]]


for i in range(4):
    s = x[i][0]
    sum = 0
    for j in range(len(x[i])):
        if (s == x[i][j]):
            sum += y[i][j]
        else:
            sum = sum/50
            xr[i].append(x[i][j-1])
            yr[i].append(sum)
            sum = 0
            s = x[i][j]

values = [{},{},{},{}]

for i in range(4):
    for j in range(len(xr[i])):
        if xr[i][j] in values[i]:
            temp = values[i].get(xr[i][j])
            temp = (temp + yr[i][j])/2
            values[i].update({xr[i][j]:temp})
        else:
            values[i].update({xr[i][j]:yr[i][j]})

print(len(values))

for i in range(len(values)):
    lists = sorted(values[i].items())
    x, y = zip(*lists)
    plt.plot(x,y, label=''+str((i+1)*5)+'%')
    
plt.title("parem tiitel")
plt.xlabel("Laua suurus (ruutude arv)")
plt.ylabel("Genereerimise aeg (ms)")
plt.legend()
plt.tight_layout()

plt.show()
    
file.close()