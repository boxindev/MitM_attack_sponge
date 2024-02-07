﻿# coding=UTF-8
AllPossiblesbox = {(0,0,0,0), (0,0,1,0), (0,1,0,0), (0,1,1,1), (1,0,0,0), (1,0,1,1), (1,1,0,1), (1,1,1,0)}

colormap=[0,3,6,2,7]

def buildWholeVectorSpace():
	space = []
	for x1 in {0,1}:
		for x2 in {0,1}:
			for x3 in {0,1}:
				for x4 in {0,1}:
					space.append((x1,x2,x3,x4))
	return set(space)



def build_Impossible():
	return (buildWholeVectorSpace() - AllPossiblesbox)
        #return (buildWholeVectorSpace())


def isSat(point, ineq):
	assert(len(point) == 4)
	assert(len(ineq) == 5)

	temp = point[0]*ineq[0] + point[1]*ineq[1] + point[2]*ineq[2] + point[3]*ineq[3] + ineq[4]
	return ( temp >= 0)

def collectCuttOffs(points, ineq):
	result = []
	for x in points:
		if (not isSat(x, ineq)):
			result.append(x)

	return set(result)


def best_Cutting_Ineq(points, ineqs):
	best = 0
	temp = 0
	for ineq in ineqs:
		number_of_removed_points = len(collectCuttOffs(points, ineq))
		if number_of_removed_points > temp:
			temp = number_of_removed_points
			best = ineq

	return best, collectCuttOffs(points, best)


def greedy_Seclection(ineqs, N, base_points):
	current_best = 0
	current_base = base_points
	current_ineqs = ineqs

	result = []

	for i in range(0, N):
		current_best = best_Cutting_Ineq(current_base, current_ineqs)
		current_base = current_base - current_best[1]
		current_ineqs = current_ineqs - {current_best[0]}

		result.append(current_best)

	return result

# 'An inequality (1, 0, 0, 0, 0, 1, -1, 0, 0 , 0) x + 3 >= 0' ---> (1, 0, 0, 0, 0, 1, -1, 0, 0, 0, 3)

def record2Ineq(s):
	temp = s.strip()
	temp = temp.replace('An inequality', ' ')
	temp = temp.replace(',' , ' ')
	temp = temp.replace('>= 0', ' ')
	temp = temp.replace('+' , ' ')
	temp = temp.replace(') x', ' ')
	temp = temp.replace('(', ' ')
	temp = temp.split()

	for j in range(0, len(temp)):
		temp[j] = int(temp[j])
	#print(temp)
	if tuple(temp) == ():print(s)

	return tuple(temp)



def convexHullParser(input_file):
	f = open(input_file,'r')

	result = list()
	for line in f:
	    result.append(record2Ineq(line))
	f.close()

	return set(result)

if __name__ == "__main__":
	Ineqs = convexHullParser('xoo_dom_equ.txt')
	#for x in Ineqs: print(x)
	#Points = interestedSpace() - {(0,0,0,0,0,0,0,0,0,0)}
	Points = build_Impossible()
	#for x in Points: print(x)
	Select = greedy_Seclection(Ineqs, 10, Points)

	for t in Select:print(t[0], end = ',\\\n')
