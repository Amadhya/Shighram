import json
import razorpay
from django.utils.timezone import now
from django.views.decorators.csrf import csrf_exempt
from django.http.response import JsonResponse

from api.models import *

@csrf_exempt
def payment(request):
    if request.method == 'POST':
        body = json.loads(request.body)

        payment = Payment.create(**body)

        responese = {
            'status': 200,
            'payment': payment.serialize()
        }

        return JsonResponse(responese)

    response = JsonResponse({'status': 400, 'message': 'Invlaid request method'}, status=400)
    return response

@csrf_exempt
def paymentOrder(request, rfid):
    if request.method == 'PATCH':
        body = json.loads(request.body)
        paymentObj = Payment.objects.get_by_rfid(rfid)

        if paymentObj is None:
            response = {
                'status': 400,
                'message': 'Invalid rfid number. Please check again'
            }

            return JsonResponse(response)

        paymentObj.user_id = body.pop('user_id')
        user = User.objects.get_by_id(user_id=paymentObj.user_id)

        DATA = {
            'amount': str(paymentObj.amount),
            'currency': 'INR',
            'payment_capture': 1
        }

        client = razorpay.Client(auth=("rzp_test_aaYo7uFXPntX6s", "Fy7KKIBIxesTdDKYwtT60acJ"))
        order_response = client.order.create(data=DATA)
        order_id = order_response.get('id')

        payment.razorpay_order_id = order_id
        paymentObj.save()

        response = {
            'status': 200,
            'order': {
                'details': paymentObj.serialize(),
                'user': user.serialize(),
            },
        }

        return JsonResponse(response)

@csrf_exempt
def paymentVerification(request, rfid):
    if request.method == 'PATCH':
        body = json.loads(request.body)
        paymentObj = Payment.objects.get_by_rfid(rfid)

        paymentObj.razorpay_payment_id=body.pop('razorpay_payment_id')

        client = razorpay.Client(auth=("rzp_test_aaYo7uFXPntX6s", "Fy7KKIBIxesTdDKYwtT60acJ"))

        # generated_signature = hmac_sha256(razorpay_order_id + "|" + razorpay_payment_id, secret);


        # params_dict = {
        #     'razorpay_order_id': '12122',
        #     'razorpay_payment_id': '332',
        #     'razorpay_signature': '23233'
        # }

        # verified=client.utility.verify_payment_signature(params_dict)

        # paymentObj.verified=verified
        # paymentObj.save()

        # response = {
        #     'status': 200,
        #     'verified': verified,
        # }

        # return JsonResponse(response)