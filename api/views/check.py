from django.http.response import JsonResponse
from rest_framework.decorators import api_view

@api_view(['GET'])
def Check(request):
    if request.method == 'GET':
        response = {
            'status': 200,
            'message': 'Connection established',
        }

        return JsonResponse(response)