import json
import requests
import jwt

from google.oauth2 import id_token
from google.auth.transport import requests as req
from django.contrib.auth.base_user import BaseUserManager
from django.contrib.auth.hashers import make_password
from rest_framework.views import APIView
from rest_framework.response import Response
from django.views.decorators.csrf import csrf_exempt
from django.http.response import JsonResponse

from api.models.user import User
from SmartParkingSystem.settings import SECRET_KEY, GOOGLE_CLIENT_ID


@csrf_exempt
def google_login(request):
    if request.method == 'POST':
        body = json.loads(request.body)
        token = body.pop('id_token')
        payload = {'id_token': token}  # validate the token

        try:
            # Specify the CLIENT_ID of the app that accesses the backend:
            idinfo = id_token.verify_oauth2_token(token, req.Request(), GOOGLE_CLIENT_ID)

            # Or, if multiple clients access the backend server:
            # idinfo = id_token.verify_oauth2_token(token, requests.Request())
            # if idinfo['aud'] not in [CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3]:
            #     raise ValueError('Could not verify audience.')

            if idinfo['iss'] not in ['accounts.google.com', 'https://accounts.google.com']:
                raise ValueError('Wrong issuer.')

            # If auth request is from a G Suite domain:
            # if idinfo['hd'] != GSUITE_DOMAIN_NAME:
            #     raise ValueError('Wrong hosted domain.')

            # ID token is valid. Get the user's Google Account ID from the decoded token.
            userid = idinfo['sub']
        except ValueError:
            # Invalid token
            print(ValueError, 'error')
            pass

        r = requests.get('https://oauth2.googleapis.com/tokeninfo', params=payload)

        data = json.loads(r.text)

        aud = data['aud']
        
        if 'error' in data or aud != GOOGLE_CLIENT_ID:
            content = {
                'status': '400',
                'message': 'wrong google token / this google token is already expired.',
            }

            return JsonResponse(content,status=400)

        user = User.objects.get_by_email(email=data['email'])

        if user is None:
            kwargs = {
                'first_name': data['given_name'],
                'last_name': data['family_name'],
                'phone': '',
            }

            user = User.objects.create_user(email=data['email'],password=BaseUserManager().make_random_password(),**kwargs)

        payload = {
            'email': user.email,
            'password': user.password,
        }

        jwt_token = {'token': jwt.encode(payload, "SECRET_KEY").decode('utf-8')}

        response = {
            'status': 200,
            'user_id': user.id,
            'token': jwt_token.get('token'),
        }

        return JsonResponse(response, status=200)
    
    response = {
        'status': 400,
        'message': 'Invalid request method',
    }

    return JsonResponse(response, status=400)

@csrf_exempt
def google_login_access_token(request):
    if request.method == 'POST':
        body = json.loads(request.body)
        token = body.pop('access_token')
        payload = {'access_token': token}   # validate the token

        r = requests.get('https://www.googleapis.com/oauth2/v2/userinfo', params=payload)
        data = json.loads(r.text)

        if 'error' in data:
            content = {
                'status': '400',
                'message': 'wrong google token / this google token is already expired.'
            }
            return Response(content)

        r = requests.get('https://www.googleapis.com/oauth2/v1/userinfo', params=payload)

        data = json.loads(r.text)
        
        if 'error' in data:
            content = {
                'status': '400',
                'message': 'wrong google token / this google token is already expired.',
            }

            return JsonResponse(content,status=400)

        user = User.objects.get_by_email(email=data['email'])

        if user is None:
            kwargs = {
                'first_name': data['given_name'],
                'last_name': data['family_name'],
                'phone': '',
            }

            user = User.objects.create_user(email=data['email'],password=BaseUserManager().make_random_password(),**kwargs)

        payload = {
            'email': user.email,
            'password': user.password,
        }

        jwt_token = {'token': jwt.encode(payload, "SECRET_KEY").decode('utf-8')}

        response = {
            'status': 200,
            'user_id': user.id,
            'token': jwt_token.get('token'),
        }

        return JsonResponse(response, status=200)
    
    response = {
        'status': 400,
        'message': 'Invalid request method',
    }

    return JsonResponse(response, status=400)      