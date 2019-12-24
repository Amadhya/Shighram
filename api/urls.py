from django.urls import path

from api.views import *

urlpatterns = [
    path('check', Check, name='check'),
    path('login', login, name='login'),
    path('signup', signup, name='signup'),
    path('payment', payment, name='payment'),
    path('paymentOrder/<str:rfid>', paymentOrder, name='payment order'),
    path('paymentVerification/<str:rfid>', paymentVerification, name='payment verification')
]
