from django.views.decorators.csrf import csrf_exempt
from django.http.response import JsonResponse

@csrf_exempt
def Check(request):
    if request.method == 'GET':
        response = {
            'status': 200,
            'message': 'Connection established',
        }

        return JsonResponse(response)