from django.shortcuts import render
from django.http import HttpResponse, HttpResponseRedirect
from django.template import loader
from django.urls import reverse
from pymongo import MongoClient
#from pymongo import pymongo
from pandas import *
import json
from django.shortcuts import *
#from linki.forms import *
from django.http import JsonResponse
from bson.objectid import ObjectId
from datetime import date
from datetime import datetime
import bcrypt

from .views import gClient, gDb, gFlagLogin, gUserName, gPermitLevel, gDefaultPswrd, gCurDataset, gArchDataset

