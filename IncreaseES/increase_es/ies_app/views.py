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

# below for django rest framework
import requests
from django.shortcuts import HttpResponse

gClient = MongoClient("mongodb://mainUser:********@cluster0-shard-00-00.4zljq.mongodb.net:27017,cluster0-shard-00-01.4zljq.mongodb.net:27017,cluster0-shard-00-02.4zljq.mongodb.net:27017/?ssl=true&replicaSet=atlas-kshyjm-shard-0&authSource=admin&retryWrites=true&w=majority")
gDb = gClient.increase_esales
gFlagLogin = 0 # global flag login
gUserName = 'error' # global user name
gPassword = 'XxX12345' # global password
gPermitLevel = 'error' # global role
gDefaultPswrd = '1234567' # global default password
gCurDataset = 1 # 1 - live, 0 – archive/deleted, -1/-2/-3 – reserv.
gArchDataset = 0 # 1 - live, 0 – archive/deleted, -1/-2/-3 – reserv.

# instead of request.is_ajax(), because it is depricated 3.1 -> 4.1.2
def is_ajax(request):
    return request.META.get('HTTP_X_REQUESTED_WITH') == 'XMLHttpRequest'

# Home page
def index(request): #, pk

    template = loader.get_template('index.html')
    return HttpResponse(template.render({}, request))#,

# ***********************

