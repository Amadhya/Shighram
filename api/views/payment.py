import json
import razorpay
from django.utils.timezone import now
from django.views.decorators.csrf import csrf_exempt
from django.http.response import JsonResponse

from api.models import User, Payment
from api.views import authenticate


@csrf_exempt
def verify_rfid(request):
    if request.method == 'PATCH':
        isAuth, email = authenticate(request)
        if isAuth:
            body = json.loads(request.body)
            paymentObj = Payment.objects.get_by_rfid(body.pop('rfid'))
            
            if paymentObj is None:
                response = {
                    'status': 400,
                    'message': 'Invalid rfid number',
                    'verified': 'False'
                }

                return JsonResponse(response, status=400)

            if paymentObj.amount=="0":
                responese = {
                    'status': 200,
                    'amount': '0',
                    'message': 'No due amount remaining.'
                }

                return JsonResponse(responese, status=200)

            user = User.objects.get_by_email(email)
            paymentObj.user_id = user.id

            DATA = {
                'amount': str(paymentObj.amount),
                'currency': 'INR',
                'payment_capture': 1
            }

            client = razorpay.Client(auth=("rzp_test_aaYo7uFXPntX6s", "Fy7KKIBIxesTdDKYwtT60acJ"))
            order_response = client.order.create(data=DATA)
            order_id = order_response.get('id')

            paymentObj.razorpay_order_id = order_id
            paymentObj.save()

            response = {
                    'status': 200,
                    'message': 'rfid number exist',
                    'user_name': user.first_name+' '+user.last_name,
                    'verified': 'True',
                    **paymentObj.serialize()
                }

            return JsonResponse(response, status = 200)

    response = {
        'status': 400,
        'message': 'Invalid request method',
    }

    return JsonResponse(response, status=400)

@csrf_exempt
def payment(request):
    if request.method == 'POST':
        body = json.loads(request.body)

        payment = Payment.create(**body)

        responese = {
            'status': 200,
            'payment': payment.serialize()
        }

        return JsonResponse(responese, status=200)

    response = {
        'status': 400,
        'message': 'Invalid request method',
    }

    return JsonResponse(response, status=400)

@csrf_exempt
def paymentOrder(request):
    if request.method == 'POST':
        isAuth, email = authenticate(request)
        if isAuth:
            body = json.loads(request.body)
            paymentObj = Payment.objects.get_by_rfid(body.pop('rfid'))
            user = User.objects.get_by_email(email)
            
            if paymentObj is None:
                response = {
                    'status': 400,
                    'message': 'Invalid rfid number. Please check again'
                }

                return JsonResponse(response, status=400)

            response = {
                'status': 200,
                **paymentObj.serialize(),
                **user.serialize()
            }

            return JsonResponse(response, status=200)

    response = {
        'status': 400,
        'message': 'Invalid request method',
    }

    return JsonResponse(response, status=400)

@csrf_exempt
def paymentVerification(request):
    if request.method == 'PATCH':
        if authenticate(request):
            body = json.loads(request.body)
            paymentObj = Payment.objects.get_by_razorpay_order_id(body.pop('razorpay_order_id'))

            paymentObj.razorpay_payment_id=body.pop('razorpay_payment_id')

            paymentObj.razorpay_signature=body.pop('razorpay_signature') 

            client = razorpay.Client(auth=("rzp_test_aaYo7uFXPntX6s", "Fy7KKIBIxesTdDKYwtT60acJ"))

            params_dict = {
                'razorpay_order_id': paymentObj.razorpay_order_id,
                'razorpay_payment_id': paymentObj.razorpay_payment_id,
                'razorpay_signature': paymentObj.razorpay_signature
            }

            verified=client.utility.verify_payment_signature(params_dict)
            
            paymentObj.amount = "0"

            paymentObj.verified=True
            paymentObj.save()

            response = {
                'status': 200,
                'verified': verified,
            }

            return JsonResponse(response)

    response = {
        'status': 400,
        'message': 'Invalid request method',
    }

    return JsonResponse(response, status=400)
