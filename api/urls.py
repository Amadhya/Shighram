from django.urls import path

from api.views import *

urlpatterns = [
    path('check', Check, name='check'),
    path('login', login, name='login'),
    path('signup', signup, name='signup'),
    path('profile', user_profile, name='user profile'),
    path('editProfile', edit_user_details, name='edit user details'),
    path('change_password', change_password, name='change password'),
    path('verify_rfid', verify_rfid, name='verify rfid'),
    path('payment', payment, name='payment'),
    path('paymentOrder', paymentOrder, name='payment order'),
    path('paymentVerification', paymentVerification, name='payment verification')
]
