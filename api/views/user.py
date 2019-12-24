import json
import jwt
from django.views.decorators.csrf import csrf_exempt
from django.http.response import JsonResponse

from api.models import *
from api.views.authoriztion import authenticate


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
            
            return JsonResponse(response)

        response = JsonResponse({'status': 400, 'message': 'This email is already registered'}, status=400)
        return response

    response = JsonResponse({'status': 400, 'message': 'Invlaid request method'}, status=400)
    return response


@csrf_exempt
def user_profile(request, user_id):
    if request.method == 'GET' and authenticate(request):
        user = User.objects.get_by_id(user_id)
        response = {
            'user': user.serialize(),
        }
        return JsonResponse(response)
