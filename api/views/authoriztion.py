import jwt
from rest_framework import exceptions
from django.views.decorators.csrf import csrf_exempt
from django.http.response import HttpResponse
from rest_framework.authentication import get_authorization_header

from api.models import *


@csrf_exempt
def authenticate(request):
    auth = get_authorization_header(request).split()

    if not auth or auth[0].lower() != b'bearer':
        return None

    if len(auth) == 1:
        msg = 'Invalid token header. No credentials provided.'
        raise exceptions.AuthenticationFailed(msg)
    elif len(auth) > 2:
        msg = 'Invalid token header'
        raise exceptions.AuthenticationFailed(msg)

    try:
        token = auth[1]
        if token == "null":
            msg = 'Null token not allowed'
            raise exceptions.AuthenticationFailed(msg)
    except UnicodeError:
        msg = 'Invalid token header. Token string should not contain invalid characters.'
        raise exceptions.AuthenticationFailed(msg)

    is_authenticated = authenticate_using_token(token)

    return is_authenticated


def authenticate_using_token(token):
    payload = jwt.decode(token, "SECRET_KEY")
    email = payload['email']
    password = payload['password']
    msg = {'Error': "Token mismatch", 'status': "401"}
    try:
        user = User.objects.get_by_email(email=email)
        if user.password == password:
            return True

        raise exceptions.AuthenticationFailed(msg)

    except jwt.ExpiredSignature or jwt.DecodeError or jwt.InvalidTokenError:
        return HttpResponse({'Error': "Token is invalid"}, status="403")
    except User is None:
        return HttpResponse({'Error': "Internal server error"}, status="500")