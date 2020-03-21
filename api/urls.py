from django.urls import path

from api.views import *

urlpatterns = [
    path('check', Check, name='check'),
    path('login', login, name='login'),
    path('google_login', google_login, name='google login'),
    path('google_login_access_token', google_login_access_token, name='google login access token'),
    path('signup', signup, name='signup'),
    path('profile', user_profile, name='user profile'),
    path('editProfile', edit_user_details, name='edit user details'),
    path('generate_otp', otp_generation, name='generate otp'),
    path('verify_otp', otp_verification, name='verify otp'),
    path('change_password', change_password, name='change password'),
    path('password_reset_request', password_reset_request, name='password reset request'),
    path('reset_password', reset_password, name='password reset'),
    path('verify_rfid', verify_rfid, name='verify rfid'),
    path('payment', payment, name='payment'),
    path('paymentOrder', paymentOrder, name='payment order'),
    path('paymentVerification', paymentVerification, name='payment verification'),
    path('payment_history', payment_history, name='payment history'),
]