# user login - check user name and password
def index_login(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset
    global gPassword
    if is_ajax(request=request):#request.is_ajax()
        table = gDb.users
        df = table.find()
        user_name = request.POST.get('user_name', '') # getting user_name from input
        pswrd = request.POST.get('pswrd', '')  # getting password from input
        gPassword = pswrd
        bytePwd = pswrd.encode('utf-8')
        
        # get data from users collection by user_name and dataset
        df = table.aggregate([
	    { "$match" : {"user_name":user_name, "dataset": gCurDataset}},
        {"$lookup": {
            "from": 'roles',
            "localField": 'roles_role',
            "foreignField": '_id',
            "as": 'a_value'},},
        {"$unwind":'$a_value'},
        {"$project":{
         'role':'$a_value.role',
         'roles_role': 1,
         '_id': 1, 
         'user_name': 1, 
		 'description': 1,
		 'dataset': 1,
         'pswrd': 1}}
        ])

        for cursor in df:    
            hash2 = cursor["pswrd"]
            if (bcrypt.checkpw(bytePwd, hash2)): # check user's password and hash from table
                gFlagLogin = 1
                gUserName = user_name
                gPermitLevel = cursor["role"] #
            else:
                gFlagLogin = 0
        response = {
            'gUserName' : gUserName,
            'gPermitLevel' : gPermitLevel,
            'gFlagLogin': gFlagLogin
        }
        return JsonResponse(response) # return response as JSON

#*****************************

#*****************************

# display collection seo_level
def seo_level(request): #,
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset

    if ((gFlagLogin == 1) and (gUserName != "error") and (gPermitLevel != "error")): # check permission
        table = gDb.seo_level
        df = table.find({"dataset": gCurDataset}) # fetch data from table seo_level
        template = loader.get_template('seo_level.html')
        context = {"drivers": df, "gPermitLevel": gPermitLevel,} # send data to template
        return HttpResponse(template.render(context, request))#,
    else: # check permission 
        template = loader.get_template('error.html')
        msg = "<h2>You are not logged in!</h2> <br><br>"
        msg += "<p CLASS='Thick'> Please <a href='http://localhost:8000' >login</a>!</p>"
        context = {"error_msg": msg,}
        return HttpResponse(template.render(context, request))

#*****************************

# save data to seo_level collection from temlate
def seo_level_save(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    if is_ajax(request=request):#
        id = request.POST.get('id', None) # getting data from input
        level_rule = request.POST.get('level_rule', '') 
        description = request.POST.get('description', '')  
        table = gDb.seo_level
        newvalues = { "$set": {"level_rule": level_rule, "description": description,
            "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg':'Your form has been submitted successfully:' + id  # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# add new record to collection
def seo_level_insert(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset
    if is_ajax(request=request):
        table = gDb.seo_level
        # insert new record
        table.insert_one({"create_date": datetime.now(), "dataset": gCurDataset, "last_user": gUserName, "last_changed": datetime.now()})
        dsMax = DataFrame(list(table.aggregate([{"$group": {
                "_id": 1,
                "maxId":{ "$max": "$_id"}
                }}])))
        for mov in range(len(dsMax)):
                idMax = str(dsMax.loc[mov, "maxId"])
        response = {
                'idMax': idMax # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# move record from active to archive dataset
def seo_level_delete(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gArchDataset
    if is_ajax(request=request):
        id = request.POST.get('id', None)
        table = gDb.seo_level
        # move record from active to archive dataset
        newvalues = { "$set": {"dataset": gArchDataset, "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg': 'Record deleted!' # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

#*****************************

# display collection seo_rules
def seo_rules(request): #
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset

    if ((gFlagLogin == 1) and (gUserName != "error") and (gPermitLevel != "error")): # check permission
        table = gDb.seo_rules
        #fetch data from seo_rules and seo_level
        df = table.aggregate([
	    { "$match" : {"dataset": gCurDataset}},
        {"$lookup": {
            "from": 'seo_level',
            "localField": 'seo_level_rule',
            "foreignField": '_id',
            "as": 'a_value'},},
        {"$unwind":'$a_value'},
        {"$project":{
         'level_rule':'$a_value.level_rule',
         'seo_level_rule': 1,
         '_id': 1, 
         'name_rule': 1, 
		 'rating_rule': 1,
		 'description': 1,
		 'dataset': 1,
         'create_date': 1}}
        ])

        #fetch data from seo_level
        table = gDb.seo_level
        df2 = DataFrame(list(table.find({"dataset": gCurDataset})))
        str_roles = "" # for select of roles
        for ds in range(len(df2)):
            str_roles = str_roles + "<option value='" + str(df2.loc[ds, "_id"]) + "'>" + df2.loc[ds, "level_rule"] + "</option>"
        template = loader.get_template('seo_rules.html')
        context = {"drivers": df, "seo_level_select": str_roles, "gPermitLevel": gPermitLevel,}
        return HttpResponse(template.render(context, request))#,
    else: # check permission - false
        template = loader.get_template('error.html')
        msg = "<h2>You are not logged in!</h2> <br><br>"
        msg += "<p CLASS='Thick'> Please <a href='http://localhost:8000' >login</a>!</p>"
        context = {"error_msg": msg,}
        return HttpResponse(template.render(context, request))

#*****************************

# save data to collection from template
def seo_rules_save(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    if is_ajax(request=request):
        id = request.POST.get('id', None) # getting data from input
        level_rule = request.POST.get('level_rule', '') 
        name_rule = request.POST.get('name_rule', '') 
        rating_rule = request.POST.get('rating_rule', '') 
        description = request.POST.get('description', '')  
        table = gDb.seo_rules
        # save data to collection
        newvalues = { "$set": {"seo_level_rule": ObjectId(level_rule), "name_rule": name_rule, "rating_rule": rating_rule, "description": description,
            "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg':'Your form has been submitted successfully:' + id  # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# add new record to collection
def seo_rules_insert(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset
    if is_ajax(request=request):
        table = gDb.seo_rules
        # insert new record
        table.insert_one({"create_date": datetime.now(), "dataset": gCurDataset, "last_user": gUserName, "last_changed": datetime.now()})
        dsMax = DataFrame(list(table.aggregate([{"$group": {
                "_id": 1,
                "maxId":{ "$max": "$_id"}
                }}])))
        for mov in range(len(dsMax)):
                idMax = str(dsMax.loc[mov, "maxId"])
        response = {
                'idMax': idMax # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# move record from active to archive dataset
def seo_rules_delete(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gArchDataset
    if is_ajax(request=request):
        id = request.POST.get('id', None)
        table = gDb.seo_rules
        # move record from active to archive dataset
        newvalues = { "$set": {"dataset": gArchDataset, "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg': 'Record deleted!' # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************


#*****************************

# display collection sites
def sites(request): #,
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset

    if ((gFlagLogin == 1) and (gUserName != "error") and (gPermitLevel != "error")): # check permission
        table = gDb.sites
        # fetch data from 
        df = table.find({"dataset": gCurDataset})
        template = loader.get_template('sites.html')
        context = {"drivers": df, "gPermitLevel": gPermitLevel, }
        return HttpResponse(template.render(context, request))#,
    else: # check permission - false
        template = loader.get_template('error.html')
        msg = "<h2>You are not logged in!</h2> <br><br>"
        msg += "<p CLASS='Thick'> Please <a href='http://localhost:8000' >login</a>!</p>"
        context = {"error_msg": msg,}
        return HttpResponse(template.render(context, request))


#*****************************

# save data to collection from template
def sites_save(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    if is_ajax(request=request):
        id = request.POST.get('id', None) # getting data from input
        name_site = request.POST.get('name_site', '') 
        main_page = request.POST.get('main_page', '') 
        description = request.POST.get('description', '')  
        table = gDb.sites
        # save data to collection
        newvalues = { "$set": {"name_site": name_site, "main_page": main_page, "description": description,
            "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg':'Your form has been submitted successfully:' + id  # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# add new record to collection
def sites_insert(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset
    if is_ajax(request=request):
        table = gDb.sites
        # insert new record
        table.insert_one({"create_date": datetime.now(), "dataset": gCurDataset, "last_user": gUserName, "last_changed": datetime.now()})
        dsMax = DataFrame(list(table.aggregate([{"$group": {
                "_id": 1,
                "maxId":{ "$max": "$_id"}
                }}])))
        for mov in range(len(dsMax)):
                idMax = str(dsMax.loc[mov, "maxId"])
        response = {
                'idMax': idMax # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# move record from active to archive dataset
def sites_delete(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gArchDataset
    if is_ajax(request=request):
        id = request.POST.get('id', None)
        table = gDb.sites
        newvalues = { "$set": {"dataset": gArchDataset, "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg': 'Record deleted!' # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

#*****************************

# display collection site_pages
def site_pages(request, name_site): #,
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset

    if ((gFlagLogin == 1) and (gUserName != "error") and (gPermitLevel != "error")): # check permission
        table = gDb.site_pages
        df = table.find({'name_site': name_site, "dataset": gCurDataset}).sort("_id", 1)
        template = loader.get_template('site_pages.html')
        context = {"drivers": df, "name_site": name_site, "gPermitLevel": gPermitLevel,}
        return HttpResponse(template.render(context, request))#,
    else:# check permission
        template = loader.get_template('error.html')
        msg = "<h2>You are not logged in!</h2> <br><br>"
        msg += "<p CLASS='Thick'> Please <a href='http://localhost:8000' >login</a>!</p>"
        context = {"error_msg": msg,}
        return HttpResponse(template.render(context, request))


#*****************************

# save data to collection from template
def site_pages_save(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    if is_ajax(request=request):
        id = request.POST.get('id', None) # getting data from input
        name_site = request.POST.get('name_site', '') 
        level_page = request.POST.get('level_page', '') 
        key_phrase = request.POST.get('key_phrase', '') 
        quantity_request = request.POST.get('quantity_request', '') 

        competition_level = request.POST.get('competition_level', '') 
        errors = request.POST.get('errors', '') 
        duplication = request.POST.get('duplication', '') 
        quantity_words = request.POST.get('quantity_words', '') 
        keyword_percentage = request.POST.get('keyword_percentage', '') 

        number_keywords = request.POST.get('number_keywords', '') 
        percentage_keywords = request.POST.get('percentage_keywords', '') 
        url = request.POST.get('url', '') 
        price = request.POST.get('price', '')  

        table = gDb.site_pages
        newvalues = { "$set": {"name_site": name_site, "level_page": level_page, "key_phrase": key_phrase, "quantity_request": quantity_request,
        "competition_level": competition_level, "errors": errors, "duplication": duplication, "quantity_words": quantity_words, "keyword_percentage": keyword_percentage,
        "number_keywords": number_keywords, "percentage_keywords": percentage_keywords, "url": url, "price":price,
        "last_user": gUserName, "last_changed": datetime.now() }}
        
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg':'Your form has been submitted successfully:' + id  # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# add new record to collection site_pages
def site_pages_insert(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset
    if is_ajax(request=request):
        name_site = request.POST.get('name_site', '')
        table = gDb.site_pages
        table.insert_one({"name_site":name_site,
                "create_date": datetime.now(), "dataset": gCurDataset, "last_user": gUserName, "last_changed": datetime.now()})
        dsMax = DataFrame(list(table.aggregate([{"$group": {
                "_id": 1,
                "maxId":{ "$max": "$_id"}
                }}])))
        for mov in range(len(dsMax)):
                idMax = str(dsMax.loc[mov, "maxId"])
        response = {
                'idMax': idMax # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# move record from active to archive dataset
def site_pages_delete(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gArchDataset
    if is_ajax(request=request):
        id = request.POST.get('id', None)
        table = gDb.site_pages
        newvalues = { "$set": {"dataset": gArchDataset, "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg': 'Record deleted!' # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

#*****************************

# display collection search_engine_system
def search_engine_system(request): #,
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset

    if ((gFlagLogin == 1) and (gUserName != "error") and (gPermitLevel != "error")): # check permission
        table = gDb.search_engine_system
        # fetch data from 
        df = table.find({"dataset": gCurDataset})
        template = loader.get_template('search_engine_system.html')
        context = {"drivers": df, "gPermitLevel": gPermitLevel, }
        return HttpResponse(template.render(context, request))#,
    else: # check permission - false
        template = loader.get_template('error.html')
        msg = "<h2>You are not logged in!</h2> <br><br>"
        msg += "<p CLASS='Thick'> Please <a href='http://localhost:8000' >login</a>!</p>"
        context = {"error_msg": msg,}
        return HttpResponse(template.render(context, request))


#*****************************

# save data to collection from template
def search_engine_system_save(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    if is_ajax(request=request):
        id = request.POST.get('id', None) # getting data from input
        search_system = request.POST.get('search_system', '') 
        description = request.POST.get('description', '')  
        table = gDb.search_engine_system
        # save data to collection
        newvalues = { "$set": {"search_system": search_system, "description": description,
            "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg':'Your form has been submitted successfully:' + id  # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# add new record to collection
def search_engine_system_insert(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset
    if is_ajax(request=request):
        table = gDb.search_engine_system
        # insert new record
        table.insert_one({"create_date": datetime.now(), "dataset": gCurDataset, "last_user": gUserName, "last_changed": datetime.now()})
        dsMax = DataFrame(list(table.aggregate([{"$group": {
                "_id": 1,
                "maxId":{ "$max": "$_id"}
                }}])))
        for mov in range(len(dsMax)):
                idMax = str(dsMax.loc[mov, "maxId"])
        response = {
                'idMax': idMax # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# move record from active to archive dataset
def search_engine_system_delete(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gArchDataset
    if is_ajax(request=request):
        id = request.POST.get('id', None)
        table = gDb.search_engine_system
        newvalues = { "$set": {"dataset": gArchDataset, "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg': 'Record deleted!' # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************
#*****************************

# choice search_system and name sites for seo_results data
def sites_seo_results(request): #,
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset

    if ((gFlagLogin == 1) and (gUserName != "error") and (gPermitLevel != "error")):
        table = gDb.sites
        df = table.find({"dataset": gCurDataset})
        # list of search_system
        search_system_list = ["google.kz","yandex.kz","google.kz(mob.)","yandex.kz(mob.)"]
        template = loader.get_template('sites_seo_results.html')
        context = {"drivers": df, "search_system_list": search_system_list }
        return HttpResponse(template.render(context, request))#,
    else:
        template = loader.get_template('error.html')
        msg = "<h2>You are not logged in!</h2> <br><br>"
        msg += "<p CLASS='Thick'> Please <a href='http://localhost:8000' >login</a>!</p>"
        context = {"error_msg": msg,}
        return HttpResponse(template.render(context, request))


#*****************************

#*****************************

# display collection key_phrases
def key_phrases(request): #,
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset

    if ((gFlagLogin == 1) and (gUserName != "error") and (gPermitLevel != "error")): # check permission
        table = gDb.key_phrases
        df = DataFrame(list(table.find({"dataset": gCurDataset})))
        # drop unused column
        df.drop('description', inplace=True, axis=1)
        df.drop('last_user', inplace=True, axis=1)
        df.drop('last_changed', inplace=True, axis=1)
        df.drop('create_date', inplace=True, axis=1)
        df_transpose = df.transpose() # transpose table by x/y axes

        no_col = ["id","search_system","name_site","1","2","3","4","5",
                                                "6","7","8","9","10",
                                                "11","12","13","14","15",
                                                "16","17","18","19","20"]
        
        table = gDb.sites
        df = DataFrame(list(table.find({"dataset": gCurDataset})))
        str_sites = ""
        for ds in range(len(df)):
            str_sites = str_sites + "<option value='" + str(df.loc[ds, "name_site"]) + "'>" + str(df.loc[ds, "name_site"]) + "</option>"
        
        # list of search_system
        search_system_list = "" #
        table = gDb.search_engine_system
        df = DataFrame(list(table.find({"dataset": gCurDataset})))
        for ds in range(len(df)):
            search_system_list = search_system_list + "<option value='" + str(df.loc[ds, "search_system"]) + "'>" + str(df.loc[ds, "search_system"]) + "</option>"

        template = loader.get_template('key_phrases.html')
        context = {"drivers": df_transpose, "drivers_no": no_col, "sites_list": str_sites, 
            "search_system_list": search_system_list, "gPermitLevel": gPermitLevel}
        return HttpResponse(template.render(context, request))#,
    else:# check permission
        template = loader.get_template('error.html')
        msg = "<h2>You are not logged in!</h2> <br><br>"
        msg += "<p CLASS='Thick'> Please <a href='http://localhost:8000' >login</a>!</p>"
        context = {"error_msg": msg,}
        return HttpResponse(template.render(context, request))


#*****************************
# save data to collection from template
def key_phrases_save(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    if is_ajax(request=request):
        id = request.POST.get('id', None) # getting data from input
        search_system = request.POST.get('search_system', '') 
        name_site = request.POST.get('name_site', '')
        pos1 = request.POST.get('pos1', '') 
        pos2 = request.POST.get('pos2', '') 
        pos3 = request.POST.get('pos3', '') 
        pos4 = request.POST.get('pos4', '') 
        pos5 = request.POST.get('pos5', '')   
        pos6 = request.POST.get('pos6', '') 
        pos7 = request.POST.get('pos7', '') 
        pos8 = request.POST.get('pos8', '') 
        pos9 = request.POST.get('pos9', '') 
        pos10 = request.POST.get('pos10', '')
        pos11 = request.POST.get('pos11', '')
        pos12 = request.POST.get('pos12', '')
        pos13 = request.POST.get('pos13', '')
        pos14 = request.POST.get('pos14', '')
        pos15 = request.POST.get('pos15', '')
        pos16 = request.POST.get('pos16', '')
        pos17 = request.POST.get('pos17', '')
        pos18 = request.POST.get('pos18', '')
        pos19 = request.POST.get('pos19', '')
        pos20 = request.POST.get('pos20', '')
        table = gDb.key_phrases
        newvalues = { "$set": {"name_site": name_site, "search_system": search_system, 
            "pos1": pos1, "pos2": pos2, "pos3": pos3, "pos4": pos4, "pos5": pos5,
            "pos6": pos6, "pos7": pos7, "pos8": pos8, "pos9": pos9, "pos10": pos10,
            "pos11": pos11, "pos12": pos12, "pos13": pos13, "pos14": pos14, "pos15": pos15,
            "pos16": pos16, "pos17": pos17, "pos18": pos18, "pos19": pos19, "pos20": pos20,
            "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg':'Your form has been submitted successfully:' + id  # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************
# add new record to collection
def key_phrases_insert(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset
    if is_ajax(request=request):
        table = gDb.key_phrases
        table.insert_one({"search_system": "","name_site": "",
            "pos1": "", "pos2": "", "pos3": "", "pos4": "", "pos5": "",
            "pos6": "", "pos7": "", "pos8": "", "pos9": "", "pos10": "",
            "pos11": "", "pos12": "", "pos13": "", "pos14": "", "pos15": "",
            "pos16": "", "pos17": "", "pos18": "", "pos19": "", "pos20": "",
            "create_date": datetime.now(), "dataset": gCurDataset, "last_user": gUserName, "last_changed": datetime.now()})
        dsMax = DataFrame(list(table.aggregate([{"$group": {
                "_id": 1,
                "maxId":{ "$max": "$_id"}
                }}])))
        for mov in range(len(dsMax)):
                idMax = str(dsMax.loc[mov, "maxId"])
        response = {
                'idMax': idMax # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# move record from active to archive dataset
def key_phrases_delete(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gArchDataset
    if is_ajax(request=request):
        id = request.POST.get('id', None)
        table = gDb.key_phrases
        newvalues = { "$set": {"dataset": gArchDataset, "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg': 'Record deleted!' # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

#*****************************

# display collection seo_results
def seo_results(request, name_site, search_system): #,
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset

    if ((gFlagLogin == 1) and (gUserName != "error") and (gPermitLevel != "error")): # check permission
        table = gDb.seo_results
        df = DataFrame(list(table.find({"dataset": gCurDataset, "search_system":search_system, "name_site":name_site}).sort("check_date", 1)))
        # drop unused column
        df.drop('search_system', inplace=True, axis=1)
        df.drop('name_site', inplace=True, axis=1)
        #df.drop('type_data', inplace=True, axis=1)
        df.drop('description', inplace=True, axis=1)
        #df.drop('dataset', inplace=True, axis=1)
        df.drop('last_user', inplace=True, axis=1)
        df.drop('last_changed', inplace=True, axis=1)
        df.drop('create_date', inplace=True, axis=1)
        df['check_date1'] = df['check_date'].dt.strftime('%Y-%m-%d')
        df['check_date'] = df['check_date1']
        df.drop('check_date1', inplace=True, axis=1)
        df_transpose = df.transpose() # transpose table by x/y axes

        table = gDb.key_phrases
        # drop unused column
        df2 = DataFrame(list(table.find({"dataset": gCurDataset, "search_system":search_system, "name_site":name_site})))
        df2.drop('_id', inplace=True, axis=1)
        df2.drop('search_system', inplace=True, axis=1)
        df2.drop('name_site', inplace=True, axis=1)
        #df2.drop('type_data', inplace=True, axis=1)
        df2.drop('description', inplace=True, axis=1)
        df2.drop('create_date', inplace=True, axis=1)
        df2.drop('dataset', inplace=True, axis=1)
        df2.drop('last_user', inplace=True, axis=1)
        df2.drop('last_changed', inplace=True, axis=1)
        
        
        df_transpose2 = df2.transpose() # transpose table by x/y axes
        
        template = loader.get_template('seo_results.html')
        context = {"drivers": df_transpose, "drivers_th":df_transpose2, "gPermitLevel": gPermitLevel,}
        return HttpResponse(template.render(context, request))#,
    else:# check permission
        template = loader.get_template('error.html')
        msg = "<h2>You are not logged in!</h2> <br><br>"
        msg += "<p CLASS='Thick'> Please <a href='http://localhost:8000' >login</a>!</p>"
        context = {"error_msg": msg,}
        return HttpResponse(template.render(context, request))


#*****************************
# save data to collection from template
def seo_results_save(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    if is_ajax(request=request):
        id = request.POST.get('id', None) # getting data from input
        check_date = request.POST.get('check_date', '') 
        pos1 = request.POST.get('pos1', '') 
        pos2 = request.POST.get('pos2', '') 
        pos3 = request.POST.get('pos3', '') 
        pos4 = request.POST.get('pos4', '') 
        pos5 = request.POST.get('pos5', '')   
        pos6 = request.POST.get('pos6', '') 
        pos7 = request.POST.get('pos7', '') 
        pos8 = request.POST.get('pos8', '') 
        pos9 = request.POST.get('pos9', '') 
        pos10 = request.POST.get('pos10', '')
        pos11 = request.POST.get('pos11', '')
        pos12 = request.POST.get('pos12', '')
        pos13 = request.POST.get('pos13', '')
        pos14 = request.POST.get('pos14', '')
        pos15 = request.POST.get('pos15', '')
        pos16 = request.POST.get('pos16', '')
        pos17 = request.POST.get('pos17', '')
        pos18 = request.POST.get('pos18', '')
        pos19 = request.POST.get('pos19', '')
        pos20 = request.POST.get('pos20', '')
        dt = datetime.strptime(check_date+"T00:00:00.000Z", "%Y-%m-%dT%H:%M:%S.000Z");
        table = gDb.seo_results
        newvalues = { "$set": {"check_date": dt, 
            "pos1": pos1, "pos2": pos2, "pos3": pos3, "pos4": pos4, "pos5": pos5,
            "pos6": pos6, "pos7": pos7, "pos8": pos8, "pos9": pos9, "pos10": pos10,
            "pos11": pos11, "pos12": pos12, "pos13": pos13, "pos14": pos14, "pos15": pos15,
            "pos16": pos16, "pos17": pos17, "pos18": pos18, "pos19": pos19, "pos20": pos20,
            "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg':'Your form has been submitted successfully:' + id  # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************
# add new record to collection
def seo_results_insert(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset
    if is_ajax(request=request):
        table = gDb.seo_results
        table.insert_one({"search_system": "google.kz","name_site": "phonealmaty.kz","check_date": datetime.now(),
            "pos1": 0, "pos2": 0, "pos3": 0, "pos4": 0, "pos5": 0,
            "pos6": 0, "pos7": 0, "pos8": 0, "pos9": 0, "pos10": 0,
            "pos11": 0, "pos12": 0, "pos13": 0, "pos14": 0, "pos15": 0,
            "pos16": 0, "pos17": 0, "pos18": 0, "pos19": 0, "pos20": 0,
            "create_date": datetime.now(), "dataset": gCurDataset, "last_user": gUserName, "last_changed": datetime.now()})
        dsMax = DataFrame(list(table.aggregate([{"$group": {
                "_id": 1,
                "maxId":{ "$max": "$_id"}
                }}])))
        for mov in range(len(dsMax)):
                idMax = str(dsMax.loc[mov, "maxId"])
        response = {
                'idMax': idMax # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# move record from active to archive dataset
def seo_results_delete(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gArchDataset
    if is_ajax(request=request):
        id = request.POST.get('id', None)
        table = gDb.seo_results
        newvalues = { "$set": {"dataset": gArchDataset, "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg': 'Record deleted!' # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************


#*****************************

# display collection
def marketplace_rules(request): #,
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset

    if ((gFlagLogin == 1) and (gUserName != "error") and (gPermitLevel != "error")): # check permission
        table = gDb.marketplace_rules
        df = table.find({"dataset": gCurDataset})
        template = loader.get_template('marketplace_rules.html')
        context = {"drivers": df,  "gPermitLevel": gPermitLevel,}
        return HttpResponse(template.render(context, request))#,
    else: # check permission - false
        template = loader.get_template('error.html')
        msg = "<h2>You are not logged in!</h2> <br><br>"
        msg += "<p CLASS='Thick'> Please <a href='http://localhost:8000' >login</a>!</p>"
        context = {"error_msg": msg,}
        return HttpResponse(template.render(context, request))

#*****************************

# save data to collection from template
def marketplace_rules_save(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    if is_ajax(request=request):
        id = request.POST.get('id', None) # getting data from input
        group_rule = request.POST.get('group_rule', '')
        name_rule = request.POST.get('name_rule', '') 
        rating_rule = request.POST.get('rating_rule', '') 
        description = request.POST.get('description', '')  
        table = gDb.marketplace_rules
        newvalues = { "$set": {"group_rule": group_rule, "name_rule": name_rule, "rating_rule": rating_rule, "description": description,
            "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg':'Your form has been submitted successfully:' + id  # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# add new record to collection
def marketplace_rules_insert(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset
    if is_ajax(request=request):
        table = gDb.marketplace_rules
        table.insert_one({"create_date": datetime.now(), "dataset": gCurDataset, "last_user": gUserName, "last_changed": datetime.now()})
        dsMax = DataFrame(list(table.aggregate([{"$group": {
                "_id": 1,
                "maxId":{ "$max": "$_id"}
                }}])))
        for mov in range(len(dsMax)):
                idMax = str(dsMax.loc[mov, "maxId"])
        response = {
                'idMax': idMax # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# move record from active to archive dataset
def marketplace_rules_delete(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gArchDataset
    if is_ajax(request=request):
        id = request.POST.get('id', None)
        table = gDb.marketplace_rules
        newvalues = { "$set": {"dataset": gArchDataset, "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg': 'Record deleted!' # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

#*****************************

# display collection
def marketplaces(request): #,
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset

    if ((gFlagLogin == 1) and (gUserName != "error") and (gPermitLevel != "error")): # check permission
        table = gDb.marketplaces
        df = table.find({"dataset": gCurDataset})
        template = loader.get_template('marketplaces.html')
        context = {"drivers": df, "gPermitLevel": gPermitLevel, }
        return HttpResponse(template.render(context, request))#,
    else: # check permission
        template = loader.get_template('error.html')
        msg = "<h2>You are not logged in!</h2> <br><br>"
        msg += "<p CLASS='Thick'> Please <a href='http://localhost:8000' >login</a>!</p>"
        context = {"error_msg": msg,}
        return HttpResponse(template.render(context, request))

#*****************************

# save data to collection from template
def marketplaces_save(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    if is_ajax(request=request):
        id = request.POST.get('id', None) # getting data from input
        name_marketplace = request.POST.get('name_marketplace', '') 
        main_page = request.POST.get('main_page', '') 
        description = request.POST.get('description', '')  
        table = gDb.marketplaces
        newvalues = { "$set": {"name_marketplace": name_marketplace, "main_page": main_page, "description": description,
            "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg':'Your form has been submitted successfully:' + id  # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# add new record to collection
def marketplaces_insert(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset
    if is_ajax(request=request):
        table = gDb.marketplaces
        table.insert_one({"create_date": datetime.now(), "dataset": gCurDataset, "last_user": gUserName, "last_changed": datetime.now()})
        dsMax = DataFrame(list(table.aggregate([{"$group": {
                "_id": 1,
                "maxId":{ "$max": "$_id"}
                }}])))
        for mov in range(len(dsMax)):
                idMax = str(dsMax.loc[mov, "maxId"])
        response = {
                'idMax': idMax # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# move record from active to archive dataset
def marketplaces_delete(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gArchDataset
    if is_ajax(request=request):
        id = request.POST.get('id', None)
        table = gDb.marketplaces
        newvalues = { "$set": {"dataset": gArchDataset, "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg': 'Record deleted!' # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

#*****************************

# display collection marketplace_sales
def marketplace_sales(request, name_marketplace): #,
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset

    if ((gFlagLogin == 1) and (gUserName != "error") and (gPermitLevel != "error")):
        table = gDb.marketplace_sales
        df = table.find({'name_marketplace': name_marketplace, "dataset": gCurDataset}).sort("_id", 1)
        template = loader.get_template('marketplace_sales.html')
        context = {"drivers": df, "name_marketplace": name_marketplace, "gPermitLevel": gPermitLevel, }
        return HttpResponse(template.render(context, request))#,
    else:
        template = loader.get_template('error.html')
        msg = "<h2>You are not logged in!</h2> <br><br>"
        msg += "<p CLASS='Thick'> Please <a href='http://localhost:8000' >login</a>!</p>"
        context = {"error_msg": msg,}
        return HttpResponse(template.render(context, request))


#*****************************

# save data to collection from template
def marketplace_sales_save(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    if is_ajax(request=request):
        id = request.POST.get('id', None) # getting data from input
        name_marketplace = request.POST.get('name_marketplace', '') 
        key_phrase = request.POST.get('key_phrase', '') 
        competition_level = request.POST.get('competition_level', '')
        guarantee = request.POST.get('guarantee', '')
        price = request.POST.get('price', '')
        description = request.POST.get('description', '')  
        table = gDb.marketplace_sales
        newvalues = { "$set": {"name_marketplace": name_marketplace, "key_phrase": key_phrase, "competition_level": competition_level, 
            "guarantee": guarantee, "price": price, "description": description, "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg':'Your form has been submitted successfully:' + id  # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# add new record to collection
def marketplace_sales_insert(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset
    if is_ajax(request=request):
        name_marketplace = request.POST.get('name_marketplace', '')
        table = gDb.marketplace_sales
        table.insert_one({"name_marketplace": name_marketplace,
                "create_date": datetime.now(), "dataset": gCurDataset, "last_user": gUserName, "last_changed": datetime.now()})
        dsMax = DataFrame(list(table.aggregate([{"$group": {
                "_id": 1,
                "maxId":{ "$max": "$_id"}
                }}])))
        for mov in range(len(dsMax)):
                idMax = str(dsMax.loc[mov, "maxId"])
        response = {
                'idMax': idMax # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# move record from active to archive dataset
def marketplace_sales_delete(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gArchDataset
    if is_ajax(request=request):
        id = request.POST.get('id', None)
        table = gDb.marketplace_sales
        newvalues = { "$set": {"dataset": gArchDataset, "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg': 'Record deleted!' # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

#*****************************

# display collection users
def users(request): #,
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset

    if ((gFlagLogin == 1) and (gPermitLevel in ["admin","superUser"])):
        table = gDb.users
        df = table.aggregate([
	    { "$match" : {"dataset": gCurDataset}},
        {"$lookup": {
            "from": 'roles',
            "localField": 'roles_role',
            "foreignField": '_id',
            "as": 'a_value'},},
        {"$unwind":'$a_value'},
        {"$project":{
         'role':'$a_value.role',
         'roles_role': 1,
         '_id': 1, 
         'user_name': 1, 
		 'description': 1,
		 'dataset': 1,
         'create_date': 1}}
        ])
        table = gDb.roles
        df2 = DataFrame(list(table.find({"dataset": gCurDataset})))
        str_roles = ""
        for ds in range(len(df2)):
            str_roles = str_roles + "<option value='" + str(df2.loc[ds, "_id"]) + "'>" + df2.loc[ds, "role"] + "</option>"
        template = loader.get_template('users.html')
        context = {"drivers": df, "roles_select": str_roles, "gPermitLevel":gPermitLevel, }
        return HttpResponse(template.render(context, request))#,
    else:
        template = loader.get_template('error.html')
        msg = "<h2>You don't have permission!</h2> <br><br>"
        msg += "<p CLASS='Thick'> Need permission for - superUser. Please <a href='http://localhost:8000' >login</a>!</p>"
        context = {"error_msg": msg,}
        return HttpResponse(template.render(context, request))


#*****************************

# save data to collection from template
def users_save(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    if is_ajax(request=request):
        id = request.POST.get('id', None) # getting data from input
        user_name = request.POST.get('user_name', '') 
        role = request.POST.get('role', '') 
        description = request.POST.get('description', '')  
        table = gDb.users
        newvalues = { "$set": {"user_name": user_name, "roles_role": ObjectId(role), "description": description,
            "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg':'Your form has been submitted successfully:' + id  # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# add new record to collection with gDefaultPswrd
def users_insert(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gDefaultPswrd
    global gCurDataset
    if is_ajax(request=request):
        bytePwd = gDefaultPswrd.encode('utf-8')
        mySalt = bcrypt.gensalt()
        hash = bcrypt.hashpw(bytePwd, mySalt)
        role = request.POST.get('role', '') 
        table = gDb.users
        table.insert_one({"role": role, "pswrd": hash,
            "create_date": datetime.now(), "dataset": gCurDataset, "last_user": gUserName, "last_changed": datetime.now()})
        dsMax = DataFrame(list(table.aggregate([{"$group": {
                "_id": 1,
                "maxId":{ "$max": "$_id"}
                }}])))
        for mov in range(len(dsMax)):
                idMax = str(dsMax.loc[mov, "maxId"])
        response = {
                'idMax': idMax # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

# move record from active to archive dataset
def users_delete(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gArchDataset
    if is_ajax(request=request):
        id = request.POST.get('id', None)
        table = gDb.users
        newvalues = { "$set": {"dataset": gArchDataset, "last_user": gUserName, "last_changed": datetime.now()}}
        table.update_one({'_id': ObjectId(id)}, newvalues)
        response = {
                'msg': 'Record deleted!' # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************
#*****************************

# window change password by user
def change_password(request): #,
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset

    if ((gFlagLogin == 1) and (gUserName != "error") and (gPermitLevel != "error")): # check permission
        
        template = loader.get_template('change_password.html')
        context = {"gUserName": gUserName, "gPermitLevel": gPermitLevel,}
        return HttpResponse(template.render(context, request))#,
    else: # check permission
        template = loader.get_template('error.html')
        msg = "<h2>You don't have permission!</h2> <br><br>"
        msg += "<p CLASS='Thick'> Need Log In. Please <a href='http://localhost:8000' >login</a>!</p>"
        context = {"error_msg": msg,}
        return HttpResponse(template.render(context, request))

#*****************************

# change password by user
def change_password_update(request):
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gDefaultPswrd
    global gCurDataset
    if is_ajax(request=request): #request.is_ajax()
        msg = ""
        table = gDb.users
        df = table.find()
        #user_name = request.POST.get('user_name', '') # getting data from input
        pswrd = request.POST.get('pswrd', '')  # getting data from input
        pswrd_new = request.POST.get('pswrd_new', '')  # getting data from input
        bytePwd = pswrd.encode('utf-8')
        for cursor in table.find({"user_name":gUserName, "dataset": gCurDataset}):
            hash = cursor["pswrd"]
            if (bcrypt.checkpw(bytePwd, hash)):
                bytePwd2 = pswrd_new.encode('utf-8')
                mySalt2 = bcrypt.gensalt()
                hash2 = bcrypt.hashpw(bytePwd2, mySalt2)
                table2 = gDb.users
                newvalues2 = { "$set": {"pswrd": hash2,
                    "last_user": gUserName, "last_changed": datetime.now()}}
                table2.update_one({'_id': ObjectId(cursor["_id"])}, newvalues2)
                msg = "ok"
            else:
                msg = "error"

        
        
        response = {
                'msg': msg # response message
            }
        return JsonResponse(response) # return response as JSON

#*****************************

#*****************************

# report best_seo_rules
def report_best_seo_rules(request): #,
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset

    if ((gFlagLogin == 1) and (gUserName != "error") and (gPermitLevel != "error")): # check permission
        table = gDb.seo_rules
        #df = table.find({"dataset": gCurDataset}).sort("rating_rule", -1)
        #fetch data from seo_rules and seo_level
        df = table.aggregate([
	    { "$match" : {"dataset": gCurDataset}},
		{ "$sort" : { "rating_rule" : -1 } },
        {"$lookup": {
            "from": 'seo_level',
            "localField": 'seo_level_rule',
            "foreignField": '_id',
            "as": 'a_value'},},
        {"$unwind":'$a_value'},
        {"$project":{
         'level_rule':'$a_value.level_rule',
         'seo_level_rule': 1,
         '_id': 1, 
         'name_rule': 1, 
		 'rating_rule': 1,
		 'description': 1,
		 'dataset': 1,
         'create_date': 1}}
        ])

        table = gDb.seo_rules
        df10 = table.find({"dataset": gCurDataset}).sort("rating_rule", -1).limit(10)

        template = loader.get_template('report_best_seo_rules.html')
        context = {"drivers": df, "drivers10": df10,}
        return HttpResponse(template.render(context, request))#,
    else: # check permission
        template = loader.get_template('error.html')
        msg = "<h2>You are not logged in!</h2> <br><br>"
        msg += "<p CLASS='Thick'> Please <a href='http://localhost:8000' >login</a>!</p>"
        context = {"error_msg": msg,}
        return HttpResponse(template.render(context, request))

#*****************************

# report best_marketplace_rules
def report_best_marketplace_rules(request): #,
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset

    if ((gFlagLogin == 1) and (gUserName != "error") and (gPermitLevel != "error")): # check permission
        table = gDb.marketplace_rules
        df = table.find({"dataset": gCurDataset}).sort("rating_rule", -1)
        table = gDb.marketplace_rules
        df10 = table.find({"dataset": gCurDataset}).sort("rating_rule", -1).limit(10)

        template = loader.get_template('report_best_marketplace_rules.html')
        context = {"drivers": df, "drivers10": df10, }
        return HttpResponse(template.render(context, request))#,
    else: # check permission
        template = loader.get_template('error.html')
        msg = "<h2>You are not logged in!</h2> <br><br>"
        msg += "<p CLASS='Thick'> Please <a href='http://localhost:8000' >login</a>!</p>"
        context = {"error_msg": msg,}
        return HttpResponse(template.render(context, request))

#*****************************

# replication data from ContactsBDW to IncreaseES DWH (Data WareHouse)
def replication_dwh(request): #,
    global gDb
    global gFlagLogin
    global gUserName
    global gPermitLevel
    global gCurDataset
    global gPassword
    if ((gFlagLogin == 1) and (gUserName != "error") and (gPermitLevel != "error")): # check permission
        
        body_data = {"user_id":gUserName, "pswd":gPassword}
        result = requests.get('http://localhost:8001/api/types', data=body_data)
        body_data2 = {"table_name":"types", "user_id":gUserName, "pswd":gPassword,
                        "actions":"Download table types", "dataset":1, "last_user":gUserName, "last_changed": datetime.now()}
        result = requests.post('http://localhost:8001/api/log_dwh', data=body_data2)

        result = requests.get('http://localhost:8001/api/brands', data=body_data)
        body_data2 = {"table_name":"brands", "user_id":gUserName, "pswd":gPassword,
                        "actions":"Download table brands", "dataset":1, "last_user":gUserName, "last_changed": datetime.now()}
        result = requests.post('http://localhost:8001/api/log_dwh', data=body_data2)

        result = requests.get('http://localhost:8001/api/customers', data=body_data)
        body_data2 = {"table_name":"customers", "user_id":gUserName, "pswd":gPassword,
                        "actions":"Download table customers", "dataset":1, "last_user":gUserName, "last_changed": datetime.now()}
        result = requests.post('http://localhost:8001/api/log_dwh', data=body_data2)

        result = requests.get('http://localhost:8001/api/customers_deals', data=body_data)
        body_data2 = {"table_name":"customers_deals", "user_id":gUserName, "pswd":gPassword,
                        "actions":"Download table customers_deals", "dataset":1, "last_user":gUserName, "last_changed": datetime.now()}
        result = requests.post('http://localhost:8001/api/log_dwh', data=body_data2)

        result = requests.get('http://localhost:8001/api/products', data=body_data)
        body_data2 = {"table_name":"products", "user_id":gUserName, "pswd":gPassword,
                        "actions":"Download table products", "dataset":1, "last_user":gUserName, "last_changed": datetime.now()}
        result = requests.post('http://localhost:8001/api/log_dwh', data=body_data2)

        template = loader.get_template('replication_dwh.html')
        context = {"gUserName": gUserName, "gPermitLevel": gPermitLevel,}
        return HttpResponse(template.render(context, request))#,
    else: # check permission
        template = loader.get_template('error.html')
        msg = "<h2>You don't have permission!</h2> <br><br>"
        msg += "<p CLASS='Thick'> Need Log In. Please <a href='http://localhost:8000' >login</a>!</p>"
        context = {"error_msg": msg,}
        return HttpResponse(template.render(context, request))