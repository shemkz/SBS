
from django.shortcuts import render

# Create your views here.

from django.shortcuts import render
from datetime import date
from datetime import datetime
import json

from django.http.response import JsonResponse
from rest_framework.parsers import JSONParser 
from rest_framework import status
from rest_framework import serializers
from rest_framework.response import Response
 
from .models import Types, Brands, Customers, Customers_deals, Products, Last_download, Log_dwh
from .serializers import Log_dwhSerializer
from rest_framework.decorators import api_view
from rest_framework.response import Response
import requests

from django.contrib.auth import authenticate
from django.views.decorators.csrf import csrf_exempt
from rest_framework.authtoken.models import Token
from rest_framework.decorators import api_view, permission_classes
from rest_framework.permissions import AllowAny
from rest_framework.status import (
    HTTP_400_BAD_REQUEST,
    HTTP_404_NOT_FOUND,
    HTTP_200_OK
)
#from rest_framework.response import Response

@api_view(['GET', 'POST', 'DELETE'])
def types_list(request):

    # Authentication
    username = request.data.get("user_id")
    password = request.data.get("pswd")
    if username is None or password is None:
        return Response({'error': 'Please provide both username and password'}, status=HTTP_400_BAD_REQUEST)
    user = authenticate(username=username, password=password)
    if not user:
        return Response({'error': 'Invalid Credentials'}, status=HTTP_404_NOT_FOUND)

    # GET
    if request.method == 'GET':
        ld_row = Last_download.objects.get(table_name = "Types") # find row with table_name = "Types"
        last_download = ld_row.last_download
        last_download_str = last_download.strftime("%Y-%m-%d %H:%M:%S")
        #if (last_download_str == "" ):
        #    last_download_str == "2020-10-19 10:57:33"
        body_data = '{"user_id":"' + username  + '", "pswd":"' + password + '"}'
        #post_data = '{"table": "Types", "user_id":"kanvladi", "pswd":"XxX12345"}'
        result = requests.get('http://localhost:8081/api/types/'+last_download_str, data=body_data)
        #result = requests.get('http://localhost:8081/types')

        aList = result.json() # json.loads(result)
        for i in range(len(aList)):
            t_id = int(aList[i]["id"])
            try:
                t_row = Types.objects.get(id_orig = t_id)
                t_row.type_name = type_name=aList[i]["type_name"]
                t_row.desc = aList[i]["description"]
                t_row.date_create = aList[i]["date_create"]
                t_row.dataset = aList[i]["dataset"]
                t_row.last_user = aList[i]["last_user"]
                t_row.last_changed = aList[i]["last_changed"]
                t_row.save() # save exists row into Types
            except Types.DoesNotExist:
                obj=Types.objects.create(id_orig=aList[i]["id"], type_name=aList[i]["type_name"], 
				desc=aList[i]["description"],
				date_create=aList[i]["date_create"], dataset = aList[i]["dataset"],
				last_user=aList[i]["last_user"], last_changed=aList[i]["last_changed"])
                obj.save() # insert new row into Types

        ld_row.last_download = datetime.now()
        ld_row.save() # save date and time last download into table Types

        return Response("Recived results")
        #return JsonResponse(types_serializer.data, safe=False)
        # 'safe=False' for objects serialization

