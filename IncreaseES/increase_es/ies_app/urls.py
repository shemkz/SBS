from django.urls import path
from django.contrib import admin

from .views import index, index_login

from .views import seo_level, seo_level_save, seo_level_insert, seo_level_delete
from .views import seo_rules, seo_rules_save, seo_rules_insert, seo_rules_delete
from .views import sites, sites_save, sites_insert, sites_delete
from .views import site_pages, site_pages_save, site_pages_insert, site_pages_delete
from .views import search_engine_system, search_engine_system_save, search_engine_system_insert, search_engine_system_delete
from .views import sites_seo_results
from .views import seo_results, seo_results_save, seo_results_insert, seo_results_delete
from .views import key_phrases, key_phrases_save, key_phrases_insert, key_phrases_delete

from .views import marketplace_rules, marketplace_rules_save, marketplace_rules_insert, marketplace_rules_delete
from .views import marketplaces, marketplaces_save, marketplaces_insert, marketplaces_delete
from .views import marketplace_sales, marketplace_sales_save, marketplace_sales_insert, marketplace_sales_delete

from .views import users, users_save, users_insert, users_delete
from .views import change_password, change_password_update

from .views import report_best_seo_rules, report_best_marketplace_rules

from .views import replication_dwh

urlpatterns = [
    path("", index),
    path('index_login/', index_login, name='index_login'),# ajax-posting / name = that we will use in ajax url

    path('seo_level/', seo_level, name='seo_level'),
    path('seo_level_save/', seo_level_save, name='seo_level_save'),
    path('seo_level_insert/', seo_level_insert, name='seo_level_insert'),
    path('seo_level_delete/', seo_level_delete, name='seo_level_delete'),

    path('seo_rules/', seo_rules, name='seo_rules'),
    path('seo_rules_save/', seo_rules_save, name='seo_rules_save'),
    path('seo_rules_insert/', seo_rules_insert, name='seo_rules_insert'),
    path('seo_rules_delete/', seo_rules_delete, name='seo_rules_delete'),

    path('sites/', sites, name='sites'),
    path('sites_save/', sites_save, name='sites_save'),
    path('sites_insert/', sites_insert, name='sites_insert'),
    path('sites_delete/', sites_delete, name='sites_delete'),

    path('site_pages/<str:name_site>', site_pages, name='site_pages'), #<str:name_site>
    path('site_pages_save/', site_pages_save, name='site_pages_save'),
    path('site_pages_insert/', site_pages_insert, name='site_pages_insert'),
    path('site_pages_delete/', site_pages_delete, name='site_pages_delete'),

    path('search_engine_system/', search_engine_system, name='search_engine_system'), 
    path('search_engine_system_save/', search_engine_system_save, name='search_engine_system_save'),
    path('search_engine_system_insert/', search_engine_system_insert, name='search_engine_system_insert'),
    path('search_engine_system_delete/', search_engine_system_delete, name='search_engine_system_delete'),

    path('sites_seo_results/', sites_seo_results, name='sites_seo_results'),

    path('seo_results/<str:name_site>/<str:search_system>', seo_results, name='seo_results'), #<str:name_site>
    path('seo_results_save/', seo_results_save, name='seo_results_save'),
    path('seo_results_insert/', seo_results_insert, name='seo_results_insert'),
    path('seo_results_delete/', seo_results_delete, name='seo_results_delete'),

    path('key_phrases/', key_phrases, name='key_phrases'), #<str:name_site>
    path('key_phrases_save/', key_phrases_save, name='key_phrases_save'),
    path('key_phrases_insert/', key_phrases_insert, name='key_phrases_insert'),
    path('key_phrases_delete/', key_phrases_delete, name='key_phrases_delete'),
    
    path('marketplace_rules/', marketplace_rules, name='marketplace_rules'),
    path('marketplace_rules_save/', marketplace_rules_save, name='marketplace_rules_save'),
    path('marketplace_rules_insert/', marketplace_rules_insert, name='marketplace_rules_insert'),
    path('marketplace_rules_delete/', marketplace_rules_delete, name='marketplace_rules_delete'),

    path('marketplaces/', marketplaces, name='marketplaces'),
    path('marketplaces_save/', marketplaces_save, name='marketplaces_save'),
    path('marketplaces_insert/', marketplaces_insert, name='marketplaces_insert'),
    path('marketplaces_delete/', marketplaces_delete, name='marketplaces_delete'),

    path('marketplace_sales/<str:name_marketplace>', marketplace_sales, name='marketplace_sales'),
    path('marketplace_sales_save/', marketplace_sales_save, name='marketplace_sales_save'),
    path('marketplace_sales_insert/', marketplace_sales_insert, name='marketplace_sales_insert'),
    path('marketplace_sales_delete/', marketplace_sales_delete, name='marketplace_sales_delete'),

    path('users/', users, name='users'),
    path('users_save/', users_save, name='users_save'),
    path('users_insert/', users_insert, name='users_insert'),
    path('users_delete/', users_delete, name='users_delete'),

    path('change_password/', change_password, name='change_password'),
    path('change_password_update/', change_password_update, name='change_password_update'),

    path('report_best_seo_rules/', report_best_seo_rules, name='report_best_seo_rules'),
    path('report_best_marketplace_rules/', report_best_marketplace_rules, name='report_best_marketplace_rules'),

    path('replication_dwh/', replication_dwh, name='replication_dwh'),
]