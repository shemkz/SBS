from django.db import models

# Create your models here.

class Types(models.Model):
    #class Meta:
    #    unique_together = (('type_name', 'dataset'),)
    
    id_orig = models.IntegerField(unique = True) # id from source, original database
    type_name = models.CharField(max_length=30) # types of goods or services
    desc = models.CharField(max_length=300, blank=True, null=True) # description
    date_create = models.DateField(null=True) #  date creating record
    dataset = models.IntegerField(null=True) # 1 - live, 0 – archive/deleted, -1/-2/-3 - reserv
    last_user  = models.CharField(max_length=20, blank=True, null=True) # last user
    last_changed = models.DateTimeField(null=True) # date and time last changed

class Customers(models.Model):
    #class Meta:
    #    unique_together = (('dataset', 'type_name', 'first_name', 'middle_name', 'last_name'),)
    
    id_orig = models.IntegerField(unique = True) # id from source, original database
    type_name = models.ForeignKey(Types, on_delete=models.PROTECT)
    first_name = models.CharField(max_length=20, blank=True, null=True)
    middle_name = models.CharField(max_length=20, blank=True, null=True)
    last_name = models.CharField(max_length=20, blank=True, null=True)

    TIN = models.CharField(max_length=30, blank=True, null=True) # INN
    mobile_p = models.CharField(max_length=20, blank=True, null=True)
    station_p = models.CharField(max_length=20, blank=True, null=True)
    address = models.CharField(max_length=50, blank=True, null=True)
    desc = models.CharField(max_length=300, blank=True, null=True) # description

    date_create = models.DateField(null=True) #  date creating record
    dataset  = models.IntegerField(null=True) # 1 - live, 0 – archive/deleted, -1/-2/-3 - reserv
    last_user  = models.CharField(max_length=20, blank=True, null=True) # last user
    last_changed = models.DateTimeField(null=True) # date and time last changed
 
class Customers_deals(models.Model):
    #class Meta:
    #    unique_together = (('dataset', 'id_customer', 'name_good', 'price', 'quantity', 'sum', 'date_deal'),)
    
    id_orig = models.IntegerField(unique = True) # id from source, original database
    id_customer = models.ForeignKey(Customers, on_delete=models.PROTECT)
    name_good = models.CharField(max_length=30, blank=True, null=True)
    price = models.BigIntegerField(null=True)
    quantity = models.IntegerField(null=True)

    sum = models.BigIntegerField(null=True)
    date_deal = models.DateField(null=True) #  date creating record
    details = models.CharField(max_length=300, blank=True, null=True) # description
    dataset = models.IntegerField(null=True) # 1 - live, 0 – archive/deleted, -1/-2/-3 - reserv
    last_user = models.CharField(max_length=20, blank=True, null=True) # last user

    last_changed = models.DateTimeField(null=True) # date and time last changed

class Brands(models.Model):
    #class Meta:
    #    unique_together = (('dataset', 'brand_name'),)

    id_orig = models.IntegerField(unique = True) # id from source, original database
    brand_name = models.CharField(max_length=30, blank=True, null=True)
    desc = models.CharField(max_length=300, blank=True, null=True) # description
    date_create = models.DateField(null=True) #  date creating record
    dataset  = models.IntegerField(null=True) # 1 - live, 0 – archive/deleted, -1/-2/-3 - reserv

    last_user  = models.CharField(max_length=20, blank=True, null=True) # last user
    last_changed = models.DateTimeField(null=True) # date and time last changed

class Products(models.Model):
    #class Meta:
    #    unique_together = (('dataset', 'type_name', 'brand_name', 'product_name', 'measure'),)

    id_orig = models.IntegerField(unique = True) # id from source, original database
    type_name = models.ForeignKey(Types, on_delete=models.PROTECT)
    brand_name = models.ForeignKey(Brands, on_delete=models.PROTECT)
    product_name = models.CharField(max_length=30, blank=True, null=True)
    measure = models.CharField(max_length=10, blank=True, null=True)

    price = models.BigIntegerField(null=True)
    desc = models.CharField(max_length=300, blank=True, null=True) # description
    date_create = models.DateField(null=True) #  date creating record
    dataset  = models.IntegerField(null=True) # 1 - live, 0 – archive/deleted, -1/-2/-3 - reserv

    last_user  = models.CharField(max_length=20, blank=True, null=True) # last user
    last_changed = models.DateTimeField(null=True) # date and time last changed

#**************************************************

# Last download data by tables
class Last_download(models.Model):
    class Meta:
        unique_together = (('table_name', 'dataset'),)
    table_name = models.CharField(max_length=30) # types of goods or services
    last_download = models.DateTimeField(null=True) # date and time last download
    desc = models.CharField(max_length=300, blank=True, null=True) # description
    date_create = models.DateField(null=True) #  date creating record
    dataset = models.IntegerField(null=True) # 1 - live, 0 – archive/deleted, -1/-2/-3 - reserv
    last_user  = models.CharField(max_length=20, blank=True, null=True) # last user
    last_changed = models.DateTimeField(null=True) # date and time last changed

# Log download data by tables
class Log_dwh(models.Model):
    #class Meta:
    #    unique_together = (('actions', 'dataset', 'last_changed'),)
    actions = models.CharField(max_length=40) # 
    desc = models.CharField(max_length=300, blank=True, null=True) # description
    date_create = models.DateField(null=True) #  date creating record
    dataset = models.IntegerField(null=True) # 1 - live, 0 – archive/deleted, -1/-2/-3 - reserv
    last_user  = models.CharField(max_length=20, blank=True, null=True) # last user
    last_changed = models.DateTimeField(null=True) # date and time last changed

#***************************