@api_view(['GET', 'POST', 'DELETE'])		
def brands_list(request):
    
    # Authentication
    username = request.data.get("user_id")
    password = request.data.get("pswd")
    if username is None or password is None:
        return Response({'error': 'Please provide both username and password'}, status=HTTP_400_BAD_REQUEST)
    user = authenticate(username=username, password=password)
    if not user:
        return Response({'error': 'Invalid Credentials'}, status=HTTP_404_NOT_FOUND)

    # GET
    if request.method == 'GET':
        ld_row = Last_download.objects.get(table_name = "Brands") # find row with table_name = "Types"
        last_download = ld_row.last_download
        last_download_str = last_download.strftime("%Y-%m-%d %H:%M:%S")
        body_data = '{"user_id":"' + username  + '", "pswd":"' + password + '"}'
        result = requests.get('http://localhost:8081/api/brands/'+last_download_str, data=body_data)

        aList = result.json() # json.loads(result)
        for i in range(len(aList)):
            t_id = int(aList[i]["id"])
            try:
                t_row = Brands.objects.get(id_orig = t_id)
                t_row.brand_name = aList[i]["brand_name"]
                t_row.desc = aList[i]["description"]
                t_row.date_create = aList[i]["date_create"]
                t_row.dataset = aList[i]["dataset"]
                t_row.last_user = aList[i]["last_user"]
                t_row.last_changed = aList[i]["last_changed"]
                t_row.save() # save exists row into Brands
            except Brands.DoesNotExist:
                obj=Brands.objects.create(id_orig=aList[i]["id"], brand_name=aList[i]["brand_name"], 
				desc=aList[i]["description"],
				date_create=aList[i]["date_create"], dataset = aList[i]["dataset"],
				last_user=aList[i]["last_user"], last_changed=aList[i]["last_changed"])
                obj.save() # insert new row into Brands

        ld_row.last_download = datetime.now()
        ld_row.save() # save date and time last download into table Types

        return Response("Recived results")

@api_view(['GET', 'POST', 'DELETE'])		
def customers_list(request):
    
    # Authentication
    username = request.data.get("user_id")
    password = request.data.get("pswd")
    if username is None or password is None:
        return Response({'error': 'Please provide both username and password'}, status=HTTP_400_BAD_REQUEST)
    user = authenticate(username=username, password=password)
    if not user:
        return Response({'error': 'Invalid Credentials'}, status=HTTP_404_NOT_FOUND)

    # GET
    if request.method == 'GET':
        ld_row = Last_download.objects.get(table_name = "Customers") # find row with table_name = "Types"
        last_download = ld_row.last_download
        last_download_str = last_download.strftime("%Y-%m-%d %H:%M:%S")
        body_data = '{"user_id":"' + username  + '", "pswd":"' + password + '"}'
        result = requests.get('http://localhost:8082/api/customers/'+last_download_str, data=body_data)

        aList = result.json() # json.loads(result)
        for i in range(len(aList)):
            t_id = int(aList[i]["id"])
            #t_type_name = aList[i]["type_name"]
            types_row = Types.objects.get(type_name = aList[i]["type_name"]) # find row for references
            

            try:
                t_row = Customers.objects.get(id_orig = t_id)
                t_row.type_name = types_row # types_id 
                t_row.first_name = aList[i]["first_name"]
                t_row.middle_name = aList[i]["middle_name"]
                t_row.last_name = aList[i]["last_name"]

                t_row.TIN = aList[i]["TIN"]
                t_row.mobile_p = aList[i]["mobile_p"]
                t_row.station_p = aList[i]["station_p"]
                t_row.address = aList[i]["address"]
                t_row.desc = aList[i]["description"]

                t_row.date_create = aList[i]["date_create"]
                t_row.dataset = aList[i]["dataset"]
                t_row.last_user = aList[i]["last_user"]
                t_row.last_changed = aList[i]["last_changed"]
                t_row.save() # save exists row into Customers
            except Customers.DoesNotExist:
                obj=Customers.objects.create(id_orig=aList[i]["id"], type_name=types_row, 
                first_name = aList[i]["first_name"], middle_name = aList[i]["middle_name"], last_name = aList[i]["last_name"],
                TIN = aList[i]["TIN"], mobile_p = aList[i]["mobile_p"],
                station_p = aList[i]["station_p"], address = aList[i]["address"],
				desc=aList[i]["description"],
				date_create=aList[i]["date_create"], dataset = aList[i]["dataset"],
				last_user=aList[i]["last_user"], last_changed=aList[i]["last_changed"])
                obj.save() # insert new row into Customers

        ld_row.last_download = datetime.now()
        ld_row.save() # save date and time last download into table Types

        return Response("Recived results")

