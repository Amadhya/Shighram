from django.urls import path

from api.views import *

urlpatterns = [
    path('check', Check, name='check'),
    path('login', login, name='login'),
    path('signup', signup, name='signup'),
    path('profile', user_profile, name='user profile'),
    path('edit_profile/<uuid:user_id>', edit_user_details, name='edit user details'),
    path('change_password/<uuid:user_id>', change_password, name='change password'),
    path('payment', payment, name='payment'),
    path('paymentOrder', paymentOrder, name='payment order'),
    path('paymentVerification/<str:rfid>', paymentVerification, name='payment verification')
]
