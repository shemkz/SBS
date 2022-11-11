print("********************************************** lesson 21")

#function = a block of code which is executed only when it is called.

def hello(first_name,last_name,age):
    print("hello "+first_name+" "+last_name)
    print("You are "+str(age)+" years old")
    print("Have a nice day!")

hello("Bro","Code",21)

print("********************************************** lesson 22")
# return statement = Functions send Python values/objects back to the caller.
#                    These values/objects are known as the function’s return value

def multiply(number1,number2):
    return number1 * number2

x = multiply(6,8)

print(x)

print("********************************************** lesson 23")
# keyword arguments =   arguments preceded by an identifier when we pass them to a function
#                       The order of the arguments doesn't matter, unlike positional arguments
#                       Python knows the names of the arguments that a function receives

def hello(first,middle,last):
    print("Hello "+first+" "+middle+" "+last)


hello(last="Code",middle="Dude",first="Bro")

print("********************************************** lesson 24")
# nested functions calls =  function calls inside other function calls
#                           innermost function calls are resolved first
#                           returned value is used as argument for the next outer function

# num = input("Enter a whole positive number: ")
# num = float(num)
# num = abs(num)
# num = round(num)
# print(num)

print(round(abs(float("47"))))

print("********************************************** lesson 25")
# scope = The region that a variable is recognized
#         A variable is only available from inside the region it is created.
#         A global and locally scoped version of a variable can be created

name = "Bro" # global scope (available inside & outside functions)

def display_name():
    name = "Code"    # local scope (available only inside this function)
    print(name)

display_name()
print(name)

print("********************************************** lesson 26")
# *args =   parameter that will pack all arguments into a tuple
#                useful so that a function can accept a varying amount of arguments

def add(*args):
    sum = 0
    for i in args:
        sum += i
    return sum


print(add(1,2,3,4,5,6,7,8))

print("********************************************** lesson 27")
# **kwargs = parameter that will pack all arguments into a dictionary
# useful so that a function can accept a varying amount of keyword arguments
def hello(**kwargs):
    #print("Hello " + kwargs['first'] + " " + kwargs['last'])
    print("Hello",end=" ")
    for key,value in kwargs.items():#
        print(value,end=" ")
hello(title="Mr.",first="Bro",middle="Dude",last="Code")

print("********************************************** lesson 28")
# str.format() =    optional method that gives users
#                   more control when displaying output

animal = "cow"
item = "moon"

print("The "+animal+" jumped over the "+item)

# {} = format field
print("The {} jumped over the {}".format("cow","moon"))
print("The {0} jumped over the {1}".format(animal,item)) # positional arguments
print("The {animal} jumped over the {item}".format(animal="cow",item="moon"))   # keyword arguments

text = "The {} jumped over the {}"
print(text.format("cow","moon"))

name = "Bro"

print("My name is {}".format(name))
print("My name is {:10}".format(name,name))   # amount of padding
print("My name is {:<10}".format(name,name))  # < = left align
print("My name is {:>10}".format(name,name))  # > = right align
print("My name is {:^10}".format(name,name))  # ^ = center align


# str.format() =    optional method that gives users
#                   more control when displaying output

number = 1000

print("The number pi is {:.3f}".format(number))
print("The number is {:,}".format(number))
print("The number is {:b}".format(number))
print("The number is {:o}".format(number))
print("The number is {:X}".format(number))
print("The number is {:E}".format(number))

print("********************************************** lesson 29")
import random

x = random.randint(1,6)
y = random.random()
myList = ['rock','paper','scissors']
z = random.choice(myList)
print(z)
cards = [1,2,3,4,5,6,7,8,9,"J","Q","K","A"]
random.shuffle(cards)
print(cards)

print("********************************************** lesson 30")
# exception =   events detected during execution that interrupt the flow of a program
try:
    numerator = int(input("Enter a number to divide: "))
    denominator = int(input("Enter a number to divide by: "))
    result = numerator / denominator
except ZeroDivisionError as e:
    print(e)
    print("You can't divide by zero! idiot!")
except ValueError as e:
    print(e)
    print("Enter only numbers plz")
except Exception as e:
    print(e)
    print("something went wrong :(")
else:
    print(result)
finally:
    print("This will always execute")