@api_view(['GET', 'POST', 'DELETE'])		
def customers_deals_list(request):
    
    # Authentication
    username = request.data.get("user_id")
    password = request.data.get("pswd")
    if username is None or password is None:
        return Response({'error': 'Please provide both username and password'}, status=HTTP_400_BAD_REQUEST)
    user = authenticate(username=username, password=password)
    if not user:
        return Response({'error': 'Invalid Credentials'}, status=HTTP_404_NOT_FOUND)

    # GET
    if request.method == 'GET':
        ld_row = Last_download.objects.get(table_name = "Customers_deals") # find row with table_name = "Types"
        last_download = ld_row.last_download
        last_download_str = last_download.strftime("%Y-%m-%d %H:%M:%S")
        body_data = '{"user_id":"' + username  + '", "pswd":"' + password + '"}'
        result = requests.get('http://localhost:8082/api/customers_deals/'+last_download_str, data=body_data)

        aList = result.json() # json.loads(result)
        for i in range(len(aList)):
            t_id = int(aList[i]["id"])
            #t_type_name = aList[i]["type_name"]
            customers_row = Customers.objects.get(id_orig = aList[i]["id_customer"]) # find row for references
            

            try:
                t_row = Customers_deals.objects.get(id_orig = t_id)
                t_row.id_customer = customers_row # types_id 
                t_row.name_good = aList[i]["name_good"]
                t_row.price = aList[i]["price"]
                t_row.quantity = aList[i]["quantity"]

                t_row.sum = aList[i]["sum"]
                t_row.date_deal = aList[i]["date_deal"]
                t_row.details = aList[i]["details"]
                t_row.dataset = aList[i]["dataset"]
                t_row.last_user = aList[i]["last_user"]

                t_row.save() # save exists row into Customers
            except Customers_deals.DoesNotExist:
                obj=Customers_deals.objects.create(id_orig=aList[i]["id"], id_customer = customers_row, 
                name_good = aList[i]["name_good"], price = aList[i]["price"], quantity = aList[i]["quantity"],
                sum = aList[i]["sum"], date_deal = aList[i]["date_deal"],
                details = aList[i]["details"], dataset = aList[i]["dataset"],
				last_user=aList[i]["last_user"])
                obj.save() # insert new row into Customers

        ld_row.last_download = datetime.now()
        ld_row.save() # save date and time last download into table Types

        return Response("Recived results")

@api_view(['GET', 'POST', 'DELETE'])		
def products_list(request):
    
    # Authentication
    username = request.data.get("user_id")
    password = request.data.get("pswd")
    if username is None or password is None:
        return Response({'error': 'Please provide both username and password'}, status=HTTP_400_BAD_REQUEST)
    user = authenticate(username=username, password=password)
    if not user:
        return Response({'error': 'Invalid Credentials'}, status=HTTP_404_NOT_FOUND)

    # GET
    if request.method == 'GET':
        ld_row = Last_download.objects.get(table_name = "Products") # find row with table_name = "Types"
        last_download = ld_row.last_download
        last_download_str = last_download.strftime("%Y-%m-%d %H:%M:%S")
        body_data = '{"user_id":"' + username  + '", "pswd":"' + password + '"}'
        result = requests.get('http://localhost:8082/api/products/'+last_download_str, data=body_data)

        aList = result.json() # json.loads(result)
        for i in range(len(aList)):
            t_id = int(aList[i]["id"])

            types_row = Types.objects.get(type_name = aList[i]["type_name"]) # find row for references
            brands_row = Brands.objects.get(brand_name = aList[i]["brand_name"]) # find row for references
            try:
                t_row = Products.objects.get(id_orig = t_id)
                t_row.type_name = types_row # references to Types table 
                t_row.brand_name = brands_row # references to Brands table
                t_row.product_name = aList[i]["product_name"]
                t_row.measure = aList[i]["measure"]

                t_row.price = aList[i]["price"]
                t_row.desc = aList[i]["description"]
                t_row.date_create = aList[i]["date_create"]
                t_row.dataset = aList[i]["dataset"]
                t_row.last_user = aList[i]["last_user"]

                t_row.last_changed = aList[i]["last_changed"]

                t_row.save() # save exists row into Customers
            except Products.DoesNotExist:
                obj=Products.objects.create(id_orig=aList[i]["id"], type_name = types_row, 
                brand_name = brands_row, product_name = aList[i]["product_name"], measure = aList[i]["measure"],
                price = aList[i]["price"], desc = aList[i]["description"],
                date_create = aList[i]["date_create"], dataset = aList[i]["dataset"], last_user = aList[i]["last_user"],
				last_changed = aList[i]["last_changed"])
                obj.save() # insert new row into Customers

        ld_row.last_download = datetime.now()
        ld_row.save() # save date and time last download into table Customers

        return Response("Recived results")

