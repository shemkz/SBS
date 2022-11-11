from rest_framework import serializers
from .models import Types, Customers, Customers_deals, Brands, Products, Last_download, Log_dwh

class TypesSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Types
        fields = ('type_name', 'desc', 'date_create', 'dataset', 'last_user', 'last_changed')

class CustomersSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Customers
        fields = ('type_name', 'first_name', 'middle_name', 'last_name',
                    'TIN', 'mobile_p', 'station_p', 'address', 'desc',
                    'date_create', 'dataset', 'last_user', 'last_changed')

class Customers_dealsSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Customers_deals
        fields = ('id_customer', 'name_good', 'price', 'quantity',
                    'sum', 'date_deal', 'details', 'dataset', 'last_user', 
                    'last_changed')

class BrandsSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Brands
        fields = ('brand_name', 'desc', 'date_create', 'dataset', 
                    'last_user', 'last_changed')

class ProductsSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Products
        fields = ('type_name', 'brand_name', 'product_name', 'measure',
                'price', 'desc', 'date_create', 'dataset', 'last_user', 
                'last_changed')

class Last_downloadSerializer(serializers.ModelSerializer):
    class Meta:
        model = Last_download
        fields = ('last_user') #'last_download', 'desc', 'date_create', 'dataset',table_name',  , 'last_changed'

class Log_dwhSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
        model = Log_dwh
        fields = ('actions', 'dataset', 'last_user')#, 'last_changed' # fields, which we must fill