from django_otp.oath import TOTP
from django_otp.util import random_hex
from unittest import mock
from SmartParkingSystem.settings import SECRET_KEY
from api.models import Otp

import time


class TOTPVerification:

    def __init__(self):
        # secret key that will be used to generate a token,
        # User can provide a custom value to the key.
        self.key = b'%b'%(SECRET_KEY.encode())
        # counter with which last token was verified.
        # Next token must be generated at a higher counter value.
        self.last_verified_counter = -1
        # number of digits in a token. Default is 6
        self.number_of_digits = 6
        # validity period of a token. Default is 30 second.
        self.token_validity_period = 60

    def totp_obj(self):
        # create a TOTP object

        totp = TOTP(key=self.key,
                    step=self.token_validity_period,
                    digits=self.number_of_digits)
        # the current time will be used to generate a counter
        totp.time = time.time()
        return totp

    def generate_token(self,**kwargs):
        # get the TOTP object and use that to create token
        totp = self.totp_obj()
        # token can be obtained with `totp.token()`
        token = str(totp.token()).zfill(6)

        kwargs = {
            'token': token,
            **kwargs
        }

        otp = Otp.create(**kwargs)

        return otp

    def verify_token(self, token, user,tolerance=0):
        response = {} 

        totp = self.totp_obj()
        # check if the current counter value is higher than the value of
        # last verified counter and check if entered token is correct by
        # calling totp.verify_token()
        if ((totp.t() > self.last_verified_counter) and
                (totp.verify(int(token), tolerance=tolerance))):
            # if the condition is true, set the last verified counter value
            # to current counter value, and return True
            self.last_verified_counter = totp.t()
            otp = Otp.objects.get_by_token(token)

            if not user == otp.user:
                response = {
                    'status': '400',
                    'message': 'otp is not valid'
                }

            otp.verified = True
            otp.save()

            response = {
                'status': '200',
                'message': 'otp is valid',
                'verified': str(otp.verified)
            }
        else:
            response = {
                'status': '400',
                'message': 'otp has expired'
            }

        return response