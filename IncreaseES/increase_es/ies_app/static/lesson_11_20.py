print("********************************************** lesson 11")
# while loop =  a statement that will execute it's block of code,
#                        as long as it's condition remains true

name = ""

while (len(name) == 0):
    name = "dfgh" #input("Enter your name: ")

print("Hello "+name)

print("********************************************** lesson 12")
# for loop =    a statement that will execute it's block of code
#                      a limited amount of times
#
#                     while loop = unlimited
#                     for loop = limited

import time

#for i in range(10):
    #print(i+1)

for i in range(50,100+1,2):
    print(i)

#for i in "Bro Code":
    #print(i)

for seconds in range(10,0,-1):
    print(seconds)
    time.sleep(0.1)
print("Happy New Year!")

print("********************************************** lesson 13")
#python #nested #loops

# nested loops =    The "inner loop" will finish all of it's iterations before
#                   finishing one iteration of the "outer loop"

rows = 3 #int(input("How many rows?: "))
columns = 4 # int(input("How many columns?: "))
symbol = "e" #input("Enter a symbol to use: ")

for i in range(rows):
    for j in range(columns):
        print(symbol, end="-")
    print()

print("********************************************** lesson 14")
# Loop Control Statements = change a loops execution from its normal sequence

# break =       used to terminate the loop entirely
# continue =    skips to the next iteration of the loop.
# pass =        does nothing, acts as a placeholder

while True:
    name = "jhg" #input("Enter your name: ")
    if name != "":
        break

phone_number = "123-456-7890"
for i in phone_number:
    if i == "-":
        continue
    print(i, end="")

for i in range(1,21):
    if i == 13:
        pass
    else:
        print(i)

print("********************************************** lesson 15")
# list = used to store multiple items in a single variable
food = ["pizza","hamburger","hotdog","spaghetti","pudding"]
food[0] = "sushi"
#food.append("ice cream")
#food.remove("hotdog")
#food.pop()
food.insert(0,"cake")
#food.sort()
#food.clear()

for x in food:
    print(x)

print("********************************************** lesson 16")
# 2D lists = a list of lists

drinks = ["coffee","soda","tea"]
dinner = ["pizza","hamburger","hotdog"]
dessert = ["cake","ice cream"]

food = [drinks,dinner,dessert]

for x in food:
    for y in x:
        print(y)

print("********************************************** lesson 17")
# tuple =   collection which is ordered and unchangeable
#                used to group together related data

student = ("Bro",21,"male")

print(student.count("Bro"))
print(student.index("male"))

for x in student:
    print(x)

if "Bro" in student:
    print("Bro is here!")

print("********************************************** lesson 18")
# set = collection which is unordered, unindexed. No duplicate values

utensils = {"fork","spoon","knife"}
dishes = {"bowl","plate","cup","knife"}

utensils.add("napkin")
#utensils.remove("fork")
#utensils.clear()
#dishes.update(utensils)
#dinner_table = utensils.union(dishes)

#print(dishes.difference(utensils))
#print(utensils.intersection(dishes))

for x in utensils:
    print(x)

print("********************************************** lesson 19")
# dictionary =  A changeable, unordered collection of unique key:value pairs
#                        Fast because they use hashing, allow us to access a value quickly

capitals = {'USA':'Washington DC',
            'India':'New Dehli',
            'China':'Beijing',
            'Russia':'Moscow'}

capitals.update({'Germany':'Berlin'})
capitals.update({'USA':'Las Vegas'})
# capitals.pop('China')
# capitals.clear()

# print(capitals['Germany'])
print(capitals.get('Germany'))
# print(capitals.keys())
# print(capitals.values())
# print(capitals.items())

for key,value in capitals.items():
    print(key, value)

print("********************************************** lesson 20")
# index operator [] = gives access to a sequence’s element (str,list,tuples)

name = "bro Code!"

#if(name[0].islower()):
   #name = name.capitalize()
  
first_name = name[:3].upper()
last_name = name[4:].lower()
last_character = name[-1]

print(first_name)
print(last_name)
print(last_character)