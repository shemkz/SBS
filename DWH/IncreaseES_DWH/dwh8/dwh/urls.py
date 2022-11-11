from django.urls import include, path 
from . import views 
 
urlpatterns = [ 
    path('api/types', views.types_list),
    path('api/brands', views.brands_list),
    path('api/customers', views.customers_list),
    path('api/customers_deals', views.customers_deals_list),
    path('api/products', views.products_list),
    path('api/log_dwh', views.log_dwh),
    
    #path('api/log_dwh2', views.log_dwh2),
    #path('api/log_dwh2/<str:pk>', views.log_dwh3),
    #path('api/login', views.login),
    #path('api/log_dwh2/<str:pk>', views.log_dwh2),
    #path('api/tutorials/<str:pk>', views.tutorial_detail),
]