@api_view(['GET', 'POST', 'PATCH'])		
def log_dwh(request):
    
    # Authentication
    username = request.data.get("user_id")
    password = request.data.get("pswd")
    if username is None or password is None:
        return Response({'error': 'Please provide both username and password'}, status=HTTP_400_BAD_REQUEST)
    user = authenticate(username=username, password=password)
    if not user:
        return Response({'error': 'Invalid Credentials'}, status=HTTP_404_NOT_FOUND)

    # POST
    if request.method == 'POST':
        serializer = Log_dwhSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
        table_name = request.data.get("table_name")
        body_data = '{"table_name": "' + table_name + '", "user_id":"' + username  + '", "pswd":"' + password + '"}'
        response = requests.post('http://localhost:8081/api/log_dwh', data=body_data)
        return Response("Ok!")

"""
    if request.method == 'PATCH':
        last_user = 'kv'
        #t ="000"
        serializer = Log_dwhSerializer(data=request.data)
        if serializer.is_valid():
            serializer.save()
            post_data = '{"table": "Types", "user":"' + last_user + '"}'
            response = requests.post('http://localhost:8081/api/log_dwh', data=post_data)
        #last_user = request.POST.get('last_user', None)
        #last_user = json.dumps(serializer)
        #data=request.data
        #t = type(data)
        #json_object = json.loads(data=request.data)
        #last_user = serializer["last_user"]
        #
        #content = response.content
        return Response("Recived results:" + "t")

@api_view(['GET', 'POST', 'PATCH'])	
def log_dwh2(request): # , *args, **kwargs
    if request.method == 'PATCH':
        '''
        Create the Todo with given todo data
        
        '''
        data = {
            'actions': request.data.get('actions'), 
            'dataset': request.data.get('dataset'), 
            'last_user': request.data.get('last_user')
        }
        serializer = Log_dwhSerializer2(data=data)#request.
        if serializer.is_valid():
            serializer.save()
            return Response(serializer.data, status=status.HTTP_201_CREATED)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    if request.method == 'POST':
        '''
        Create the Todo with given todo data
        
        '''
        data = {
            'actions': 'Downloaded table ' + request.data.get('actions'), 
            'dataset': request.data.get('dataset'), 
            'last_user': request.data.get('last_user')
        }
        last_user =request.data.get('last_user')
        serializer = Log_dwhSerializer2(data=data)#request.
        if serializer.is_valid():
            serializer.save()
            post_data = '{"table": "Types", "user":"' + last_user + '"}'
            response = requests.post('http://localhost:8081/api/log_dwh', data=post_data)
            return Response(serializer.data, status=status.HTTP_201_CREATED)

        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)    

@api_view(['POST', 'PATCH', 'DELETE'])	
def log_dwh3(request, pk): # , *args, **kwargs
    if request.method == 'DELETE': 
        try: 
            log_dwh = Log_dwh.objects.get(pk=pk) 
            log_dwh.delete() 
            return Response("OK!!!", status=status.HTTP_201_CREATED)
        except log_dwh.DoesNotExist: 
            return Response("Errors", status=status.HTTP_400_BAD_REQUEST)
        
# LOgin
#@csrf_exempt
@api_view(["POST"])
#@permission_classes((AllowAny,))
def login(request):
    username = request.data.get("user_id")
    password = request.data.get("pswd")
    if username is None or password is None:
        return Response({'error': 'Please provide both username and password'},
                        status=HTTP_400_BAD_REQUEST)
    user = authenticate(username=username, password=password)
    if not user:
        return Response({'error': 'Invalid Credentials'},
                        status=HTTP_404_NOT_FOUND)
    #token, _ = Token.objects.get_or_create(user=user)
    return Response({'token': "token.key"},
                    status=HTTP_200_OK)
"""