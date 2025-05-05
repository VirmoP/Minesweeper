import matplotlib.pyplot as plt
import numpy as np
from scipy.optimize import curve_fit

excludeFalse = True
file = open("results_4w2530.txt")


times = []
size = []
mines = []

for line in file:
    info = line.strip().split(",")
    if len(info) < 3:
        break
    if info[5].split(" = ")[1] == "false" and excludeFalse:
        continue
    time = int(info[0].split(" ")[0])
    if time > 200000:
        continue 
    times.append(time)
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
    

print(x[4])

xr = []
yr = []


for i in range(6):
    xr.append([])
    yr.append([])
    if not x[i]:
        continue 
    s = x[i][0]
    sum = 0
    count = 0
    for j in range(len(x[i])):
        if (s == x[i][j]):
            sum += y[i][j]
            count += 1
        else:
            sum = sum/count
            xr[i].append(x[i][j-1])
            yr[i].append(sum)
            sum = y[i][j]
            s = x[i][j]
            count = 1

values = []

for i in range(len(xr)):
    values.append({})
    for j in range(len(xr[i])):
        if xr[i][j] in values[i]:
            temp = values[i].get(xr[i][j])
            temp = (temp + yr[i][j])/2
            values[i].update({xr[i][j]:temp})
        else:
            values[i].update({xr[i][j]:yr[i][j]})

print(len(values))

def polynomial(x, a, b):
    return a*x**b

def exponent(x, a, b):
    return a*np.exp(x*b*0.00001)


for i in range(0, len(values)-5):
    test = polynomial
    if len(values[i].items())<1:
        continue
    lists = sorted(values[i].items())
    x, y = zip(*lists)
    x = list(x)
    y = list(y)
    param, param_cov = curve_fit(test, x, y)
    plt.plot(x,y, color = 'tab:blue', label=''+str((i+1)*5)+'%')
    print("parameters for " + str((i+1)*5)+'%:')
    print(param)
    print(param_cov)
    
    ans = []
    
    for ele in x:
        #ans.append(test(ele, 1, 1))
        ans.append(test(ele, param[0], param[1]))
            
    plt.plot(x, ans, '--', color ='blue', label = str(round(param[0],3))+'*x^'+str(round(param[1],3)))
    
    



plt.xlabel("Laua suurus (ruutude arv)")
plt.ylabel("Genereerimise aeg (ms)")
plt.legend()
plt.tight_layout()

plt.show()
    
file.close()