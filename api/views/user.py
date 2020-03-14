import json
import jwt
from django.views.decorators.csrf import csrf_exempt
from django.http.response import JsonResponse
from rest_framework import exceptions
from rest_framework.authentication import get_authorization_header
from django.contrib.auth.hashers import (
    check_password
)

from api.models import *
from api.views.authoriztion import authenticate
from api.views.otp import TOTPVerification


@csrf_exempt
def login(request):
    if request.method == 'POST':
        body = json.loads(request.body)
        email = body['email']

        if User.objects.get_by_email(email=email) is None:
            response = JsonResponse({'status': 400, 'message': 'Username or Password is not correct'}, status=400)
            return response

        password = body['password']
        user = User.objects.authenticate(email=email, password=password)

        if user is not None:
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

            return JsonResponse(response)

        response = JsonResponse({'status': 400, 'message': 'Username or Password is not correct'}, status=400)
        return response


    response = JsonResponse({'status': 400, 'message': 'Invlaid request method'}, status=400)
    return response

@csrf_exempt
def signup(request):
    if request.method == 'POST':
        body = json.loads(request.body)
        email = body.pop('email')
        password = body.pop('password')
        user = User.objects.get_by_email(email=email)

        if user is None:
            user = User.objects.create_user(email=email, password=password, **body)

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

        response = JsonResponse({'status': 400, 'message': 'This email is already registered'}, status=400)
        return response

    response = JsonResponse({'status': 400, 'message': 'Invlaid request method'}, status=400)
    return response


@csrf_exempt
def user_profile(request):
    if request.method == 'GET':
        isAuth, email = authenticate(request)
        if isAuth:
            user = User.objects.get_by_email(email)
            response = {
                'status': 200,
                **user.serialize(),
            }
            return JsonResponse(response, status=200)

        response = {
            'status': 400,
            'message': 'Not authorized',
        }

        return JsonResponse(response, status=400)

    response = {
        'status': 400,
        'message': 'Invalid request method',
    }

    return JsonResponse(response, status=400)


@csrf_exempt
def edit_user_details(request):
    if request.method == 'PATCH':
        isAuth, email = authenticate(request)
        if isAuth:
            response = {}

            body = json.loads(request.body)

            user = User.objects.get_by_email(email)

            if body.get('firstName') is not None:
                user.first_name = body.pop('firstName')
            
            if body.get('last') is not None:
                user.last_name = body.pop('lastName')

            if body.get('phone') is not None:
                user.phone = body.pop('phone')

            if body.get('email') is not None:
                user.email = body.pop('email')
                payload = {
                    'email': user.email,
                    'password': user.password,
                }

                jwt_token = {'token': jwt.encode(payload, "SECRET_KEY").decode('utf-8')}
                response = {
                    'token': jwt_token.get('token'),
                }
            
            user.save()
            response = {
                'status': 200,
                'message': 'Successfully changed',
                **response
            }
            
            return JsonResponse(response, status=200)
        
        response = {
            'status': 400,
            'message': 'Not authorized to change the credentials',
        }

        return JsonResponse(response, status=400)

    response = {
        'status': 400,
        'message': 'Invalid request method',
    }

    return JsonResponse(response, status=400)


@csrf_exempt
def change_password(request):
    if request.method == 'PATCH':
        isAuth, email = authenticate(request)
        if isAuth:
            body = json.loads(request.body)
            user = User.objects.get_by_email(email)
    
            if check_password(body.pop('current_password'), user.password):
                user.set_password(body.pop('new_password'))
                user.save()

                payload = {
                    'email': user.email,
                    'password': user.password,
                }
                jwt_token = {'token': jwt.encode(payload, "SECRET_KEY").decode('utf-8')}

                response = {
                    'status': 200,
                    'token': jwt_token.get('token'),
                }

                return JsonResponse(response, status=200)

            response = {
                'status': 400,
                'message': 'Current Password incorrect',
            }

            return JsonResponse(response, status=400)
        
        response = {
            'status': 400,
            'message': 'Not authorized to change the credentials',
        }

        return JsonResponse(response, status=400)

    response = {
        'status': 400,
        'message': 'Invalid request method',
    }

    return JsonResponse(response, status=400)

@csrf_exempt
def otp_generation(request):
    if request.method == 'POST':
        body = json.loads(request.body)
        email = body.pop('email')
        user = User.objects.get_by_email(email)

        if user is not None:
            totp = TOTPVerification()
            kwargs = {
                'user': user
            }
            respone = totp.generate_token(**kwargs)

            response = {
                'status': 200,
                **respone
            }

            return JsonResponse(response, status=200)
        
        response = {
            'status': '400',
            'message': 'Invalid email',
        }

        return JsonResponse(response, status=int(response.get('status')))

@csrf_exempt
def otp_verification(request):
    if request.method=='PATCH':
        body = json.loads(request.body)
        email = body.pop('email')
        token=body.pop('token')
        user = User.objects.get_by_email(email)

        if user is not None:
            totp = TOTPVerification()

            response = totp.verify_token(token,user)

            return JsonResponse(response, status=int(response.get('status')))

        response = {
            'status': '400',
            'message': 'Invalid email',
        }

        return JsonResponse(response, status=int(response.get('status')))